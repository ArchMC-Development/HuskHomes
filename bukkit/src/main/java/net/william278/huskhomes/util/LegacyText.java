/*
 * This file is part of HuskHomes, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.huskhomes.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Preva1l
 */
@UtilityClass
public final class LegacyText {
    private final Pattern FINAL_LEGACY_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-fA-F])");

    /**
     * Takes a string formatted in minimessage OR legacy and turns it into a legacy String.
     *
     * @param message the modernMessage
     * @return colorized component
     */
    public String message(@NotNull String message) {
        Matcher matcher = FINAL_LEGACY_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public List<String> list(@NotNull List<String> list) {
        List<String> legacyList = new ArrayList<>();
        for (String s : list) {
            legacyList.add(message(s));
        }
        return legacyList;
    }
}