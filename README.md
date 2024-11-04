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