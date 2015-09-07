package com.alcist.anvilcraft.account.permissions;

import com.alcist.anvilcraft.account.models.User;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by istar on 04/09/15.
 */
public class Permissions {

    public boolean has(Player p, String s) {
        return p.hasPermission(s);
    }

    public boolean has(CommandSender sender, String s) {
        if (sender instanceof ConsoleCommandSender) {
            return true;

        }
        return has((Player) sender, s);
    }
}
