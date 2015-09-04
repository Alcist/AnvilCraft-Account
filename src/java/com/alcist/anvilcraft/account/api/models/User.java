package com.alcist.anvilcraft.account.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Date;

/**
 * Created by istar on 03/08/14.
 */
public class User {
    public String minecraftName;
    public Date lastLogin;
    public String currentAvatar;

}