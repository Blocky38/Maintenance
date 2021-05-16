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

package eu.kennytv.maintenance.velocity.util;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.connection.Player;
import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.util.ProxySenderInfo;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.UUID;

public final class VelocitySenderInfo implements ProxySenderInfo {
    private final CommandSource sender;

    public VelocitySenderInfo(final CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public UUID getUuid() {
        return sender instanceof Player ? ((Player) sender).id() : null;
    }

    @Override
    public String getName() {
        return sender instanceof Player ? ((Player) sender).username() : null;
    }

    @Override
    public boolean hasPermission(final String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public void sendMessage(final String message) {
        sender.sendMessage(fromLegacy(message));
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public void sendMessage(final TextComponent textComponent) {
        sender.sendMessage(textComponent);
    }

    @Override
    public boolean canAccess(final Server server) {
        return true;
    }

    @Override
    public void disconnect(final String message) {
        if (sender instanceof Player) {
            ((Player) sender).disconnect(fromLegacy(message));
        }
    }

    private TextComponent fromLegacy(final String s) {
        return LegacyComponentSerializer.legacySection().deserialize(s);
    }
}
