package com.alcist.anvilcraft.account.api;
import com.alcist.anvilcraft.account.models.User;
import org.bukkit.entity.Player;

/**
 * Created by istar on 04/08/14.
 */
public interface AccountEventHandler {

    /**
     * Add a listener on the current avatar the player is using.
     */
    void addAvatarListener(String userUUID, Callback<User.Avatar> callback);
    void removeAvatarListener(String userUUID);
    void removeAllListeners();
}
