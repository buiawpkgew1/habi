/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.client.option.KeyBinding;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.mixinterface.IKeyBinding;
import net.habiclient.settings.SliderSetting;

@SearchTags({"miley cyrus", "twerk", "wrecking ball"})
public final class MileyCyrusHack extends Hack implements UpdateListener
{
	private final SliderSetting twerkSpeed = new SliderSetting("蹲起速度", "我看起来就像疯了一样....", 5.0, 1.0, 10.0, 1.0, SliderSetting.ValueDisplay.INTEGER);
	
	private int timer;
	
	public MileyCyrusHack()
	{
		super("不停潜行");
		setCategory(Category.FUN);
		addSetting(twerkSpeed);
	}
	
	@Override
	public void onEnable()
	{
		timer = 0;
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		
		KeyBinding sneak = MC.options.sneakKey;
		sneak.setPressed(((IKeyBinding)sneak).isActallyPressed());
	}
	
	@Override
	public void onUpdate()
	{
		timer++;
		if(timer < 10 - twerkSpeed.getValueI())
			return;
		
		MC.options.sneakKey.setPressed(!MC.options.sneakKey.isPressed());
		timer = -1;
	}
}
