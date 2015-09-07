package com.alcist.anvilcraft.account.listeners;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.AccountAdapter;
import com.alcist.anvilcraft.account.models.Avatar;
import com.alcist.anvilcraft.account.models.Location;
import com.alcist.anvilcraft.account.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by istar on 03/08/14.
 */
public class BukkitEventListener implements Listener {



    AccountAdapter dataHelper;
    FirebaseEventListener eventHandler;
    Plugin plugin;

    public BukkitEventListener(FirebaseEventListener eventHandler) {
        plugin = Plugin.getPlugin(Plugin.class);
        dataHelper = plugin.getAccountData();
        this.eventHandler = eventHandler;
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
                //TODO Check if the user has avatars and let it choose one.
                //TODO first time uses logs in create avatar.
                Avatar avatar = new Avatar();
                avatar.name = player.getDisplayName();
                user.currentAvatar = dataHelper.saveAvatar(avatar);
                if(user.avatars == null) {
                    user.avatars = new HashMap<>();
                }
                user.avatars.put(user.currentAvatar, true);
                dataHelper.saveUser(playerUUID, user);
            }
            eventHandler.addUserListener(playerUUID, nUser -> {

                eventHandler.addAvatarListener(playerUUID, avatar -> {
                    if (avatar.name != null) {
                        player.setDisplayName(avatar.name);
                        player.setPlayerListName(avatar.name);
                    }
                    if (avatar.location != null && plugin.getConfig().getBoolean("location.tpOnEnter")) {
                        player.teleport(avatar.location.getBukkitLocation());
                    }
                });
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
                dataHelper.getAvatar(user.currentAvatar, avatar -> {
                    if(plugin.getConfig().getBoolean("location.logOnExit")) {
                        avatar.location = new Location(player.getLocation());
                    }
                    dataHelper.saveAvatar(user.currentAvatar, avatar);
                })
        );

        eventHandler.removeAvatarListener(playerUUID);
    }

}
