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
import net.habiclient.hacks.BlinkHack;

public final class BlinkCmd extends Command
{
	public BlinkCmd()
	{
		super("blink", "开启, 关闭 或 取消 Blink.", ".blink [on|off]",
			".blink cancel");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 1)
			throw new CmdSyntaxError();
		
		BlinkHack blinkHack = WURST.getHax().blinkHack;
		
		if(args.length == 0)
		{
			blinkHack.setEnabled(!blinkHack.isEnabled());
			return;
		}
		
		switch(args[0].toLowerCase())
		{
			default:
			throw new CmdSyntaxError();
			
			case "on":
			blinkHack.setEnabled(true);
			break;
			
			case "off":
			blinkHack.setEnabled(false);
			break;
			
			case "cancel":
			cancel(blinkHack);
			break;
		}
	}
	
	private void cancel(BlinkHack blinkHack) throws CmdException
	{
		if(!blinkHack.isEnabled())
			throw new CmdError("无法取消，Blink 已关闭!");
		
		blinkHack.cancel();
	}
}
