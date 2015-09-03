package com.alcist.anvilcraft.account.listeners.bukkit;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.api.AccountDataHelper;
import com.alcist.anvilcraft.account.api.Callback;
import com.alcist.anvilcraft.account.listeners.firebase.AvatarFireListener;
import com.alcist.anvilcraft.account.models.User;
import com.alcist.anvilcraft.account.api.AccountEventHandler;
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
public class UserEventListener implements Listener {



    AccountDataHelper dataHelper;
    AccountEventHandler eventHandler;

    HashMap<String, AvatarFireListener> playerAvatarListenerMap;

    public UserEventListener() {
        Plugin plugin = Plugin.getPlugin(Plugin.class);
        dataHelper = plugin.getDataHelper();
        eventHandler = plugin.getEventHandler();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEvent(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String playerUUID = event.getPlayer().getUniqueId().toString();

        dataHelper.getUser(playerUUID, new Callback<User>() {
            @Override
            public void onCallBack(User user) {
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

                user.lastLogin = new Date();
                dataHelper.saveUser(playerUUID, user);
                eventHandler.addAvatarListener(player);
            }
        });
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final String playerUUID = player.getUniqueId().toString();
        dataHelper.getUser(playerUUID, new Callback<User>() {
            @Override
            public void onCallBack(final User user) {
                dataHelper.getAvatar(playerUUID, user.currentAvatar, new Callback<User.Avatar>() {
                    @Override
                    public void onCallBack(User.Avatar avatar) {
                        avatar.location = new User.Avatar.Location(player.getLocation());
                        dataHelper.saveAvatar(playerUUID, user.currentAvatar, avatar);
                    }
                });
            }
        });

        eventHandler.removeAvatarListener(player);
        dataHelper.clearData(playerUUID);

    }


}
