/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.hacks.newchunks;

import java.util.Set;

import net.minecraft.client.render.BufferBuilder.BuiltBuffer;
import net.minecraft.util.math.ChunkPos;

public interface NewChunksChunkRenderer
{
	public BuiltBuffer buildBuffer(Set<ChunkPos> chunks, int drawDistance);
}
