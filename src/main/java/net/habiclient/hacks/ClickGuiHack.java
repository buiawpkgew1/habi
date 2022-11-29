/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import java.awt.Color;

import net.habiclient.DontBlock;
import net.habiclient.SearchTags;
import net.habiclient.clickgui.screens.ClickGuiScreen;
import net.habiclient.hack.DontSaveState;
import net.habiclient.hack.Hack;
import net.habiclient.settings.ColorSetting;
import net.habiclient.settings.SliderSetting;
import net.habiclient.settings.SliderSetting.ValueDisplay;

@DontSaveState
@DontBlock
@SearchTags({"click gui", "WindowGUI", "window gui", "HackMenu", "hack menu"})
public final class ClickGuiHack extends Hack
{
	private final ColorSetting bgColor =
		new ColorSetting("背景", "背景颜色", new Color(0x404040));
	
	private final ColorSetting acColor =
		new ColorSetting("边框", "边框颜色", new Color(0x101010));
	
	private final ColorSetting txtColor =
		new ColorSetting("文字", "文字颜色", new Color(0xF0F0F0));
	
	private final SliderSetting opacity = new SliderSetting("不透明性", 0.5,
		0.15, 0.85, 0.01, ValueDisplay.PERCENTAGE);
	
	private final SliderSetting ttOpacity = new SliderSetting("工具提示的不透明度",
		0.75, 0.15, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	public ClickGuiHack()
	{
		super("ClickGUI");
		addSetting(bgColor);
		addSetting(acColor);
		addSetting(txtColor);
		addSetting(opacity);
		addSetting(ttOpacity);
	}
	
	@Override
	public void onEnable()
	{
		MC.setScreen(new ClickGuiScreen(WURST.getGui()));
		setEnabled(false);
	}
	
	public float[] getBackgroundColor()
	{
		return bgColor.getColorF();
	}
	
	public float[] getAccentColor()
	{
		return acColor.getColorF();
	}
	
	public int getTextColor()
	{
		return txtColor.getColorI();
	}
	
	public float getOpacity()
	{
		return opacity.getValueF();
	}
	
	public float getTooltipOpacity()
	{
		return ttOpacity.getValueF();
	}
}
