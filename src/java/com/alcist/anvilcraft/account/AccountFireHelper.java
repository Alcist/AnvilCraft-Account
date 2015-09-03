package com.alcist.anvilcraft.account;

/**
 * Created by istar on 03/09/15.
 */

import com.alcist.anvilcraft.account.api.IAccountData;
import com.alcist.anvilcraft.account.api.Callback;
import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.*;

import java.util.HashMap;

/**
 * Created by istar on 04/08/14.
 */
class AccountFireHelper implements IAccountData {

    //HashMaps for memory persistence.

    //Minecraft UUID, User
    HashMap<String, User> userMap = new HashMap<>();
    //Minecraft UUID, List Avatar Ids
    HashMap<String, HashMap<String, User.Avatar>> avatarListMap = new HashMap<>();

    // Firebase client reference
    final Firebase firebase;

    // Base references to Firebase
    final Firebase usersRef;
    final Firebase avatarsRef;

    public AccountFireHelper(Firebase firebase) {
        this.firebase = firebase;
        this.usersRef =  firebase.child("/users");
        this.avatarsRef = firebase.child("/avatars");
    }

    /**
     * Retrieve the User data using the minecraft player UUID
     * @param playerId The player UUID
     * @param callback listener to notify when the request is done.
     *                 The callback is notified with the user if exist or null.
     */
    @Override
    public void getUser(final String playerId, final Callback<User> callback) {
        User user = userMap.get(playerId);
        if (user == null) {
            // /users/{playerId}/{User Object}

            Query query = usersRef.child(playerId).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null) {
                        userMap.put(playerId, user);
                    }
                    callback.onCallBack(user);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    callback.onCallBack(null);
                }
            });
        } else {
            callback.onCallBack(user);
        }
    }

    @Override
    public void getAvatars(final String playerId, final Callback<HashMap<String,User.Avatar>> callback) {
        HashMap<String, User.Avatar> avatars = avatarListMap.get(playerId);
        if(avatars == null) {
            Query query = avatarsRef.child(playerId).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, User.Avatar> avatars = null;
                    avatars = dataSnapshot.getValue(avatars.getClass());
                    if(avatars != null) {
                        avatarListMap.put(playerId, avatars);
                    }
                    callback.onCallBack(avatars);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    callback.onCallBack(null);
                }
            });
        } else {
            callback.onCallBack(avatars);
        }
    }

    @Override
    public void getAvatar(String playerUUID, String avatarId, final Callback<User.Avatar> callback) {
        HashMap<String, User.Avatar> avatarMap = avatarListMap.get(playerUUID);
        User.Avatar avatar = null;
        if(avatarMap != null) {
            avatar = avatarMap.get(avatarId);
        }
        if(avatar != null) {
            callback.onCallBack(avatar);
        }
        else {
            Query query = avatarsRef.child(playerUUID).child(avatarId).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User.Avatar avatar = dataSnapshot.getValue(User.Avatar.class);
                    callback.onCallBack(avatar);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    callback.onCallBack(null);
                }
            });
        }
    }

    @Override
    public void saveUser(String playerUUID, User user) {
        userMap.put(playerUUID, user);
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
        HashMap<String, User.Avatar> avatars = avatarListMap.get(playerUUID);
        if(avatars == null) {
            avatars = new HashMap<>();
            avatarListMap.put(playerUUID, avatars);
        }
        avatars.put(avatarId, avatar);
        avatarsRef.child(playerUUID).child(avatarId).setValue(avatar);
    }

    @Override
    public void clearData(String playerUUID) {
        userMap.remove(playerUUID);
        avatarListMap.remove(playerUUID);
    }
}
