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

import com.google.common.base.CharMatcher;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import gg.scala.commons.agnostic.sync.server.ServerContainer;
import gg.scala.commons.agnostic.sync.server.impl.GameServer;
import gg.scala.commons.agnostic.sync.server.state.ServerState;
import net.william278.huskhomes.BukkitHuskHomes;
import net.william278.huskhomes.HuskHomes;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.teleport.Teleport;
import net.william278.huskhomes.user.OnlineUser;
import net.william278.huskhomes.util.LegacyText;
import net.william278.huskhomes.util.TransactionResolver;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SpawnMenu extends FastInv {
    public SpawnMenu(OnlineUser user, HuskHomes plugin) {
        super(InventoryType.HOPPER, "Go To Spawn");
        List<GameServer> servers = ServerContainer.INSTANCE.getServersInGroup("survival-spawn")
                .stream()
                .map(GameServer.class::cast)
                .filter(server -> server.getState() == ServerState.Loaded)
                .filter(server -> server.getWhitelisted() != null && !server.getWhitelisted())
                .sorted(Comparator.comparingInt(s -> Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(s.getId()))))
                .toList();

        AtomicInteger i = new AtomicInteger();
        servers.forEach(server -> {
            if (i.getAndIncrement() == 4) return;
            List<String> format = List.of(
                    "&3Players: &f" + server.getPlayersCount(),
                    "",
                    "&7Click to teleport");

            addItem(new ItemBuilder(Material.COMPASS)
                    .name(LegacyText.message("&f" + server.getId()))
                    .lore(LegacyText.list(format))
                    .build(), e -> {
                Optional<Position> spawnLocation = plugin.getSpawn();
                if (spawnLocation.isEmpty()) {
                    plugin.getLocales().getLocale("").ifPresent(user::sendMessage);
                    return;
                }
                Position spawn = spawnLocation.get();
                spawn.setServer(server.getId());

                e.getWhoClicked().closeInventory();
                Teleport.builder(BukkitHuskHomes.getPlugin(BukkitHuskHomes.class))
                        .type(Teleport.Type.TELEPORT)
                        .actions(TransactionResolver.Action.SPAWN_TELEPORT)
                        .target(spawn)
                        .teleporter(user)
                        .toTimedTeleport().execute();
            });
        });
    }
}