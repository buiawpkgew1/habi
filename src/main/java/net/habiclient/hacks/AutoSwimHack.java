/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.client.network.ClientPlayerEntity;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;

@SearchTags({"auto swim"})
public final class AutoSwimHack extends Hack implements UpdateListener
{
	public AutoSwimHack()
	{
		super("自动游泳");
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
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
		ClientPlayerEntity player = MC.player;
		
		if(player.horizontalCollision || player.isSneaking())
			return;
		
		if(!player.isTouchingWater())
			return;
		
		if(player.forwardSpeed > 0)
			player.setSprinting(true);
	}
}
