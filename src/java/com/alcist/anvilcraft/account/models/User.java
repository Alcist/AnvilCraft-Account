package com.alcist.anvilcraft.account.models;

import java.util.Date;

/**
 * Created by istar on 03/08/14.
 */
public class User {
    public String minecraftName;
    public Date lastLogin;
    public String currentAvatar;
    public String email;

    public static class Avatar {
        public String name;
        public Location location;

        public static class Location {
            public String world;
            public int x;
            public int y;
            public int z;

            public Location(){}

            public Location(org.bukkit.Location location) {
                x = location.getBlockX();
                y = location.getBlockY();
                z = location.getBlockZ();
                world = location.getWorld().getName();
            }
        }
    }
}