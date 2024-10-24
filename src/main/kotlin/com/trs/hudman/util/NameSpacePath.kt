package com.trs.hudman.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
class NameSpacePath(_fpath: String)
{
    val fpath: String = _fpath;
    val resourceLocation: ResourceLocation = ResourceLocation(fpath)
    val namespace: String = resourceLocation.namespace
    val path: String = resourceLocation.path

    override fun toString(): String
    {
        return resourceLocation.toString()
    }
}