/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.commands;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.habiclient.command.CmdException;
import net.habiclient.command.CmdSyntaxError;
import net.habiclient.command.Command;
import net.habiclient.events.RenderListener;
import net.habiclient.util.ChatUtils;

public final class InvseeCmd extends Command implements RenderListener
{
	private String targetName;
	
	public InvseeCmd()
	{
		super("invsee",
			"查看其他玩家的背包.",
			".invsee <目标玩家>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		if(MC.player.getAbilities().creativeMode)
		{
			ChatUtils.error("仅限生存模式.");
			return;
		}
		
		targetName = args[0];
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		boolean found = false;
		
		for(Entity entity : MC.world.getEntities())
		{
			if(!(entity instanceof OtherClientPlayerEntity))
				continue;
			
			OtherClientPlayerEntity player = (OtherClientPlayerEntity)entity;
			
			String otherPlayerName = player.getName().getString();
			if(!otherPlayerName.equalsIgnoreCase(targetName))
				continue;
			
			ChatUtils.message("正在展示 " + otherPlayerName + "的背包.");
			MC.setScreen(new InventoryScreen(player));
			found = true;
			break;
		}
		
		if(!found)
			ChatUtils.error("找不到该名玩家.");
		
		targetName = null;
		EVENTS.remove(RenderListener.class, this);
	}
}
