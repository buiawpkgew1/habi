/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks.chestesp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.util.math.Box;
import net.habiclient.settings.CheckboxSetting;
import net.habiclient.settings.ColorSetting;
import net.habiclient.settings.Setting;

public abstract class ChestEspGroup
{
	protected final ArrayList<Box> boxes = new ArrayList<>();
	private final ColorSetting color;
	private final CheckboxSetting enabled;
	
	public ChestEspGroup(ColorSetting color, CheckboxSetting enabled)
	{
		this.color = Objects.requireNonNull(color);
		this.enabled = enabled;
	}
	
	public void clear()
	{
		boxes.clear();
	}
	
	public boolean isEnabled()
	{
		return enabled == null || enabled.isChecked();
	}
	
	public Stream<Setting> getSettings()
	{
		return Stream.of(enabled, color).filter(Objects::nonNull);
	}
	
	public float[] getColorF()
	{
		return color.getColorF();
	}
	
	public List<Box> getBoxes()
	{
		return Collections.unmodifiableList(boxes);
	}
}
