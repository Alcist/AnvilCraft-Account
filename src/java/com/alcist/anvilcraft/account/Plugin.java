package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.commands.AccountCommandHandler;
import com.alcist.anvilcraft.account.listeners.FirebaseEventListener;
import com.firebase.client.*;
import com.alcist.anvilcraft.account.listeners.BukkitEventListener;
import com.alcist.firehelper.FireHelper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by istar on 03/08/14.
 */
public class Plugin extends JavaPlugin implements AnvilCraftAccount {

    //Bukkit Listeners
    private BukkitEventListener userListener;

    private FirebaseAccountAdapter fireHelper;

    private FirebaseEventListener eventHandler;

    @Override
    public void onEnable() {
        super.onEnable();
        Firebase firebase = ((FireHelper)Bukkit.getPluginManager().getPlugin("FireHelper")).getFirebase();
        fireHelper = new FirebaseAccountAdapter(firebase);
        eventHandler = new FirebaseEventListener(firebase, fireHelper);
        userListener = new BukkitEventListener(eventHandler);
        new AccountCommandHandler(this);
        getServer().getPluginManager().registerEvents(userListener, this);
        setConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventHandler.removeAllListeners();
    }

    public void setConfig() {
        saveResource("config.yml", false);
    }

    @Override
    public AccountAdapter getAccountData() {
        return fireHelper;
    }

}
