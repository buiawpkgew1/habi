/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;

@SearchTags({"jet pack", "AirJump", "air jump"})
public final class JetpackHack extends Hack implements UpdateListener
{
	public JetpackHack()
	{
		super("喷气背包");
		
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		WURST.getHax().creativeFlightHack.setEnabled(false);
		WURST.getHax().flightHack.setEnabled(false);
		
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(MC.options.jumpKey.isPressed())
			MC.player.jump();
	}
}
