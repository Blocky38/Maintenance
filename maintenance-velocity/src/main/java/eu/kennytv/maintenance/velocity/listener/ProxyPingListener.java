/*
 * Maintenance - https://git.io/maintenancemode
 * Copyright (C) 2018-2021 KennyTV (https://github.com/KennyTV)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.kennytv.maintenance.velocity.listener;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.velocity.MaintenanceVelocityPlugin;

import java.util.UUID;

public final class ProxyPingListener implements EventHandler<ProxyPingEvent> {
    private final MaintenanceVelocityPlugin plugin;
    private final SettingsProxy settings;
    private final UUID uuid = new UUID(0, 0);

    public ProxyPingListener(final MaintenanceVelocityPlugin plugin, final SettingsProxy settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public EventTask execute(final ProxyPingEvent event) {
        if (!settings.isMaintenance() || !settings.isEnablePingMessages()) return null;

        final ServerPing ping = event.ping();
        final ServerPing.Builder builder = ping.asBuilder();
        if (settings.hasCustomPlayerCountMessage()) {
            builder.version(new ServerPing.Version(1, settings.getPlayerCountMessage()
                    .replace("%ONLINE%", Integer.toString(builder.getMaximumPlayers()))
                    .replace("%MAX%", Integer.toString(builder.getOnlinePlayers()))));
        }

        final String[] split = settings.getPlayerCountHoverMessage().split("\n");
        final ServerPing.SamplePlayer[] samplePlayers = new ServerPing.SamplePlayer[split.length];
        for (int i = 0; i < split.length; i++) {
            samplePlayers[i] = new ServerPing.SamplePlayer(split[i], uuid);
        }

        builder.description(plugin.translate(settings.getRandomPingMessage()))
                .samplePlayers(samplePlayers);

        if (settings.hasCustomIcon() && plugin.getFavicon() != null) {
            builder.favicon(plugin.getFavicon());
        }

        event.setPing(builder.build());
        return null;
    }
}
