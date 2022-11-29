/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.events;

import java.util.ArrayList;

import net.habiclient.event.Event;
import net.habiclient.event.Listener;
import net.habiclient.mixinterface.IClientPlayerEntity;

public interface PlayerMoveListener extends Listener
{
	public void onPlayerMove(IClientPlayerEntity player);
	
	public static class PlayerMoveEvent extends Event<PlayerMoveListener>
	{
		private final IClientPlayerEntity player;
		
		public PlayerMoveEvent(IClientPlayerEntity player)
		{
			this.player = player;
		}
		
		@Override
		public void fire(ArrayList<PlayerMoveListener> listeners)
		{
			for(PlayerMoveListener listener : listeners)
				listener.onPlayerMove(player);
		}
		
		@Override
		public Class<PlayerMoveListener> getListenerType()
		{
			return PlayerMoveListener.class;
		}
	}
}
