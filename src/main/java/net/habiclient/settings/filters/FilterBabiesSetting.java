/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;

public final class FilterBabiesSetting extends EntityFilterCheckbox
{
	public FilterBabiesSetting(String description, boolean checked)
	{
		super("排除婴儿", description, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof PassiveEntity && ((PassiveEntity)e).isBaby());
	}
	
	public static FilterBabiesSetting genericCombat(boolean checked)
	{
		return new FilterBabiesSetting(
			"不会攻击小猪仔,小村民, 诸如此类.", checked);
	}
}
