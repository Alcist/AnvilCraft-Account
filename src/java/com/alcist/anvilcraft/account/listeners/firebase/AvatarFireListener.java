package com.alcist.anvilcraft.account.listeners.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.models.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by istar on 04/08/14.
 */
public class AvatarFireListener implements ValueEventListener {

    Player player;

    public AvatarFireListener(Player player) {
        this.player = player;
    }

    @Override
    synchronized public void onDataChange(DataSnapshot dataSnapshot) {
        Runnable rn = new SyncedT(dataSnapshot);
        Bukkit.getScheduler().runTask(Plugin.getPlugin(Plugin.class), rn);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    private class SyncedT implements Runnable {

        DataSnapshot dataSnapshot;

        public SyncedT(DataSnapshot dataSnapshot) {
            this.dataSnapshot = dataSnapshot;
        }

        @Override
        public void run() {
            User.Avatar avatar = dataSnapshot.getValue(User.Avatar.class);
            player.setDisplayName(avatar.name);
            player.setPlayerListName(avatar.name);
            World world = Bukkit.getWorld(avatar.location.world);
            Location location = new Location(world, avatar.location.x, avatar.location.y, avatar.location.z);
            player.teleport(location);
        }
    }
}
