/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient;

public enum Category
{
	BLOCKS("方块"),
	MOVEMENT("移动"),
	COMBAT("PVP"),
	RENDER("渲染"),
	CHAT("聊天"),
	FUN("乐趣"),
	ITEMS("物品"),
	OTHER("其他");
	
	private final String name;
	
	private Category(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
