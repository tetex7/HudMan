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

public final class ClientWorldEvent
{
    public final static Event<ClientWorldUnloadEvent> CLIENT_WORLD_UNLOAD_EVENT = EventFactory.createArrayBacked(ClientWorldUnloadEvent.class,
            (listeners) -> () -> {
                for (ClientWorldUnloadEvent listener : listeners) {
                    listener.interact();
                }
            });

    public final static Event<ClientWorldLoadEvent> CLIENT_WORLD_LOAD_EVENT = EventFactory.createArrayBacked(ClientWorldLoadEvent.class,
            (listeners) -> () -> {
                for (ClientWorldLoadEvent listener : listeners) {
                    listener.interact();
                }
            });

    /*static void call()
    {
        HudResetEvent.EVENT.invoker().interact();
    }*/

    @FunctionalInterface
    public interface ClientWorldLoadEvent
    {
        void interact();
        static void call()
        {
            ClientWorldEvent.CLIENT_WORLD_UNLOAD_EVENT.invoker().interact();
        }
    }

    @FunctionalInterface
    public interface ClientWorldUnloadEvent
    {
        void interact();
        static void call()
        {
            ClientWorldEvent.CLIENT_WORLD_LOAD_EVENT.invoker().interact();
        }
    }
}
