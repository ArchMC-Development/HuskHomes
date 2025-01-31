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

package net.william278.huskhomes.features;

import gg.scala.cloudsync.shared.discovery.CloudSyncDiscoveryService;
import gg.scala.commons.annotations.plugin.SoftDependency;
import gg.scala.flavor.service.Configure;
import gg.scala.flavor.service.Service;
import gg.scala.flavor.service.ignore.IgnoreAutoScan;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Service
@IgnoreAutoScan
@SoftDependency("cloudsync")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CloudSyncFeature {
    public static final CloudSyncFeature INSTANCE =  new CloudSyncFeature();

    @Configure
    public void configure() {
        CloudSyncDiscoveryService.INSTANCE.getDiscoverable().getAssets()
                .add("net.william278:bukkit:HuskHomes-paper");
    }
}
