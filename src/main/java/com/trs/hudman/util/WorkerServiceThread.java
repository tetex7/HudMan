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

package com.trs.hudman.util;


import java.util.Stack;
import org.slf4j.Logger;

@Deprecated(forRemoval = true)
public final class WorkerServiceThread extends Thread
{
    interface Service
    {
        boolean work(WorkService this_service, Logger logger);
    }

    public record WorkService(
            String serviceName,
            Service service
    ){}

    private final Logger logger;


    private final Stack<WorkService> workStack = new Stack<>();


    public void post(WorkService work)
    {
        workStack.push(work);
    }

    WorkerServiceThread(String modid, Logger logger)
    {
        super( modid + "-WorkerServiceThread");
        this.logger = logger;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (!workStack.empty())
            {
                WorkService wservice = workStack.pop();
                wservice.service.work(wservice, logger);
            }
        }
    }
}
