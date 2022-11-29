/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.ChatOutputListener;
import net.habiclient.hack.Hack;

@SearchTags({"fancy chat"})
public final class FancyChatHack extends Hack implements ChatOutputListener
{
	private final String blacklist = "(){}[]|";
	
	public FancyChatHack()
	{
		super("Unicode消息");
		setCategory(Category.CHAT);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(ChatOutputListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(ChatOutputListener.class, this);
	}
	
	@Override
	public void onSentMessage(ChatOutputEvent event)
	{
		String message = event.getOriginalMessage();
		if(message.startsWith("/") || message.startsWith("."))
			return;
		
		String newMessage = convertString(message);
		event.setMessage(newMessage);
	}
	
	private String convertString(String input)
	{
		String output = "";
		for(char c : input.toCharArray())
			output += convertChar(c);
		
		return output;
	}
	
	private String convertChar(char c)
	{
		if(c < 0x21 || c > 0x80)
			return "" + c;
		
		if(blacklist.contains(Character.toString(c)))
			return "" + c;
		
		return new String(Character.toChars(c + 0xfee0));
	}
}
