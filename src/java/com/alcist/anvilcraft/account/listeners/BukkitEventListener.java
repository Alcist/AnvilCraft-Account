package com.alcist.anvilcraft.account.listeners;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.api.AccountAdapter;
import com.alcist.anvilcraft.account.api.models.Avatar;
import com.alcist.anvilcraft.account.api.models.Location;
import com.alcist.anvilcraft.account.api.models.User;
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
public class BukkitEventListener implements Listener {



    AccountAdapter dataHelper;
    AccountEventHandler eventHandler;
    Plugin plugin;

    public BukkitEventListener() {
        plugin = Plugin.getPlugin(Plugin.class);
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
                Avatar avatar = new Avatar();
                avatar.name = player.getDisplayName();
                user.currentAvatar = dataHelper.saveAvatar(playerUUID, avatar);
            }
            eventHandler.addAvatarListener(playerUUID, avatar -> {
                if (avatar.name != null) {
                    player.setDisplayName(avatar.name);
                    player.setPlayerListName(avatar.name);
                }
                if (avatar.location != null && plugin.getConfig().getBoolean("location.tpOnEnter")) {
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
                    if(plugin.getConfig().getBoolean("location.logOnExit")) {
                        avatar.location = new Location(player.getLocation());
                    }
                    dataHelper.saveAvatar(playerUUID, user.currentAvatar, avatar);
                })
        );

        eventHandler.removeAvatarListener(playerUUID);
        dataHelper.clearData(playerUUID);
    }


}
