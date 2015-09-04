package com.alcist.anvilcraft.account.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Created by istar on 04/09/15.
 */
public class Location {
    public String world;
    public int x;
    public int y;
    public int z;
    public float yaw;
    public float pitch;

    public Location() {
    }

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
