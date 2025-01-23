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

package net.william278.huskhomes.menus;

import net.wesjd.anvilgui.AnvilGUI;
import net.william278.huskhomes.BukkitHuskHomes;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.function.Consumer;

public class TextEditMenu {
    public TextEditMenu(Player player, String placeholder, Consumer<String> callback) {
        new AnvilGUI.Builder().plugin(BukkitHuskHomes.getPlugin(BukkitHuskHomes.class))
                .title(placeholder)
                .text(placeholder)
                .onClick((slot, state) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                    String search = state.getText();
                    return Collections.singletonList(AnvilGUI.ResponseAction.run(
                            () -> callback.accept((search != null && search.contains(placeholder)) ? null : search))
                    );
                })
                .onClose((state) -> {
                    String text = state.getText();
                    callback.accept((text != null && text.contains(placeholder)) ? null : text);
                }).open(player);
    }
}
