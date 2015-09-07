package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.models.Avatar;
import com.alcist.anvilcraft.account.models.User;
import com.alcist.firehelper.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by istar on 04/08/14.
 */
public interface AccountAdapter {

    /**
     * Returns the user info from Firebase or the memory if it's already loaded.
     * @param playerUUID The player unique identifier
     * @param callback
     */
    void getUser(String playerUUID, Callback<User> callback);

    void getUserByName(String minecraftName, Callback<UserResponse> callback);

    /**
     * Saves the reference the user has to the player given on Firebase.
     * @param playerUUID The player unique identifier
     * @param user the user info.
     */
    void saveUser(String playerUUID, User user);

    void getAvatar(String avatarUUID, Callback<Avatar> callback);

    void getAvatarByName(String avatarName, Callback<AvatarResponse> callback);

    void saveAvatar(String avatarUUID, Avatar avatar);

    String saveAvatar(Avatar avatar);

    class MapResponse<T> extends HashMap<String, T> {}
    class UserResponse extends MapResponse<User>{}
    class AvatarResponse extends MapResponse<Avatar>{}
}