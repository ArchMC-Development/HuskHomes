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
import net.william278.huskhomes.position.Home;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.teleport.Teleport;
import net.william278.huskhomes.user.OnlineUser;
import net.william278.huskhomes.util.LegacyText;
import net.william278.huskhomes.util.TransactionResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Preva1l
 */
public class HomesListMenu extends PaginatedFastInv {
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
    private static final Pattern wrapPattern = Pattern.compile("([\\x00-\\xFF]{1,2}|.?){27}");
    private final InventoryScheme scheme;

    public HomesListMenu(OnlineUser user, boolean isPublic) {
        super(6 * 9, "Your Homes");

        this.scheme = new InventoryScheme()
                .bindPagination('X')
                .bindItem('N', new ItemBuilder(Material.ARROW)
                        .name(LegacyText.message("&3Next Page"))
                        .build(), e -> openNext())
                .bindItem('P', new ItemBuilder(Material.ARROW)
                        .name(LegacyText.message("&3Previous Page"))
                        .build(), e -> openPrevious());

        HuskHomesAPI.getInstance().getUserHomes(user).thenAccept(immutableHomes -> {
            List<Home> homes = new ArrayList<>(immutableHomes);
            homes.removeIf(home -> home.isPublic() == !isPublic);
            for (Home home : homes) {
                String description = home.getMeta().getDescription().isEmpty() ? "No Description" : textWrap(home.getMeta().getDescription());
                addContent(new ItemBuilder(randomSign())
                        .name(LegacyText.message(home.getName()))
                        .lore(LegacyText.list(List.of(
                                "&f\u24D8 " + description,
                                "&3Server: &f" + home.getServer(),
                                "&3World &f" + home.getWorld().getName(),
                                "&3Location: &f%s&7, &f%s&7, &f%s".formatted((int) home.getX(), (int) home.getY(), (int) home.getZ())
                        )))
                        .build(), e -> {
                    e.getWhoClicked().closeInventory();
                    Teleport.builder(BukkitHuskHomes.getPlugin(BukkitHuskHomes.class))
                            .type(Teleport.Type.TELEPORT)
                            .actions(TransactionResolver.Action.HOME_TELEPORT)
                            .target(Position.at(home.getX(), home.getY(), home.getZ(), home.getWorld(), home.getServer()))
                            .teleporter(user)
                            .toTimedTeleport().execute();
                });
            }
        });
    }

    @Override
    public void open(Player player) {
        scheme.apply(this);
        super.open(player);
    }

    private Material randomSign() {
        return signs.get(ThreadLocalRandom.current().nextInt(signs.size()));
    }

    private String textWrap(@NotNull String string) {
        Matcher matcher = wrapPattern.matcher(string);
        StringBuilder out = new StringBuilder();

        while (matcher.find()) {
            if (!matcher.group().trim().isEmpty()) {
                out.append("&7%s\n".formatted(matcher.group().trim()));
            }
        }
        return String.valueOf(out);
    }
}