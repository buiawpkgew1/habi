/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.clickgui.Window;
import net.habiclient.clickgui.components.RadarComponent;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.CheckboxSetting;
import net.habiclient.settings.SliderSetting;
import net.habiclient.settings.SliderSetting.ValueDisplay;
import net.habiclient.settings.filterlists.EntityFilterList;
import net.habiclient.settings.filters.FilterAnimalsSetting;
import net.habiclient.settings.filters.FilterInvisibleSetting;
import net.habiclient.settings.filters.FilterMonstersSetting;
import net.habiclient.settings.filters.FilterPlayersSetting;
import net.habiclient.settings.filters.FilterSleepingSetting;
import net.habiclient.util.FakePlayerEntity;

@SearchTags({"MiniMap", "mini map"})
public final class RadarHack extends Hack implements UpdateListener
{
	private final Window window;
	private final ArrayList<Entity> entities = new ArrayList<>();
	
	private final SliderSetting radius = new SliderSetting("半径",
		"半径方块.", 100, 1, 100, 1, ValueDisplay.INTEGER);
	private final CheckboxSetting rotate =
		new CheckboxSetting("跟随玩家", true);
	
	private final EntityFilterList entityFilters = new EntityFilterList(
		new FilterPlayersSetting("不显示其他玩家.", false),
		new FilterSleepingSetting("不显示正在睡觉的.", false),
		new FilterMonstersSetting("不显示僵尸,苦力怕,等.", false),
		new FilterAnimalsSetting("不显示猪,牛,等.", false),
		new FilterInvisibleSetting("不显示隐身的实体.", false));
	
	public RadarHack()
	{
		super("雷达");
		
		setCategory(Category.RENDER);
		addSetting(radius);
		addSetting(rotate);
		entityFilters.forEach(this::addSetting);
		
		window = new Window("雷达");
		window.setPinned(true);
		window.setInvisible(true);
		window.add(new RadarComponent(this));
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		window.setInvisible(false);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		window.setInvisible(true);
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayerEntity player = MC.player;
		ClientWorld world = MC.world;
		
		entities.clear();
		Stream<Entity> stream =
			StreamSupport.stream(world.getEntities().spliterator(), true)
				.filter(e -> !e.isRemoved() && e != player)
				.filter(e -> !(e instanceof FakePlayerEntity))
				.filter(e -> e instanceof LivingEntity)
				.filter(e -> ((LivingEntity)e).getHealth() > 0);
		
		stream = entityFilters.applyTo(stream);
		
		entities.addAll(stream.collect(Collectors.toList()));
	}
	
	public Window getWindow()
	{
		return window;
	}
	
	public Iterable<Entity> getEntities()
	{
		return Collections.unmodifiableList(entities);
	}
	
	public double getRadius()
	{
		return radius.getValue();
	}
	
	public boolean isRotateEnabled()
	{
		return rotate.isChecked();
	}
}
