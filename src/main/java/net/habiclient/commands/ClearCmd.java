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

public final class ClearCmd extends Command
{
	public ClearCmd()
	{
		super("clear", "清空聊天记录.", ".clear");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();
		
		MC.inGameHud.getChatHud().clear(true);
	}
}
