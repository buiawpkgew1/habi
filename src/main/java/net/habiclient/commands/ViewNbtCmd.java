/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.habiclient.SearchTags;
import net.habiclient.command.CmdError;
import net.habiclient.command.CmdException;
import net.habiclient.command.CmdSyntaxError;
import net.habiclient.command.Command;
import net.habiclient.util.ChatUtils;

@SearchTags({"view nbt", "NBTViewer", "nbt viewer"})
public final class ViewNbtCmd extends Command
{
	public ViewNbtCmd()
	{
		super("viewnbt", "显示一个物品的NBT数据", ".viewnbt","复制到剪贴板: .viewnbt copy");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		ClientPlayerEntity player = MC.player;
		ItemStack stack = player.getInventory().getMainHandStack();
		if(stack.isEmpty())
			throw new CmdError("你必须把一个物品放在主手");
		
		NbtCompound tag = stack.getNbt();
		String nbt = tag == null ? "" : tag.asString();
		
		switch(String.join(" ", args).toLowerCase())
		{
			case "":
			ChatUtils.message("NBT: " + nbt);
			break;
			
			case "copy":
			MC.keyboard.setClipboard(nbt);
			ChatUtils.message("NBT数据复制成功");
			break;
			
			default:
			throw new CmdSyntaxError();
		}
	}
}
