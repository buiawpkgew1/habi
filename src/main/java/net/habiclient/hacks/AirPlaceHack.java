/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.RightClickListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.SliderSetting;
import net.habiclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"air place"})
public final class AirPlaceHack extends Hack implements RightClickListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	public AirPlaceHack()
	{
		super("空中放置");
		setCategory(Category.BLOCKS);
		addSetting(range);
	}
	
	@Override
	public void onEnable()
	{
		WURST.getHax().autoFishHack.setEnabled(false);
		
		EVENTS.add(RightClickListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(RightClickListener.class, this);
	}
	
	@Override
	public void onRightClick(RightClickEvent event)
	{
		HitResult hitResult = MC.player.raycast(range.getValue(), 0, false);
		if(!(hitResult instanceof BlockHitResult blockHitResult))
			return;
		
		IMC.getInteractionManager().rightClickBlock(
			blockHitResult.getBlockPos(), blockHitResult.getSide(),
			blockHitResult.getPos());
	}
}
