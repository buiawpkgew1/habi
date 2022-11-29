/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.habiclient.Category;
import net.habiclient.hack.DontSaveState;
import net.habiclient.hack.Hack;
import net.habiclient.mixinterface.IGameRenderer;

@DontSaveState
public final class LsdHack extends Hack
{
	public LsdHack()
	{
		super("LSD");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		if(!(MC.getCameraEntity() instanceof PlayerEntity))
		{
			setEnabled(false);
			return;
		}
		
		if(MC.gameRenderer.getShader() != null)
			MC.gameRenderer.disableShader();
		
		((IGameRenderer)MC.gameRenderer)
			.loadWurstShader(new Identifier("shaders/post/wobble.json"));
	}
	
	@Override
	public void onDisable()
	{
		if(MC.gameRenderer.getShader() != null)
			MC.gameRenderer.disableShader();
	}
}
