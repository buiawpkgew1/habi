/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.commands;

import java.util.Comparator;
import java.util.stream.StreamSupport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.habiclient.command.CmdError;
import net.habiclient.command.CmdException;
import net.habiclient.command.CmdSyntaxError;
import net.habiclient.command.Command;
import net.habiclient.hacks.FollowHack;
import net.habiclient.util.FakePlayerEntity;

public final class FollowCmd extends Command
{
	public FollowCmd()
	{
		super("follow", "跟随一个给定的实体.", ".follow <实体名>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		FollowHack followHack = WURST.getHax().followHack;
		
		if(followHack.isEnabled())
			followHack.setEnabled(false);
		
		Entity entity = StreamSupport
			.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> e instanceof LivingEntity)
			.filter(e -> !e.isRemoved() && ((LivingEntity)e).getHealth() > 0)
			.filter(e -> e != MC.player)
			.filter(e -> !(e instanceof FakePlayerEntity))
			.filter(e -> args[0].equalsIgnoreCase(e.getName().getString()))
			.min(
				Comparator.comparingDouble(e -> MC.player.squaredDistanceTo(e)))
			.orElse(null);
		
		if(entity == null)
			throw new CmdError(
				"实体 \"" + args[0] + "\" 找不到.");
		
		followHack.setEntity(entity);
		followHack.setEnabled(true);
	}
}
