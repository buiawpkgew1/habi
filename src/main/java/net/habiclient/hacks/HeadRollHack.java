/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;

@SearchTags({"head roll", "nodding", "yes"})
public final class HeadRollHack extends Hack implements UpdateListener
{
	public HeadRollHack()
	{
		super("疯狂点头");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		// disable incompatible derps
		WURST.getHax().derpHack.setEnabled(false);
		WURST.getHax().tiredHack.setEnabled(false);
		
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
		float timer = MC.player.age % 20 / 10F;
		float pitch = MathHelper.sin(timer * (float)Math.PI) * 90F;
		
		MC.player.networkHandler.sendPacket(
			new PlayerMoveC2SPacket.LookAndOnGround(MC.player.getYaw(), pitch,
				MC.player.isOnGround()));
	}
}
