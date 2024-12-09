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
# The Grand Lua Experiment
I am proud to say I believe it's a success 

```lua
---@param delta number
---@param guiGraphics userdata
---@param gui userdata
function render(delta, guiGraphics, gui)
    s = this:getJsonElement():strings():size()
    guiGraphics:drawCenteredString(gui:getFont(), "Hello from Lua", 100, 200, 0xFFFFFF)
    guiGraphics:drawCenteredString(gui:getFont(), this:getJsonElement():strings():get(s-1), 100, 210, 0xFFFFFF)
end

function tick()
    return
end

function init()
    LOGGER:info("Hello from Lua")
end
```
This here is an example Lewis script to render hello from Lua into your Minecraft screen<br>
The as of today it works<br>
Using this example configuration

```json
{
    "ver": "v1.0",
    "elements": [
        {
            "elementId": "hudman:lua",
            "cords": {
                "x": 100,
                "y": 110
            },
            "width": 0,
            "height": 0,
            "scale": 0.75,
            "pairGameHudElement": "",
            "enable": true,
            "strings": [
                "test:tester"
            ]
        }
    ],
    "debug": false,
    "errorNotification": true
}
```

<br>

There are some limitations For now<br>
There's not easy access to widgets that plans be remedied<br>
No access to Lua Standard Library this was done on purpose 

### Where to even put your scripts
Navigate to 'Game folder'/config/hudman/scripts
And then add the Lua code above as 'test@tester.lua'     
This file name will be converted into a namespace path To be used by the Lua element 