/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.commands;

import net.habiclient.command.CmdException;
import net.habiclient.command.CmdSyntaxError;
import net.habiclient.command.Command;

public final class LeaveCmd extends Command
{
	public LeaveCmd()
	{
		super("leave", "退出服务器.", ".leave");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 1 && args[0].equalsIgnoreCase("taco"))
			for(int i = 0; i < 128; i++)
				MC.player.sendChatMessage("Taco!", null);
		else if(args.length != 0)
			throw new CmdSyntaxError();
		
		MC.world.disconnect();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Leave";
	}
	
	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("leave");
	}
}
