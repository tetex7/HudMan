/*
 * Copyright (C) 2024  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.trs.hudman.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface HudResetEvent
{
    Event<HudResetEvent> EVENT = EventFactory.createArrayBacked(HudResetEvent.class,
            (listeners) -> (Minecraft client) -> {
                for (HudResetEvent listener : listeners) {
                    boolean result = listener.interact(client);

                    if(!result) {
                        return false;
                    }
                }

                return true;
            });

    static void call(Minecraft client)
    {
        HudResetEvent.EVENT.invoker().interact(null);
    }

    boolean interact(Minecraft client);
}
