package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.api.AnvilCraftAccount;
import com.alcist.anvilcraft.account.api.Callback;
import com.alcist.anvilcraft.account.listeners.firebase.AvatarFireListener;
import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.*;
import com.alcist.anvilcraft.account.api.AccountDataHelper;
import com.alcist.anvilcraft.account.api.AccountEventHandler;
import com.alcist.anvilcraft.account.listeners.bukkit.UserEventListener;
import com.alcist.firehelper.FireHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by istar on 03/08/14.
 */
public class Plugin extends JavaPlugin implements AnvilCraftAccount {

    //Firebase reference
    public Firebase firebase;

    //Bukkit Listeners
    public UserEventListener userListener;


    private AccountFireHelper fireHelper;

    private FireEventHandler eventHandler;


    @Override
    public void onEnable() {
        super.onEnable();
        Thread t = Thread.currentThread();

        firebase = ((FireHelper)Bukkit.getPluginManager().getPlugin("FireHelper")).getFirebase();
        fireHelper = new AccountFireHelper();
        eventHandler = new FireEventHandler();
        userListener = new UserEventListener();
        getServer().getPluginManager().registerEvents(userListener, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fireHelper = null;
        Firebase.goOffline();
    }

    @Override
    public AccountFireHelper getDataHelper() {
        return fireHelper;
    }

    @Override
    public FireEventHandler getEventHandler() {
        return eventHandler;
    }

    private class FireEventHandler implements AccountEventHandler {

        // Base references to Firebase
        Firebase usersRef =  firebase.child("/users");
        Firebase avatarsRef = firebase.child("/avatars");

        HashMap<String, AvatarFireListener> avatarFireMap = new HashMap<>();

        @Override
        public void addAvatarListener(final Player player) {
            final String playerUUID = player.getUniqueId().toString();

            fireHelper.getUser(playerUUID, new Callback<User>() {
                @Override
                public void onCallBack(User user) {
                    AvatarFireListener listener = avatarFireMap.get(playerUUID);
                    if(listener == null) {
                        listener = new AvatarFireListener(player);
                        avatarFireMap.put(playerUUID,listener);
                    }
                    avatarsRef.child(playerUUID).child(user.currentAvatar).addValueEventListener(listener);
                }
            });
        }

        @Override
        public void removeAvatarListener(final Player player) {
            final String playerUUID = player.getUniqueId().toString();

            fireHelper.getUser(playerUUID, new Callback<User>() {
                @Override
                public void onCallBack(User user) {
                    AvatarFireListener listener = avatarFireMap.get(playerUUID);
                    avatarsRef.child(playerUUID).child(user.currentAvatar).removeEventListener(listener);
                    avatarFireMap.remove(playerUUID);
                }
            });
        }
    }


    /**
     * Created by istar on 04/08/14.
     */
    private class AccountFireHelper implements AccountDataHelper {

        //HashMaps for memory persistence.

        //Minecraft UUID, User
        HashMap<String, User> userMap = new HashMap<>();
        //Minecraft UUID, List Avatar Ids
        HashMap<String, HashMap<String, User.Avatar>> avatarListMap = new HashMap<>();

        // Base references to Firebase
        Firebase usersRef =  firebase.child("/users");
        Firebase avatarsRef = firebase.child("/avatars");


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
}
