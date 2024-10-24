package com.trs.hudman.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface HudResetEvent
{
    Event<HudResetEvent> EVENT = EventFactory.createArrayBacked(HudResetEvent.class,
            (listeners) -> () -> {
                for (HudResetEvent listener : listeners) {
                    boolean result = listener.interact();

                    if(!result) {
                        return false;
                    }
                }

                return true;
            });

    public static void call()
    {
        HudResetEvent.EVENT.invoker().interact();
    }

    boolean interact();
}
