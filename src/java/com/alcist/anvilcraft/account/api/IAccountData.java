package com.alcist.anvilcraft.account.api;

import com.alcist.anvilcraft.account.models.User;
import com.alcist.firehelper.Callback;

import java.util.HashMap;

/**
 * Created by istar on 04/08/14.
 */
public interface IAccountData {

    /**
     * Returns the user info from Firebase or the memory if it's already loaded.
     * @param playerUUID The player unique identifier
     * @param callback
     */
    void getUser(String playerUUID, Callback<User> callback);

    /**
     * Returns the avatars for the given user from Firebase or the memory if it's already loaded..
     * @param playerUUID
     * @param callback
     */
    void getAvatars(String playerUUID, Callback<HashMap> callback);

    /**
     * Return the given avatar from Firebase or the memory if it's already loaded.
     * @param playerUUID The player unique identifier
     * @param avatarId  The avatar unique identifier
     * @param callback
     */
    void getAvatar(String playerUUID, String avatarId, Callback<User.Avatar> callback);

    /**
     * Saves the reference the user has to the player given on Firebase.
     * @param playerUUID The player unique identifier
     * @param user the user info.
     */
    void saveUser(String playerUUID, User user);

    /**
     * Saves the given avatar on Firebase and persist it in memory.
     * @param playerUUID The player unique identifier
     * @param avatar The avatar.
     * @return the unique identifier Firebase has created for that avatar.
     */
    String saveAvatar(String playerUUID, User.Avatar avatar);

    /**
     * Saves the given avatar on Firebase and persist it in memory.
     * @param playerUUID The player unique identifier
     * @param avatarId The avatar unique identifier
     * @param avatar The avatar
     */
    void saveAvatar(String playerUUID, String avatarId, User.Avatar avatar);

    /**
     * Free the memory that the player given is using.
     * @param playerUUID The player unique identifier
     */
    void clearData(String playerUUID);
}