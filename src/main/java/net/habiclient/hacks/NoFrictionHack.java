package net.habiclient.hacks;

import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.hack.Hack;
import net.habiclient.settings.SliderSetting;
import net.habiclient.settings.SliderSetting.ValueDisplay;

@SearchTags({"friction", "no friction", "slippery", "slipperiness"})
public final class NoFrictionHack extends Hack
{
	public final SliderSetting friction =
                new SliderSetting("摩擦/滑溜", 0.989, 0.8, 1.1, 0.001, ValueDisplay.DECIMAL);
	public NoFrictionHack()
	{
		super("无摩擦");
		setCategory(Category.MOVEMENT);
		addSetting(friction);
	}
}
