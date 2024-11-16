<?--
Copyright (C) 2024  Tete

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see "https://www.gnu.org/licenses/"
--?>
# Hud Manager
a client side minecraft mod I make in my free time for mc1.20.1    
the main feature of the mod is To allow the user to create custom HUD using a config file written in Json.  
This mod quite small, only having one dependency and that is the Fabric API and technically Minecraft itself.
This mod is also expandable by external plug-in mods using the Element registry system.

> [!WARNING]
> Anything written in Kotlin in this mod might not be; soon™<br>
> And yes that it does mean `HudState`


here is a test config:
```json
{
    "ver":"v1.0",
    "elements": [
        {
            "elementId": "hudman:velocity_vector",
            "cords": {"x": 100, "y": 110},
            "width": 0,
            "height": 0,
            "scale": 0.75,
            "pairGameHudElement": "minecraft:hotbar",
            "enable": true,
            "strings": [
                "doTooltip"
            ]
        }
    ],
    "debug": false
}   
```


<hr>
<br>
<br>
Also here's some syntax on how to make your own element written of Java of course   
I do not recommend writing this in Kotlin

```java
import com.trs.hudman.confg.JsonConfgHudElement;
import com.trs.hudman.gui.hudmods.AbstractHudElement;
import com.trs.hudman.gui.hudmods.widget.FlowMeterWidget;
import com.trs.hudman.util.Vec2i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//Minecraft import's

public class Tester extends AbstractHudElement
{
    private final FlowMeterWidget TEST_FLOW_METER = new FlowMeterWidget(getCords().x(), getCords().x(), 0.5f);
    private int v = -25;
    public Tester(@Nullable AbstractHudElement root, @NotNull Minecraft client, @NotNull Vec2i rCords, @NotNull JsonConfgHudElement jsonElement)
    {
        super(root, client, rCords, jsonElement);
        TEST_FLOW_METER.addTickHandler((widget) -> {
            FlowMeterWidget this_widget = (FlowMeterWidget)widget;

            this_widget.setValue(this.v);
            this.v = v >= 25 ? -25 : v+1;
        });
    }

    @Override
    public void render(float partialTick, GuiGraphics guiGraphics, Gui gui)
    {
        TEST_FLOW_METER.render(guiGraphics, var1);
    }

    @Override
    public void tick()
    {
        TEST_FLOW_METER.widget_tick();
    }
}
```
<hr>
<br>

And then the Register So it can be used in game
``` java
class ModClient implements ClientModInitializer
{
    public final static MODID = "test";
    void onInitializeClient()
    {
         HudState.getElementRegistry().register(NamespacePath.of(MODID, "test_text"), Tester::new);
    }
}
```
