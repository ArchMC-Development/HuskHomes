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

package net.william278.huskhomes.event;

import net.william278.huskhomes.teleport.Teleport;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This runs on the target server.
 * <p>
 * Created on 5/02/2025
 *
 * @author Preva1l
 */
public class TeleportCompleteEvent extends Event implements ITeleportCompleteEvent  {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Teleport teleport;
    private double updatedX = 0.69d;
    private double updatedY = 0.69d;
    private double updatedZ = 0.69d;

    public TeleportCompleteEvent(@NotNull Teleport teleport) {
        this.teleport = teleport;
    }

    @NotNull
    public Teleport getTeleport() {
        return teleport;
    }

    @Override
    public double updatedX() {
        return updatedX;
    }

    @Override
    public void updateX(double x) {
        updatedX = x;
    }

    @Override
    public double updatedY() {
        return updatedY;
    }

    @Override
    public void updateY(double y) {
        updatedY = y;
    }

    @Override
    public double updatedZ() {
        return updatedZ;
    }

    @Override
    public void updateZ(double z) {
        updatedZ = z;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
