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

package com.trs.hudman.util.services;

import java.util.Stack;

public final class ServiceManager extends Thread
{
    private final Thread[] serviceArray = new Thread[3];
    private final Stack<AbstractService> serviceStack = new Stack<>();

    ServiceManager(String name)
    {
        super("ServiceManager-" + name);
        serviceArray[0] = null;
        serviceArray[1] = null;
        serviceArray[2] = null;
    }

    public synchronized void stopService()
    {

    }

    @Override
    public void run()
    {
    }
}
