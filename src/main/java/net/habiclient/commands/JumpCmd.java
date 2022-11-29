/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.commands;

import net.habiclient.command.CmdError;
import net.habiclient.command.CmdException;
import net.habiclient.command.CmdSyntaxError;
import net.habiclient.command.Command;

public final class JumpCmd extends Command
{
	public JumpCmd()
	{
		super("jump", "使你跳一下.");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();
		
		if(!MC.player.isOnGround() && !WURST.getHax().jetpackHack.isEnabled())
			throw new CmdError("不能在空中跳跃.");
		
		MC.player.jump();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "跳";
	}
	
	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("jump");
	}
}
