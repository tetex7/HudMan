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

package com.trs.qlang;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Qlang
{

    public static final class Builder
    {
        private final Vector<ImmutablePair<Pattern, QlangInstruction>> ints = new Vector<>();
        private boolean hasSTD = false;

        @SafeVarargs
        public final Builder AddAllInstructions(ImmutablePair<Pattern, QlangInstruction>... intss)
        {
            ints.addAll(List.of(intss));
            return this;
        }

        public Builder AddAllInstructions(List<ImmutablePair<Pattern, QlangInstruction>> itss)
        {
            ints.addAll(itss);
            return this;
        }

        public Builder AddTagLibrary(List<ImmutablePair<Pattern, QlangInstruction>> itss)
        {
            return AddAllInstructions(itss);
        }

        public Builder includeStandardLibrary()
        {
            hasSTD = true;
            return this;
        }

        public Builder AddInstruction(ImmutablePair<Pattern, QlangInstruction> inst)
        {
            ints.add(inst);
            return this;
        }

        public Builder AddInstruction(Pattern pattern, QlangInstruction inst)
        {
            ints.add(ImmutablePair.of(pattern, inst));
            return this;
        }

        public Builder AddInstruction(String tag, QlangInstruction inst)
        {
            ints.add(ImmutablePair.of(Pattern.compile(tag), inst));
            return this;
        }

        public Qlang build()
        {
            return new Qlang(this);
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }

    private final Stack<ImmutablePair<Pattern, QlangInstruction>> instructionStack = new Stack<>();

    public record WorkOutput(
       String outputString,
       boolean didWork
    ){}


    public WorkOutput parse(final String text)
    {
        WorkOutput workOuput = null;
        String work_string = text;
        boolean dw = false;
        for (ImmutablePair<Pattern, QlangInstruction> work_pair : instructionStack)
        {
            final Matcher cm = work_pair.getKey().matcher(work_string);
            final String pw = work_pair.getRight().run(work_pair.left.pattern(), work_pair.left, null);
            if (pw != null)
            {
                work_string = cm.replaceAll(pw);
            }
        }
        if (work_string.equals(text))
        {
            return new WorkOutput(
                    text,
                    false
            );
        }
        return new WorkOutput(
                work_string,
                true
        );
    }

    private static void addStandardLibrary(final Qlang interpreter)
    {
        interpreter.instructionStack.addAll(
                List.of(
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_HOUR), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalTime.now().getHour()))),
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_MIUNTE), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalTime.now().getMinute()))),
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_SECOND), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalTime.now().getSecond()))),
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_MOUTH), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalDate.now().getMonth().getValue()))),
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_DAY), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalDate.now().getDayOfMonth()))),
                        ImmutablePair.of(Pattern.compile(QlangStandardTags.H_YEAR), QlangInstruction.of((String tag, Pattern pattern) -> Integer.toString(LocalDate.now().getYear())))
                )
        );
    }


    public WorkOutput invoke(String text)
    {
        return parse(text);
    }

    Qlang(@NotNull Builder builder)
    {
        this.instructionStack.addAll(builder.ints);
        if (builder.hasSTD)
        {
            addStandardLibrary(this);
        }
    }
}
