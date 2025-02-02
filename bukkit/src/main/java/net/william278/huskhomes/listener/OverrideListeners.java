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

package net.william278.huskhomes.listener;

import net.william278.huskhomes.event.HomeListEvent;
import net.william278.huskhomes.event.WarpListEvent;
import net.william278.huskhomes.menus.HomesListMenu;
import net.william278.huskhomes.menus.WarpListMenu;
import net.william278.huskhomes.user.BukkitUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class OverrideListeners implements Listener {
    @EventHandler
    public void onHomeListView(@NotNull HomeListEvent event) {
        if (!(event.getListViewer() instanceof BukkitUser onlineUser)) return;
        event.setCancelled(true);
        new HomesListMenu(onlineUser, event.getIsPublicHomeList()).open(onlineUser.getPlayer());
    }

    @EventHandler
    public void onWarpListView(@NotNull WarpListEvent event) {
        if (!(event.getListViewer() instanceof BukkitUser onlineUser)) return;
        event.setCancelled(true);
        new WarpListMenu(onlineUser).open(onlineUser.getPlayer());
    }
}