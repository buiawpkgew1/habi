/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.client.option.KeyBinding;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.mixinterface.IKeyBinding;

@SearchTags({"auto walk"})
public final class AutoWalkHack extends Hack implements UpdateListener
{
	public AutoWalkHack()
	{
		super("自动走");
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
		
		KeyBinding forwardKey = MC.options.forwardKey;
		forwardKey.setPressed(((IKeyBinding)forwardKey).isActallyPressed());
	}
	
	@Override
	public void onUpdate()
	{
		MC.options.forwardKey.setPressed(true);
	}
}
