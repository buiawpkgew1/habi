/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.BlockBreakingProgressListener;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.mixinterface.IClientPlayerInteractionManager;
import net.habiclient.settings.CheckboxSetting;

@SearchTags({"FastMine", "SpeedMine", "SpeedyGonzales", "fast break",
	"fast mine", "speed mine", "speedy gonzales", "NoBreakDelay",
	"no break delay"})
public final class FastBreakHack extends Hack
	implements UpdateListener, BlockBreakingProgressListener
{
	private final CheckboxSetting legitMode = new CheckboxSetting("低速模式",
		"只删除破坏方块时的延迟，而不加速破坏过程",
		false);
	
	public FastBreakHack()
	{
		super("FastBreak");
		setCategory(Category.BLOCKS);
		addSetting(legitMode);
	}
	
	@Override
	public String getRenderName()
	{
		if(legitMode.isChecked())
			return getName() + "Legit";
		return getName();
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(BlockBreakingProgressListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(BlockBreakingProgressListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		IMC.getInteractionManager().setBlockHitDelay(0);
	}
	
	@Override
	public void onBlockBreakingProgress(BlockBreakingProgressEvent event)
	{
		if(legitMode.isChecked())
			return;
		
		IClientPlayerInteractionManager im = IMC.getInteractionManager();
		
		if(im.getCurrentBreakingProgress() >= 1)
			return;
		
		Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
		BlockPos blockPos = event.getBlockPos();
		Direction direction = event.getDirection();
		im.sendPlayerActionC2SPacket(action, blockPos, direction);
	}
}
