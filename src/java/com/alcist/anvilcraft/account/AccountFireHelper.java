package com.alcist.anvilcraft.account;

/**
 * Created by istar on 03/09/15.
 */

import com.alcist.anvilcraft.account.api.IAccountData;
import com.alcist.firehelper.Callback;
import com.alcist.firehelper.BukkitFireListener;
import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.*;

import java.util.HashMap;

/**
 * Created by istar on 04/08/14.
 */
class AccountFireHelper implements IAccountData {

    // Firebase client reference
    final Firebase firebase;

    // Base references to Firebase
    final Firebase usersRef;
    final Firebase avatarsRef;
    final BukkitFireListener<Plugin> bukkitFireListener;

    public AccountFireHelper(Firebase firebase) {
        this.firebase = firebase;
        this.usersRef =  firebase.child("/users");
        this.avatarsRef = firebase.child("/avatars");
        this.bukkitFireListener = new BukkitFireListener<>(Plugin.class);
    }

    /**
     * Retrieve the User data using the minecraft player UUID
     * @param playerId The player UUID
     * @param callback listener to notify when the request is done.
     *                 The callback is notified with the user if exist or null.
     */
    @Override
    public void getUser(final String playerId, final Callback<User> callback) {
        Query query = usersRef.child(playerId);
        query.addListenerForSingleValueEvent(bukkitFireListener.listen(User.class, callback));
    }

    @Override
    public void getAvatars(final String playerId, final Callback<HashMap> callback) {
        Query query = usersRef.child(playerId);
        query.addListenerForSingleValueEvent(bukkitFireListener.listen(HashMap.class, callback));
    }

    @Override
    public void getAvatar(String playerUUID, String avatarId, final Callback<User.Avatar> callback) {
        Query query = avatarsRef.child(playerUUID).child(avatarId);
        query.addListenerForSingleValueEvent(bukkitFireListener.listen(User.Avatar.class, callback));
    }

    @Override
    public void saveUser(String playerUUID, User user) {
        usersRef.child(playerUUID).setValue(user);
    }

    @Override
    public String saveAvatar(String playerUUID, User.Avatar avatar) {
        Firebase avatarRef = avatarsRef.child(playerUUID).push();
        saveAvatar(playerUUID, avatarRef.getKey(), avatar);
        return avatarRef.getKey();
    }

    @Override
    public void saveAvatar(String playerUUID, String avatarId, User.Avatar avatar) {
        avatarsRef.child(playerUUID).child(avatarId).setValue(avatar);
    }

    @Override
    public void clearData(String playerUUID) {

    }
}
