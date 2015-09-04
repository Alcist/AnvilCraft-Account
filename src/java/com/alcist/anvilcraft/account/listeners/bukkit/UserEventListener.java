package com.alcist.anvilcraft.account.listeners.bukkit;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.api.IAccountData;
import com.alcist.anvilcraft.account.models.User;
import com.alcist.anvilcraft.account.api.AccountEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

/**
 * Created by istar on 03/08/14.
 */
public class UserEventListener implements Listener {



    IAccountData dataHelper;
    AccountEventHandler eventHandler;

    public UserEventListener() {
        Plugin plugin = Plugin.getPlugin(Plugin.class);
        dataHelper = plugin.getAccountData();
        eventHandler = plugin.getEventHandler();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEvent(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String playerUUID = event.getPlayer().getUniqueId().toString();

        dataHelper.getUser(playerUUID, user -> {

            if (user == null) {
                user = new User();
                user.minecraftName = player.getName();
            }
            if (user.currentAvatar == null) {
                //TODO Customize creation of avatar.
                User.Avatar avatar = new User.Avatar();
                avatar.name = player.getDisplayName();
                user.currentAvatar = dataHelper.saveAvatar(playerUUID, avatar);
            }
            eventHandler.addAvatarListener(playerUUID, avatar -> {
                if (avatar.name != null) {
                    player.setDisplayName(avatar.name);
                    player.setPlayerListName(avatar.name);
                }
                if (avatar.location != null) {
                    player.teleport(avatar.location.getBukkitLocation());
                }
            });

            user.lastLogin = new Date();
            dataHelper.saveUser(playerUUID, user);
        });
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final String playerUUID = player.getUniqueId().toString();
        dataHelper.getUser(playerUUID, user ->
                dataHelper.getAvatar(playerUUID, user.currentAvatar, avatar -> {
                    avatar.location = new User.Avatar.Location(player.getLocation());
                    dataHelper.saveAvatar(playerUUID, user.currentAvatar, avatar);
                })
        );

        eventHandler.removeAvatarListener(playerUUID);
        dataHelper.clearData(playerUUID);
    }


}
