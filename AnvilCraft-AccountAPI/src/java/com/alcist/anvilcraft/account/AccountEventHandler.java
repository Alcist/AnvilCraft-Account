package com.alcist.anvilcraft.account;
import com.alcist.anvilcraft.account.models.Avatar;
import com.alcist.firehelper.Callback;

/**
 * Created by istar on 04/08/14.
 */
public interface AccountEventHandler {

    /**
     * Add a listener on the current avatar the player is using.
     */
    void addAvatarListener(String userUUID, Callback<Avatar> callback);
    void removeAvatarListener(String userUUID);
    void removeAllListeners();
}
