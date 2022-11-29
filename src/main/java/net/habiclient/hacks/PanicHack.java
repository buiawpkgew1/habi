/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.habiclient.Category;
import net.habiclient.DontBlock;
import net.habiclient.SearchTags;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;

@SearchTags({"legit", "disable"})
@DontBlock
public final class PanicHack extends Hack implements UpdateListener
{
	public PanicHack()
	{
		super("§4[危]");
		setCategory(Category.OTHER);
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
	}
	
	@Override
	public void onUpdate()
	{
		for(Hack hack : WURST.getHax().getAllHax())
			if(hack.isEnabled() && hack != this)
				hack.setEnabled(false);
			
		setEnabled(false);
	}
}
