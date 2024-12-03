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

package com.trs.bobbuilder;

import com.trs.qlang.Qlang;
import com.trs.qlang.QlangInstruction;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

import groovy.text.SimpleTemplateEngine;

public class ReleaseUtils
{
    private static final String[] RELEASE_PREFIXES = {
            "Slow", "Quick", "Bright", "Dark", "Fast", "Blue", "Star", "Sun", "Wind",
            "Thunder", "Soft", "Hard", "High", "Low", "Night", "Day", "Cloud",
            "Fire", "Frost", "Sweet", "Earth", "Sky", "Golden", "Silver", "Shadow",
            "Swift", "Bold", "Iron", "Steel", "Mystic", "Electric", "Storm", "Rain",
            "Snow", "Crimson", "Emerald", "Ruby", "Amber", "Lunar", "Solar",
            "Cosmic", "Ocean", "Wave", "Echo", "Silent", "Ancient", "Frozen",
            "Burning", "Clever", "Gentle", "Wild", "Free", "Majestic", "Brightest",
            "Darkest", "Fierce", "Shining", "Blazing", "Soaring", "Falling",
            "Glowing", "Hidden", "Distant", "Endless", "Brave", "Calm", "Goldenrod",
            "Velvet", "Starlit", "Stormy", "Dew", "Drift", "Feather", "Gale",
            "Ironclad", "Pebble", "Radiant", "Shimmer", "Silentwood", "Spire",
            "Stoneheart", "Verdant", "Zephyr", "Hollow", "Whispering", "Boldest",
            "Ethereal", "Frosty", "Horizon", "Infinity", "Jagged", "Kindred",
            "Lucid", "Meadow", "Noble", "Primal", "Quiet", "Restless", "Serene",
            "Tenacious", "Umbra", "Valiant", "Wistful", "Zenith"
    };

    private static final String[] RELEASE_SUFFIXES = {
            "rabbit", "bird", "fox", "wolf", "cat", "tree", "stone", "flower", "river",
            "heart", "wing", "leaf", "song", "light", "shadow", "runner", "hunter",
            "dream", "whisper", "keeper", "flame", "claw", "paw", "fang", "mist",
            "thorn", "blossom", "breeze", "storm", "wave", "crystal", "guardian",
            "spark", "seer", "weaver", "dancer", "watcher", "seeker", "speaker",
            "herald", "builder", "wanderer", "explorer", "defender", "champion",
            "singer", "glow", "beam", "path", "star", "orb", "flare", "trail",
            "veil", "mark", "howl", "roar", "songbird", "shade", "spear", "quill",
            "shard", "echo", "ember", "flint", "ridge", "forge", "grove", "crown",
            "hearth", "wild", "spirit", "bringer", "sentinel", "skyline", "keeper",
            "caster", "chaser", "stalker", "protector", "dreamer", "tracker",
            "soarer", "guardian", "glider", "screecher", "wisp", "flitter",
            "sprint", "sparkle", "relic", "strider", "vision", "whirl", "zephyr",
            "arbor", "pillar", "cascade", "spire", "whisperer"
    };

    private static final String BUILD_STAMP_JSON = """
    {
        "version": "%version%",
        "builtDate": "%date%",
        "builtTime": "%time%",
        "BuildName": "%BuildName%",
        "builtID": %bid%,
        "VendorID": %buid%
    }
    """;

    public static String getRandomName(final long seed) {
        Random random = new Random(seed);
        String prefix = RELEASE_PREFIXES[random.nextInt(RELEASE_PREFIXES.length-1)];
        String suffix = RELEASE_SUFFIXES[random.nextInt(RELEASE_SUFFIXES.length-1)];
        return prefix + '-' + suffix;
    }

    public static long mkVendorId()
    {
        long un = System.getProperty("user.name").hashCode();
        long rn = "In the beginning God created the heavens and the Earth".hashCode();
        long ret = (un + rn);
        return (ret < 0) ? -ret : ret;
    }

    public static void mkJsonMark(ProcessResources processResources) throws IOException
    {
        final var sep = File.separatorChar;
        final var META_INF_PATH = new File(processResources.getDestinationDir().getPath() + sep + "META-INF");
        final var BUILD_STAMP_JSON_PATH = new File(META_INF_PATH.getPath() + sep + "buildStamp.json");

        final String ver = (String)processResources.getProject().getVersion();
        final String date = String.valueOf(LocalDate.now().getMonthValue()) + '/' + LocalDate.now().getDayOfMonth() + '/' + LocalDate.now().getYear();
        final String time = String.valueOf(LocalTime.now().getHour()) + ':' + LocalTime.now().getHour() + ':' + LocalTime.now().getSecond();
        final int rbid = (ver + date + time).hashCode();
        final int fbid = (rbid < 0) ? -rbid : rbid;
        long vid = mkVendorId();

        final List<ImmutablePair<Pattern, QlangInstruction>> instructions = List.of(
                ImmutablePair.of(Pattern.compile("%version%"), QlangInstruction.of((a, b) -> ver)),
                ImmutablePair.of(Pattern.compile("%date%"), QlangInstruction.of((a, b) -> date)),
                ImmutablePair.of(Pattern.compile("%time%"), QlangInstruction.of((a, b) -> time)),
                ImmutablePair.of(Pattern.compile("%BuildName%"), QlangInstruction.of((a, b) -> getRandomName(vid))),
                ImmutablePair.of(Pattern.compile("%bid%"), QlangInstruction.of((a, b) -> String.valueOf(fbid))),
                ImmutablePair.of(Pattern.compile("%buid%"), QlangInstruction.of((a, b) -> String.valueOf(vid)))
        );
        if (!META_INF_PATH.exists())
        {
            META_INF_PATH.mkdir();
        }

        if (!BUILD_STAMP_JSON_PATH.exists())
        {
            BUILD_STAMP_JSON_PATH.createNewFile();

            try (FileWriter s = new FileWriter(BUILD_STAMP_JSON_PATH))
            {
                s.write(Qlang.builder().AddTagLibrary(instructions).build().parse(BUILD_STAMP_JSON).outputString());
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}