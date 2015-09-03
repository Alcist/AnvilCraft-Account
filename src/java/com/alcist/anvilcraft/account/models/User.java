package com.alcist.anvilcraft.account.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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
            public float yaw;
            public float pitch;

            public Location(){}

            public Location(org.bukkit.Location location) {
                x = location.getBlockX();
                y = location.getBlockY();
                z = location.getBlockZ();
                yaw = location.getYaw();
                pitch = location.getPitch();
                world = location.getWorld().getName();
            }

            @JsonIgnore
            public org.bukkit.Location getBukkitLocation() {
                World bukkitWorld = Bukkit.getWorld(world);
                return new org.bukkit.Location(bukkitWorld, x, y, z, yaw, pitch);
            }
        }
    }
}