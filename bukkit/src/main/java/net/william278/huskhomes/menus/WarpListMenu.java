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

import fr.mrmicky.fastinv.InventoryScheme;
import fr.mrmicky.fastinv.ItemBuilder;
import fr.mrmicky.fastinv.PaginatedFastInv;
import net.william278.huskhomes.BukkitHuskHomes;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.position.Warp;
import net.william278.huskhomes.teleport.Teleport;
import net.william278.huskhomes.user.BukkitUser;
import net.william278.huskhomes.util.LegacyText;
import net.william278.huskhomes.util.TransactionResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WarpListMenu extends PaginatedFastInv {
    private static final List<Material> signs = List.of(
            Material.OAK_SIGN,
            Material.ACACIA_SIGN,
            Material.DARK_OAK_SIGN,
            Material.BIRCH_SIGN,
            Material.CRIMSON_SIGN,
            Material.SPRUCE_SIGN,
            Material.JUNGLE_SIGN,
            Material.WARPED_SIGN
    );
    private final InventoryScheme scheme;

    public WarpListMenu(BukkitUser user) {
        super(4 * 9, "Warps");

        this.scheme = new InventoryScheme()
                .masks(
                        "XXXXXXXXX",
                        "XXXXXXXXX",
                        "XXXXXXXXX",
                        "000P0N000"
                )
                .bindPagination('X')
                .bindItem('N', new ItemBuilder(Material.ARROW)
                        .name(LegacyText.message("&3Next Page"))
                        .lore(LegacyText.message("&7Current Page &f%s&7/&f%s".formatted(currentPage(), lastPage() == 0 ? 1 : lastPage())))
                        .build(), e -> openNext())
                .bindItem('P', new ItemBuilder(Material.ARROW)
                        .name(LegacyText.message("&3Previous Page"))
                        .lore(LegacyText.message("&7Current Page &f%s&7/&f%s".formatted(currentPage(), lastPage() == 0 ? 1 : lastPage())))
                        .build(), e -> openPrevious());

        HuskHomesAPI.getInstance().getWarps().thenAccept(immutableWarps -> {
            List<Warp> warps = immutableWarps.stream().filter(warp -> user.getPlayer().hasPermission(warp.getPermission())).toList();
            warps.forEach(warp -> {
                List<String> description = warp.getMeta().getDescription().isEmpty() ? List.of("&7No Description") : LegacyText.textWrap(warp.getMeta().getDescription());
                List<String> format = List.of(
                        "%description%",
                        "",
                        "&7Click to teleport");

                addContent(new ItemBuilder(randomSign())
                        .name(LegacyText.message(warp.getName()))
                        .lore(LegacyText.list(createLore(format, description)))
                        .build(), e -> {
                    e.getWhoClicked().closeInventory();
                    Teleport.builder(BukkitHuskHomes.getPlugin(BukkitHuskHomes.class))
                            .type(Teleport.Type.TELEPORT)
                            .actions(TransactionResolver.Action.WARP_TELEPORT)
                            .target(Position.at(warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch(), warp.getWorld(), warp.getServer()))
                            .teleporter(user)
                            .toTimedTeleport().execute();
                });
            });
        }).thenRun(() -> openPage(currentPage()));
    }

    private List<String> createLore(List<String> format, List<String> description) {
        List<String> lore = new ArrayList<>();
        int i = 0;
        for (String line : format) {
            if (line.contains("%description%")) {
                for (String descriptionLine : description) {
                    if (i == 0) {
                        descriptionLine = "&f\u24D8 &7" + descriptionLine;
                    }
                    lore.add(i, descriptionLine);
                    i++;
                }
                continue;
            }
            lore.add(i, line);
            i++;
        }
        return lore;
    }

    @Override
    public void open(Player player) {
        scheme.apply(this);
        super.open(player);
    }

    private Material randomSign() {
        return signs.get(ThreadLocalRandom.current().nextInt(signs.size()));
    }
}