/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.LeftClickListener;
import net.habiclient.events.RenderListener;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.BlockListSetting;
import net.habiclient.settings.BlockSetting;
import net.habiclient.settings.CheckboxSetting;
import net.habiclient.settings.EnumSetting;
import net.habiclient.settings.SliderSetting;
import net.habiclient.util.BlockUtils;
import net.habiclient.util.RenderUtils;
import net.habiclient.util.RotationUtils;

@SearchTags({"LegitNuker", "nuker legit", "legit nuker"})
public final class NukerLegitHack extends Hack
	implements LeftClickListener, RenderListener, UpdateListener
{
	private final SliderSetting range = new SliderSetting("范围", 4.25, 1.0, 4.25, 0.05, SliderSetting.ValueDisplay.DECIMAL);
    private final EnumSetting<Mode> mode = new EnumSetting("模式", "§l普通§r 模式很简单的破坏\n你周边的东西.\n§lID§r 模式只破坏所选的方块\n类型. 左键方块选择其方块.\n§l多个ID§r 模式只破坏那些你选择\n在你 多个ID 列表.\n§l平坦§r 模式只会挖你水平上的方块,\n但不会往下挖.\n§l粉碎§r 模式只会破坏那些\n能够瞬间破坏的方块 (例.如. 高大的草).", (Enum[])Mode.values(), (Enum)Mode.NORMAL);
    private final BlockSetting id = new BlockSetting("ID", "在ID模式,将会破坏指定ID的方块类型.\nair = 不会破坏任何东西", "minecraft:air", true);
    private final CheckboxSetting lockId = new CheckboxSetting("锁ID", "保护且不会导致因点击其他方块\n而改变挖掘的方块,同时也不会因重启而重置.", false);
	
	private final BlockListSetting multiIdList = new BlockListSetting(
		"多个ID列表", "有多个方块将会被破坏在多个ID列表模式.",
		"minecraft:ancient_debris", "minecraft:bone_block", "minecraft:clay",
		"minecraft:coal_ore", "minecraft:diamond_ore", "minecraft:emerald_ore",
		"minecraft:glowstone", "minecraft:gold_ore", "minecraft:iron_ore",
		"minecraft:lapis_ore", "minecraft:nether_gold_ore",
		"minecraft:nether_quartz_ore", "minecraft:redstone_ore");
	
	private BlockPos currentBlock;
	
	public NukerLegitHack()
	{
		super("矿井-");
		
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(mode);
		addSetting(id);
		addSetting(lockId);
		addSetting(multiIdList);
	}
	
	@Override
	public String getRenderName()
	{
		return mode.getSelected().getRenderName(this);
	}
	
	@Override
	public void onEnable()
	{
		// disable other nukers
		WURST.getHax().autoMineHack.setEnabled(false);
		WURST.getHax().excavatorHack.setEnabled(false);
		WURST.getHax().nukerHack.setEnabled(false);
		WURST.getHax().speedNukerHack.setEnabled(false);
		WURST.getHax().tunnellerHack.setEnabled(false);
		
		// add listeners
		EVENTS.add(LeftClickListener.class, this);
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		// remove listeners
		EVENTS.remove(LeftClickListener.class, this);
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		
		// resets
		MC.options.attackKey.setPressed(false);
		currentBlock = null;
		if(!lockId.isChecked())
			id.setBlock(Blocks.AIR);
	}
	
	@Override
	public void onLeftClick(LeftClickEvent event)
	{
		// check mode
		if(mode.getSelected() != Mode.ID)
			return;
		
		if(lockId.isChecked())
			return;
		
		// check hitResult
		if(MC.crosshairTarget == null
			|| !(MC.crosshairTarget instanceof BlockHitResult))
			return;
		
		// check pos
		BlockPos pos = ((BlockHitResult)MC.crosshairTarget).getBlockPos();
		if(pos == null
			|| BlockUtils.getState(pos).getMaterial() == Material.AIR)
			return;
		
		// set id
		id.setBlockName(BlockUtils.getName(pos));
	}
	
	@Override
	public void onUpdate()
	{
		// abort if using IDNuker without an ID being set
		if(mode.getSelected() == Mode.ID && id.getBlock() == Blocks.AIR)
			return;
		
		currentBlock = null;
		
		// get valid blocks
		Iterable<BlockPos> validBlocks = getValidBlocks(range.getValue(),
			mode.getSelected().getValidator(this));
		
		// find closest valid block
		for(BlockPos pos : validBlocks)
		{
			// break block
			if(!breakBlockExtraLegit(pos))
				continue;
			
			// set currentBlock if successful
			currentBlock = pos;
			break;
		}
		
		// reset if no block was found
		if(currentBlock == null)
			MC.options.attackKey.setPressed(false);
	}
	
	private ArrayList<BlockPos> getValidBlocks(double range,
		Predicate<BlockPos> validator)
	{
		Vec3d eyesVec = RotationUtils.getEyesPos().subtract(0.5, 0.5, 0.5);
		double rangeSq = Math.pow(range + 0.5, 2);
		int rangeI = (int)Math.ceil(range);
		
		BlockPos center = new BlockPos(RotationUtils.getEyesPos());
		BlockPos min = center.add(-rangeI, -rangeI, -rangeI);
		BlockPos max = center.add(rangeI, rangeI, rangeI);
		
		return BlockUtils.getAllInBox(min, max).stream()
			.filter(pos -> eyesVec.squaredDistanceTo(Vec3d.of(pos)) <= rangeSq)
			.filter(BlockUtils::canBeClicked).filter(validator)
			.sorted(Comparator.comparingDouble(
				pos -> eyesVec.squaredDistanceTo(Vec3d.of(pos))))
			.collect(Collectors.toCollection(ArrayList::new));
	}
	
	private boolean breakBlockExtraLegit(BlockPos pos)
	{
		Vec3d eyesPos = RotationUtils.getEyesPos();
		Vec3d posVec = Vec3d.ofCenter(pos);
		double distanceSqPosVec = eyesPos.squaredDistanceTo(posVec);
		
		for(Direction side : Direction.values())
		{
			Vec3d hitVec = posVec.add(Vec3d.of(side.getVector()).multiply(0.5));
			double distanceSqHitVec = eyesPos.squaredDistanceTo(hitVec);
			
			// check if hitVec is within range (4.25 blocks)
			if(distanceSqHitVec > 18.0625)
				continue;
			
			// check if side is facing towards player
			if(distanceSqHitVec >= distanceSqPosVec)
				continue;
			
			// check line of sight
			if(MC.world
				.raycast(new RaycastContext(eyesPos, hitVec,
					RaycastContext.ShapeType.COLLIDER,
					RaycastContext.FluidHandling.NONE, MC.player))
				.getType() != HitResult.Type.MISS)
				continue;
			
			// face block
			WURST.getRotationFaker().faceVectorClient(hitVec);
			
			if(currentBlock != null)
				WURST.getHax().autoToolHack.equipIfEnabled(currentBlock);
				
			// if attack key is down but nothing happens, release it for one
			// tick
			if(MC.options.attackKey.isPressed()
				&& !MC.interactionManager.isBreakingBlock())
			{
				MC.options.attackKey.setPressed(false);
				return true;
			}
			
			// damage block
			MC.options.attackKey.setPressed(true);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(currentBlock == null)
			return;
		
		// GL settings
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		matrixStack.push();
		RenderUtils.applyRegionalRenderOffset(matrixStack);
		
		BlockPos camPos = RenderUtils.getCameraBlockPos();
		int regionX = (camPos.getX() >> 9) * 512;
		int regionZ = (camPos.getZ() >> 9) * 512;
		
		// set position
		matrixStack.translate(currentBlock.getX() - regionX,
			currentBlock.getY(), currentBlock.getZ() - regionZ);
		
		// get progress
		float progress;
		if(BlockUtils.getHardness(currentBlock) < 1)
			progress = IMC.getInteractionManager().getCurrentBreakingProgress();
		else
			progress = 1;
		
		// set size
		if(progress < 1)
		{
			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.scale(progress, progress, progress);
			matrixStack.translate(-0.5, -0.5, -0.5);
		}
		
		// get color
		float red = progress * 2F;
		float green = 2 - red;
		
		// draw box
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		RenderSystem.setShaderColor(red, green, 0, 0.25F);
		RenderUtils.drawSolidBox(matrixStack);
		
		RenderSystem.setShaderColor(red, green, 0, 0.5F);
		RenderUtils.drawOutlinedBox(matrixStack);
		
		matrixStack.pop();
		
		// GL resets
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	private enum Mode
	{
		NORMAL("普通", n -> "合法版挖块", (n, p) -> true),
		
		ID("ID", n -> "ID合法版挖块 ["
				+ n.id.getBlockName().replace("minecraft:", "") + "]",
			(n, p) -> BlockUtils.getName(p).equals(n.id.getBlockName())),
		
		MULTI_ID("多ID", n -> "多ID合法版挖块 [" + n.multiIdList.getBlockNames().size()
				+ (n.multiIdList.getBlockNames().size() == 1 ? " ID]"
					: " IDs]"),
			(n, p) -> n.multiIdList.getBlockNames()
				.contains(BlockUtils.getName(p))),
		
		FLAT("平坦", n -> "平坦合法版挖块",
			(n, p) -> p.getY() >= MC.player.getPos().getY()),
		
		SMASH("粉碎", n -> "粉碎合法版挖块",
			(n, p) -> BlockUtils.getHardness(p) >= 1);
		
		private final String name;
		private final Function<NukerLegitHack, String> renderName;
		private final BiPredicate<NukerLegitHack, BlockPos> validator;
		
		private Mode(String name, Function<NukerLegitHack, String> renderName,
			BiPredicate<NukerLegitHack, BlockPos> validator)
		{
			this.name = name;
			this.renderName = renderName;
			this.validator = validator;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
		
		public String getRenderName(NukerLegitHack n)
		{
			return renderName.apply(n);
		}
		
		public Predicate<BlockPos> getValidator(NukerLegitHack n)
		{
			return p -> validator.test(n, p);
		}
	}
}
