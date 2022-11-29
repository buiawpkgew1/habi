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
import net.habiclient.hack.Hack;

@SearchTags({"no hurtcam", "no hurt cam"})
public final class NoHurtcamHack extends Hack
{
	public NoHurtcamHack()
	{
		super("反摇摆阳");
		setCategory(Category.RENDER);
	}
	
	// See GameRendererMixin.onBobViewWhenHurt()
}
