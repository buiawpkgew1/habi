/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.habiclient.Category;
import net.habiclient.SearchTags;
import net.habiclient.clickgui.Window;
import net.habiclient.clickgui.components.TpsComponent;
import net.habiclient.events.PacketInputListener;
import net.habiclient.hack.Hack;
import net.habiclient.settings.SliderSetting;

import java.util.LinkedList;

@SearchTags({"tps"})
public final class TpsDisplayHack extends Hack implements PacketInputListener
{
	private final Window window;
	private float serverTps = 0.0f;

	public static LinkedList<Long[]> serverTicks = new LinkedList<>();

	private final SliderSetting dataPoints = new SliderSetting("Data Points",
			"Number of Data Points.", 2, 2, 20, 1, SliderSetting.ValueDisplay.INTEGER);

	public TpsDisplayHack()
	{
		super("TPS-Display");

		setCategory(Category.RENDER);
		addSetting(dataPoints);

		window = new Window("TPS Estimate");
		window.setPinned(true);
		window.setInvisible(true);
		window.add(new TpsComponent(this));
	}

	@Override
	public void onEnable()
	{
		serverTps = 0.0f;
		serverTicks.clear();
		EVENTS.add(PacketInputListener.class, this);
		window.setInvisible(false);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(PacketInputListener.class, this);
		window.setInvisible(true);
	}

	public float getServerTps() {
		return serverTps;
	}

	public int getLatencyMs() {
		try {
			return MC.player.networkHandler.getPlayerListEntry(MC.player.getUuid()).getLatency();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public long getLastServerTickMs() {
		if (serverTicks.size() <= 0) {
			return 0L;
		}
		long r = System.currentTimeMillis() - serverTicks.getLast()[0];
		return Math.max(r, 0L);
	}

	@Override
	public void onReceivedPacket(PacketInputEvent event) {
		Packet<?> p = event.getPacket();
		if (p instanceof WorldTimeUpdateS2CPacket packet) {
			long now = System.currentTimeMillis();
			long nowTick = packet.getTime();
			serverTicks.add(new Long[]{now, nowTick});
			while (serverTicks.size() > dataPoints.getValueI()) {
				serverTicks.removeFirst();
			}
			if (serverTicks.size() >= 2) {
				Long[] first = serverTicks.getFirst();
				serverTps = (float)(nowTick - first[1]) / (float)(now - first[0]) * 1000.0f;
			}
		}
	}

	public Window getWindow()
	{
		return window;
	}

}
