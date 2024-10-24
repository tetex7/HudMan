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
