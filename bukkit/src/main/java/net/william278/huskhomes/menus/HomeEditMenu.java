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

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.InventoryScheme;
import fr.mrmicky.fastinv.ItemBuilder;
import net.william278.huskhomes.BukkitHuskHomes;
import net.william278.huskhomes.manager.HomesManager;
import net.william278.huskhomes.position.Home;
import net.william278.huskhomes.user.BukkitUser;
import net.william278.huskhomes.util.LegacyText;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeEditMenu extends FastInv {
    private final InventoryScheme scheme;
    private final HomesManager manager;
    private final BukkitUser user;
    private final Home home;

    public HomeEditMenu(BukkitUser user, Home home) {
        super(3 * 9, "Editing Home %s".formatted(home.getName()));
        this.user = user;
        this.home = home;
        this.manager = BukkitHuskHomes.getPlugin(BukkitHuskHomes.class).getManager().homes();
        this.scheme = new InventoryScheme()
                .masks(
                        "000000000",
                        "0N0D0L0P0",
                        "B00000000"
                );
        update();
    }

    private void update() {
        this.scheme
                .bindItem('N', new ItemBuilder(Material.NAME_TAG)
                                .name(LegacyText.message("&3Edit Home Name"))
                                .lore(LegacyText.message("&3Current: &f" + home.getName()))
                                .build(),
                        e -> new TextEditMenu(user.getPlayer(), "Editing Name",
                                newName -> {
                                    BukkitHuskHomes.getPlugin(BukkitHuskHomes.class).getLocales()
                                            .getLocale("edit_home_update_name", home.getName(), newName)
                                            .ifPresent(user::sendMessage);
                                    manager.setHomeName(home, newName);
                                    update();
                                    open(user.getPlayer());
                                }))
                .bindItem('D', new ItemBuilder(Material.OAK_SIGN)
                                .name(LegacyText.message("&3Edit Home Description"))
                                .lore(LegacyText.list(LegacyText.textWrap(home.getMeta().getDescription())))
                                .build(),
                        e -> new TextEditMenu(user.getPlayer(), "Editing Description",
                                newDescription -> {
                                    BukkitHuskHomes.getPlugin(BukkitHuskHomes.class).getLocales()
                                            .getLocale("edit_home_update_description", home.getMeta().getDescription(), newDescription)
                                            .ifPresent(user::sendMessage);
                                    manager.setHomeDescription(home, newDescription);
                                    update();
                                    open(user.getPlayer());
                                }))
                .bindItem('L', new ItemBuilder(Material.COMPASS)
                                .name(LegacyText.message("&3Change Home Location"))
                                .lore(LegacyText.list(List.of(
                                        "&3Server: &f" + home.getServer(),
                                        "&3World &f" + home.getWorld().getName(),
                                        "&3Location: &f%s&7, &f%s&7, &f%s".formatted((int) home.getX(), (int) home.getY(), (int) home.getZ())
                                )))
                                .build(),
                        e -> {
                            BukkitHuskHomes.getPlugin(BukkitHuskHomes.class).getLocales()
                                    .getLocale("edit_home_update_location", home.getName())
                                    .ifPresent(user::sendMessage);
                            manager.setHomePosition(home, user.getPosition());
                            update();
                            open(user.getPlayer());
                        })
                .bindItem('P', new ItemBuilder(Material.BEACON)
                                .name(LegacyText.message("&3Change Home Privacy"))
                                .lore(LegacyText.message("&3Current: &f" + (home.isPublic() ? "Public" : "Private")))
                                .build(),
                        e -> {
                            BukkitHuskHomes.getPlugin(BukkitHuskHomes.class).getLocales()
                                    .getLocale("edit_home_privacy_%s_success".formatted(home.isPublic() ? "public" : "private"), home.getOwner().getName(), home.getName())
                                    .ifPresent(user::sendMessage);
                            manager.setHomePrivacy(home, !home.isPublic());
                            update();
                            open(user.getPlayer());
                        })
                .bindItem('B', new ItemBuilder(Material.FEATHER)
                        .name(LegacyText.message("&3Go Back"))
                        .build(), e -> new HomesListMenu(user, home.isPublic()).open(user.getPlayer()));
    }

    @Override
    public void open(Player player) {
        scheme.apply(this);
        super.open(player);
    }
}
