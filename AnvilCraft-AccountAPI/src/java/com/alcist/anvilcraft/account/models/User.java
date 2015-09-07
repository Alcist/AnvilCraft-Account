package com.alcist.anvilcraft.account.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by istar on 03/08/14.
 */
public class User {
    public String minecraftName;
    public Date lastLogin;
    public String currentAvatar;
    public HashMap<String, Object> avatars;
}