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
import net.habiclient.events.RightClickListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.SliderSetting;
import net.habiclient.settings.SliderSetting.ValueDisplay;

public final class ThrowHack extends Hack implements RightClickListener
{
	private final SliderSetting amount = new SliderSetting("数量",
		"Amount of uses per click.", 16, 2, 1000000, 1, ValueDisplay.INTEGER);
	
	public ThrowHack()
	{
		super("重复");
		
		setCategory(Category.OTHER);
		addSetting(amount);
	}
	
	@Override
	public String getRenderName()
	{
		return getName() + " [" + amount.getValueString() + "]";
	}
	
	@Override
	public void onEnable()
	{
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
		if(IMC.getItemUseCooldown() > 0)
			return;
		
		if(!MC.options.useKey.isPressed())
			return;
		
		for(int i = 0; i < amount.getValueI(); i++)
		{
			if(MC.crosshairTarget.getType() == HitResult.Type.BLOCK)
			{
				BlockHitResult hitResult = (BlockHitResult)MC.crosshairTarget;
				IMC.getInteractionManager().rightClickBlock(
					hitResult.getBlockPos(), hitResult.getSide(),
					hitResult.getPos());
			}
			
			IMC.getInteractionManager().rightClickItem();
		}
	}
}
