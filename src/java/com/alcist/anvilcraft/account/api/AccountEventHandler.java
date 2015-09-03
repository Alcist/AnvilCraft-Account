package com.alcist.anvilcraft.account.api;
import org.bukkit.entity.Player;

/**
 * Created by istar on 04/08/14.
 */
public interface AccountEventHandler {

    /**
     * Add a listener on the current avatar the player is using.
     * @param player the player.
     */
    void addAvatarListener(Player player);
    void removeAvatarListener(Player player);
}
