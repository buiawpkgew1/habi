/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.RenderListener;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.CheckboxSetting;
import net.habiclient.settings.EnumSetting;
import net.habiclient.util.BlockUtils;
import net.habiclient.util.RenderUtils;
import net.habiclient.util.RotationUtils;
import net.habiclient.util.RotationUtils.Rotation;

@SearchTags({"build random", "RandomBuild", "random build", "PlaceRandom",
	"place random", "RandomPlace", "random place"})
public final class BuildRandomHack extends Hack
	implements UpdateListener, RenderListener
{
	private final EnumSetting<Mode> mode = new EnumSetting("模式", "§l快速§r 模式可以在其他方块后面放置方块.\n§l合法§r 模式能绕过大部分反作弊.", (Enum[])Mode.values(), (Enum)Mode.FAST);
    private final CheckboxSetting checkItem = new CheckboxSetting("检查手持物品", "只有在您拿着物品的时候才建造建筑.\n若把这个关闭,可能会导致火,水,岩浆,\n刷怪蛋,或所有可以右键的物品\n放在随机的位置.", true);
    private final CheckboxSetting fastPlace = new CheckboxSetting("总是 快速放置", "建造的时候 总是开启 快速放置模式,\n即使他是关闭的.", false);
	
	private final Random random = new Random();
	private BlockPos lastPos;
	
	public BuildRandomHack()
	{
		super("随机建造");
		setCategory(Category.BLOCKS);
		addSetting(mode);
		addSetting(checkItem);
		addSetting(fastPlace);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		lastPos = null;
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		lastPos = null;
		
		if(WURST.getHax().freecamHack.isEnabled())
			return;
		
		// check timer
		if(!fastPlace.isChecked() && IMC.getItemUseCooldown() > 0)
			return;
		
		if(!checkHeldItem())
			return;
		
		// set mode & range
		boolean legitMode = mode.getSelected() == Mode.LEGIT;
		int range = legitMode ? 5 : 6;
		int bound = range * 2 + 1;
		
		BlockPos pos;
		int attempts = 0;
		
		do
		{
			// generate random position
			pos = new BlockPos(MC.player.getPos()).add(
				random.nextInt(bound) - range, random.nextInt(bound) - range,
				random.nextInt(bound) - range);
			attempts++;
			
		}while(attempts < 128 && !tryToPlaceBlock(legitMode, pos));
	}
	
	private boolean checkHeldItem()
	{
		if(!checkItem.isChecked())
			return true;
		
		ItemStack stack = MC.player.getInventory().getMainHandStack();
		return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(lastPos == null)
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
		matrixStack.translate(lastPos.getX() - regionX, lastPos.getY(),
			lastPos.getZ() - regionZ);
		
		// get color
		float red = partialTicks * 2F;
		float green = 2 - red;
		
		// draw box
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(red, green, 0, 0.25F);
		RenderUtils.drawSolidBox(matrixStack);
		RenderSystem.setShaderColor(red, green, 0, 0.5F);
		RenderUtils.drawOutlinedBox(matrixStack);
		
		matrixStack.pop();
		
		// GL resets
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	private boolean tryToPlaceBlock(boolean legitMode, BlockPos pos)
	{
		if(!BlockUtils.getState(pos).getMaterial().isReplaceable())
			return false;
		
		if(legitMode)
		{
			if(!placeBlockLegit(pos))
				return false;
		}else
		{
			if(!placeBlockSimple_old(pos))
				return false;
			
			MC.player.swingHand(Hand.MAIN_HAND);
		}
		IMC.setItemUseCooldown(4);
		
		lastPos = pos;
		return true;
	}
	
	private boolean placeBlockLegit(BlockPos pos)
	{
		Vec3d eyesPos = RotationUtils.getEyesPos();
		Vec3d posVec = Vec3d.ofCenter(pos);
		double distanceSqPosVec = eyesPos.squaredDistanceTo(posVec);
		
		for(Direction side : Direction.values())
		{
			BlockPos neighbor = pos.offset(side);
			
			// check if neighbor can be right clicked
			if(!BlockUtils.canBeClicked(neighbor))
				continue;
			
			Vec3d dirVec = Vec3d.of(side.getVector());
			Vec3d hitVec = posVec.add(dirVec.multiply(0.5));
			
			// check if hitVec is within range (4.25 blocks)
			if(eyesPos.squaredDistanceTo(hitVec) > 18.0625)
				continue;
			
			// check if side is visible (facing away from player)
			if(distanceSqPosVec > eyesPos.squaredDistanceTo(posVec.add(dirVec)))
				continue;
			
			// check line of sight
			if(MC.world
				.raycast(new RaycastContext(eyesPos, hitVec,
					RaycastContext.ShapeType.COLLIDER,
					RaycastContext.FluidHandling.NONE, MC.player))
				.getType() != HitResult.Type.MISS)
				continue;
			
			// face block
			Rotation rotation = RotationUtils.getNeededRotations(hitVec);
			PlayerMoveC2SPacket.LookAndOnGround packet =
				new PlayerMoveC2SPacket.LookAndOnGround(rotation.getYaw(),
					rotation.getPitch(), MC.player.isOnGround());
			MC.player.networkHandler.sendPacket(packet);
			
			// place block
			IMC.getInteractionManager().rightClickBlock(neighbor,
				side.getOpposite(), hitVec);
			MC.player.swingHand(Hand.MAIN_HAND);
			IMC.setItemUseCooldown(4);
			
			return true;
		}
		
		return false;
	}
	
	private boolean placeBlockSimple_old(BlockPos pos)
	{
		Vec3d eyesPos = RotationUtils.getEyesPos();
		Vec3d posVec = Vec3d.ofCenter(pos);
		
		for(Direction side : Direction.values())
		{
			BlockPos neighbor = pos.offset(side);
			
			// check if neighbor can be right clicked
			if(!BlockUtils.canBeClicked(neighbor))
				continue;
			
			Vec3d hitVec = posVec.add(Vec3d.of(side.getVector()).multiply(0.5));
			
			// check if hitVec is within range (6 blocks)
			if(eyesPos.squaredDistanceTo(hitVec) > 36)
				continue;
			
			// place block
			IMC.getInteractionManager().rightClickBlock(neighbor,
				side.getOpposite(), hitVec);
			
			return true;
		}
		
		return false;
	}
	
	private enum Mode
	{
		FAST("快速"),
		
		LEGIT("合法");
		
		private final String name;
		
		private Mode(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
