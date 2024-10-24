package com.trs.hudman.util;

import com.trs.hudman.confg.JsonConfgHudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface INamespaceHandler
{
    boolean work(JsonConfgHudElement element);
}
