package com.alcist.anvilcraft.account.listeners.firebase;

import com.alcist.anvilcraft.account.api.Callback;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.models.User;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by istar on 04/08/14.
 */
public class AvatarFireListener<T> implements ValueEventListener {

    final private Class<T> dataClass;
    final private Callback<T> callback;

    public AvatarFireListener(Class<T> dataClass, Callback<T> callback) {
        this.dataClass = dataClass;
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
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
            T data = dataSnapshot.getValue(dataClass);
            callback.onCallBack(data);
        }
    }
}
