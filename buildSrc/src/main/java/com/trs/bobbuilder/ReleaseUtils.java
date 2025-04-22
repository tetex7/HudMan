/*
 * Copyright (C) 2025  Tetex7
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

package com.trs.bobbuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.gradle.api.Project;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.Random;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.apache.commons.lang3.SystemUtils;

import org.jetbrains.annotations.NotNull;

public final class ReleaseUtils
{
    public static void mkJsonMark(@NotNull ProcessResources processResources) throws IOException
    {
        new BuildMetadataBuilder(processResources).build();
    }

    public static String prettyPrintWithIndent(String json, int indentSize)
    {
        JsonElement jsonElement = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Step 1: Convert JSON to a pretty-printed version with default indentation
        String prettyJson = gson.toJson(jsonElement);

        // Step 2: Define the custom indentation string
        String indent = " ".repeat(indentSize);

        // Step 3: Use StringBuilder to construct the final string with custom indentation
        StringBuilder indentedJson = new StringBuilder();
        int currentIndentLevel = 0;

        // Split the JSON into lines and apply custom indentation per line
        for (String line : prettyJson.split("\n"))
        {
            String trimmedLine = line.trim();

            // Decrease indent level for closing braces/brackets
            if (trimmedLine.startsWith("}") || trimmedLine.startsWith("]"))
            {
                currentIndentLevel--;
            }

            // Apply current indentation and add line
            indentedJson.append(indent.repeat(currentIndentLevel)).append(trimmedLine).append("\n");

            // Increase indent level after opening braces/brackets
            if (trimmedLine.endsWith("{") || trimmedLine.endsWith("["))
            {
                currentIndentLevel++;
            }
        }

        return indentedJson.toString().trim();
    }
}
