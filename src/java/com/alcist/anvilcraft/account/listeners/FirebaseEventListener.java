package com.alcist.anvilcraft.account.listeners;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.api.AccountAdapter;
import com.alcist.anvilcraft.account.api.AccountEventHandler;
import com.alcist.anvilcraft.account.api.models.Avatar;
import com.alcist.firehelper.Callback;
import com.alcist.firehelper.BukkitFireListener;
import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by istar on 03/09/15.
 */
public class FirebaseEventListener implements AccountEventHandler {

    // Reference to Account Fire Helper
    final AccountAdapter account;

    // Base references to Firebase
    final Firebase usersRef;
    final Firebase avatarsRef;
    final BukkitFireListener<Plugin> fireListener;

    HashMap<String, BukkitFireListener.Listener> avatarFireMap = new HashMap<>();

    public FirebaseEventListener(Firebase firebase, AccountAdapter account) {
        this.account = account;
        this.usersRef =  firebase.child("/users");
        this.avatarsRef = firebase.child("/avatars");
        this.fireListener = new BukkitFireListener<>(Plugin.class);
    }

    @Override
    public void addAvatarListener(String userUUID, Callback<Avatar> callback) {
        account.getUser(userUUID, user -> {
            if (user != null) {
                BukkitFireListener.Listener oldListener = avatarFireMap.get(userUUID);
                if (oldListener != null) {
                    avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(oldListener);
                }
                BukkitFireListener.Listener<Avatar> listener = fireListener.listen(Avatar.class, callback);
                avatarFireMap.put(userUUID, listener);
                avatarsRef.child(userUUID).child(user.currentAvatar).addValueEventListener(listener);
            }
        });
    }

    @Override
    public void removeAvatarListener(String userUUID) {
        BukkitFireListener.Listener listener = avatarFireMap.get(userUUID);
        if(listener != null) {
            account.getUser(userUUID, user -> {
                avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(listener);
                avatarFireMap.remove(userUUID);
            });
        }
    }

    @Override
    public void removeAllListeners() {
        avatarFireMap.forEach((userUUID, avatarFireListener) -> {
            account.getUser(userUUID, user -> {
                avatarsRef.child(userUUID).child(user.currentAvatar).removeEventListener(avatarFireListener);
                avatarFireMap.remove(userUUID);
            });
        });
    }
}
