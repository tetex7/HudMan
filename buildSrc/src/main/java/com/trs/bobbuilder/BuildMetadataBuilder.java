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
import org.apache.commons.lang3.SystemUtils;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static com.trs.bobbuilder.ReleaseUtils.prettyPrintWithIndent;

public class BuildMetadataBuilder
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
            "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard",
            "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree",
            "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot",
            "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok",
            "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran'F", "Nidorina",
            "Nidoqueen", "Nidoran'M", "Nidorino", "Nidoking", "Clefairy", "Clefable",
            "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat",
            "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat",
            "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck",
            "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag",
            "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop",
            "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool",
            "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash",
            "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo",
            "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder",
            "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee",
            "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute"
    };

    private static String getRandomName(final long seed)
    {
        Random random = new Random(seed);
        String prefix = RELEASE_PREFIXES[random.nextInt(RELEASE_PREFIXES.length-1)];
        String suffix = RELEASE_SUFFIXES[random.nextInt(RELEASE_SUFFIXES.length-1)];
        return (prefix + '-' + suffix).toLowerCase();
    }

    private static byte[] mkVendorBytes()
    {
        return ByteBuffer.allocate(Long.BYTES).order(ByteOrder.LITTLE_ENDIAN).putLong(mkVendorId()).array();
    }

    /**
     * Create a unique mark Based off of the user's username and host name on their machine when they are building
     * @return A married hash of the username and host name
     */
    private static long mkVendorId()
    {
        long un = Math.abs(SystemUtils.getUserName().hashCode());
        long rn = Math.abs(SystemUtils.getHostName().hashCode());
        return (un + rn) + (rn/2);
    }

    private final char sep = File.separatorChar;

    private final File META_INF_PATH;
    private final File BUILD_STAMP_JSON_PATH;
    private final File VENDOR_STAMP_PATH;
    private final File AUX_STAMP_PATH;

    private final ProcessResources processResources;

    public BuildMetadataBuilder(ProcessResources processResources)
    {
        this.processResources = processResources;
        META_INF_PATH = new File(processResources.getDestinationDir().getPath() + sep + "META-INF");
        BUILD_STAMP_JSON_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".BuildStamp.json");
        VENDOR_STAMP_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".VendorStamp.bin");
        AUX_STAMP_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".AuxStamp.bin");
    }

    private BuildStamp mkBuildData()
    {
        final String ver = (String)processResources.getProject().getVersion();
        final String date = String.valueOf(LocalDate.now().getMonthValue()) + '/' + LocalDate.now().getDayOfMonth() + '/' + LocalDate.now().getYear();
        final String time = String.valueOf(LocalTime.now().getHour()) + ':' + LocalTime.now().getHour() + ':' + LocalTime.now().getSecond();
        final int fbid = Math.abs((ver + date + time).hashCode());
        long vid = mkVendorId();

        return new BuildStamp(
                ver,
                date,
                time,
                getRandomName(vid),
                UUID.randomUUID(),
                fbid,
                vid
        );
    }

    private void mkAuxStamp() throws IOException
    {
        if (!AUX_STAMP_PATH.exists())
        {
            AUX_STAMP_PATH.createNewFile();
            int stapHash = 0;

            try (FileInputStream s = new FileInputStream(BUILD_STAMP_JSON_PATH))
            {
                stapHash = Arrays.hashCode(s.readAllBytes());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            try (FileOutputStream s = new FileOutputStream(AUX_STAMP_PATH))
            {
                byte[] auxBuff = ByteBuffer.allocate(Integer.BYTES)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putInt(stapHash)
                        .array();
                s.write(auxBuff);
                s.write("TRS".getBytes(StandardCharsets.US_ASCII));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private void mkVendorStamp() throws IOException
    {
        if (!VENDOR_STAMP_PATH.exists())
        {
            VENDOR_STAMP_PATH.createNewFile();

            try (FileOutputStream s = new FileOutputStream(VENDOR_STAMP_PATH))
            {
                s.write("VID".getBytes(StandardCharsets.US_ASCII));
                s.write(mkVendorBytes());
                s.write("TRS".getBytes(StandardCharsets.US_ASCII));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private void mkJsonMark() throws IOException
    {
        if (!BUILD_STAMP_JSON_PATH.exists())
        {
            BUILD_STAMP_JSON_PATH.createNewFile();

            try (FileWriter s = new FileWriter(BUILD_STAMP_JSON_PATH))
            {
                //s.write(Qlang.builder().AddTagLibrary(instructions).build().parse(BUILD_STAMP_JSON).outputString());
                s.write(prettyPrintWithIndent(new Gson().toJson(mkBuildData()), 4));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void build()
    {
        if (!META_INF_PATH.exists())
        {
            META_INF_PATH.mkdir();
        }
        try
        {
            mkJsonMark();
            mkVendorStamp();
            mkAuxStamp();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
