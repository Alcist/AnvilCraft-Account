package com.alcist.anvilcraft.account;

/**
 * Created by istar on 03/09/15.
 */
import com.alcist.anvilcraft.account.models.Avatar;
import com.alcist.firehelper.Callback;
import com.alcist.firehelper.BukkitFireListener;
import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.*;

import java.util.Map;

/**
 * Created by istar on 04/08/14.
 */
class FirebaseAccountAdapter implements AccountAdapter {

    // Firebase client reference
    final Firebase firebase;

    // Base references to Firebase
    final Firebase usersRef;
    final Firebase avatarsRef;
    final BukkitFireListener<Plugin> bukkitFireListener;

    public FirebaseAccountAdapter(Firebase firebase) {
        this.firebase = firebase;
        this.usersRef =  firebase.child("/users");
        this.avatarsRef = firebase.child("/avatars");
        this.usersRef.keepSynced(true);
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
    public void getUserByName(String minecraftName, Callback<UserResponse> callback) {
        usersRef.orderByChild("minecraftName")
                .equalTo(minecraftName)
                .addListenerForSingleValueEvent(bukkitFireListener.listen(UserResponse.class, callback));
    }

    @Override
    public void saveUser(String playerUUID, User user) {
        usersRef.child(playerUUID).setValue(user);
    }

    @Override
    public void getAvatar(String avatarUUID, Callback<Avatar> callback) {
        avatarsRef.child(avatarUUID)
                .addListenerForSingleValueEvent(bukkitFireListener.listen(Avatar.class, callback));
    }

    @Override
    public void getAvatarByName(String avatarName, Callback<AvatarResponse> callback) {
        avatarsRef.orderByChild("name")
                .equalTo(avatarName)
                .addListenerForSingleValueEvent(bukkitFireListener.listen(AvatarResponse.class, callback));
    }

    @Override
    public void saveAvatar(String avatarUUID, Avatar avatar) {
        avatarsRef.child(avatarUUID).setValue(avatar);
    }

    @Override
    public String saveAvatar(Avatar avatar) {
        Firebase avatarRef = avatarsRef.push();
        avatarRef.setValue(avatar);
        return avatarRef.getKey();
    }
}
