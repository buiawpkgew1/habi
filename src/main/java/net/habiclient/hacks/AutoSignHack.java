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
import net.habiclient.hack.DontSaveState;
import net.habiclient.hack.Hack;

@SearchTags({"auto sign"})
@DontSaveState
public final class AutoSignHack extends Hack
{
	private String[] signText;
	
	public AutoSignHack()
	{
		super("§d自动木牌");
		setCategory(Category.BLOCKS);
	}
	
	@Override
	public void onDisable()
	{
		signText = null;
	}
	
	public String[] getSignText()
	{
		return signText;
	}
	
	public void setSignText(String[] signText)
	{
		if(isEnabled() && this.signText == null)
			this.signText = signText;
	}
}
