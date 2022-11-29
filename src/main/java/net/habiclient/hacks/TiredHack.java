/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.habiclient.Category;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;

public final class TiredHack extends Hack implements UpdateListener
{
	public TiredHack()
	{
		super("疲惫");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		// disable incompatible derps
		WURST.getHax().derpHack.setEnabled(false);
		WURST.getHax().headRollHack.setEnabled(false);
		
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
		MC.player.networkHandler.sendPacket(
			new PlayerMoveC2SPacket.LookAndOnGround(MC.player.getYaw(),
				MC.player.age % 100, MC.player.isOnGround()));
	}
}
