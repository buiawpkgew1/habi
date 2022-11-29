/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.other_features;

import net.habiclient.DontBlock;
import net.habiclient.SearchTags;
import net.habiclient.other_feature.OtherFeature;

@SearchTags({"Clean Up"})
@DontBlock
public final class CleanUpOtf extends OtherFeature
{
	public CleanUpOtf()
	{
		super("清除","清理您的服务器列表.\n要使用它,请按服务器选择屏幕上的“清理”按钮.");
	}
}
