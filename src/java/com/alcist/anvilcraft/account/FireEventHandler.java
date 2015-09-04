package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.api.AccountEventHandler;
import com.alcist.firehelper.Callback;
import com.alcist.firehelper.BukkitFireListener;
import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by istar on 03/09/15.
 */
class FireEventHandler implements AccountEventHandler {

    // Reference to Account Fire Helper
    final AccountFireHelper fireHelper;

    // Firebase cliente reference
    final Firebase firebase;

    // Base references to Firebase
    final Firebase usersRef;
    final Firebase avatarsRef;
    final BukkitFireListener<Plugin> fireListener;

    HashMap<String, BukkitFireListener.Listener> avatarFireMap = new HashMap<>();

    public FireEventHandler(Firebase firebase) {
        this.firebase = firebase;
        this.fireHelper = new AccountFireHelper(firebase);
        this.usersRef =  firebase.child("/users");
        this.avatarsRef = firebase.child("/avatars");
        this.fireListener = new BukkitFireListener<>(Plugin.class);
    }

    @Override
    public void addAvatarListener(String userUUID, Callback<User.Avatar> callback) {
        fireHelper.getUser(userUUID, user -> {
            if (user != null) {
                BukkitFireListener.Listener oldListener = avatarFireMap.get(userUUID);
                if(oldListener != null) {
                    avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(oldListener);
                }
                BukkitFireListener.Listener<User.Avatar> listener = fireListener.listen(User.Avatar.class, callback);
                avatarFireMap.put(userUUID, listener);
                avatarsRef.child(userUUID).child(user.currentAvatar).addValueEventListener(listener);
            }
        });
    }

    @Override
    public void removeAvatarListener(String userUUID) {
        BukkitFireListener.Listener listener = avatarFireMap.get(userUUID);
        if(listener != null) {
            fireHelper.getUser(userUUID, user -> {
                avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(listener);
                avatarFireMap.remove(userUUID);
            });
        }
    }

    @Override
    public void removeAllListeners() {
        avatarFireMap.forEach((userUUID, avatarFireListener) -> {
            fireHelper.getUser(userUUID, user -> {
                avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(avatarFireListener);
                avatarFireMap.remove(userUUID);
            });
        });
    }
}
