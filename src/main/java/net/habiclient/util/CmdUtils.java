/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.util;

import java.util.stream.Stream;

import net.habiclient.Feature;
import net.habiclient.WurstClient;
import net.habiclient.command.CmdError;
import net.habiclient.settings.Setting;

public enum CmdUtils
{
	;
	
	public static Feature findFeature(String name) throws CmdError
	{
		Stream<Feature> stream =
			WurstClient.INSTANCE.getNavigator().getList().stream();
		stream = stream.filter(f -> name.equalsIgnoreCase(f.getName()));
		Feature feature = stream.findFirst().orElse(null);
		
		if(feature == null)
			throw new CmdError(
				"一个功能名为 \"" + name + "\" 无法被找到.");
		
		return feature;
	}
	
	public static Setting findSetting(Feature feature, String name)
		throws CmdError
	{
		name = name.replace("_", " ").toLowerCase();
		Setting setting = feature.getSettings().get(name);
		
		if(setting == null)
			throw new CmdError("一个设置名为 \"" + name + "\" 无法被找到在" + feature.getName() + ".");
		
		return setting;
	}
}
