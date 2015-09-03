package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.api.IAccountData;
import com.alcist.anvilcraft.account.api.AnvilCraftAccount;
import com.firebase.client.*;
import com.alcist.anvilcraft.account.listeners.bukkit.UserEventListener;
import com.alcist.firehelper.FireHelper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by istar on 03/08/14.
 */
public class Plugin extends JavaPlugin implements AnvilCraftAccount {

    //Bukkit Listeners
    private UserEventListener userListener;

    private AccountFireHelper fireHelper;

    private FireEventHandler eventHandler;


    @Override
    public void onEnable() {
        super.onEnable();
        Firebase firebase = ((FireHelper)Bukkit.getPluginManager().getPlugin("FireHelper")).getFirebase();
        fireHelper = new AccountFireHelper(firebase);
        eventHandler = new FireEventHandler(firebase);
        userListener = new UserEventListener();
        getServer().getPluginManager().registerEvents(userListener, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fireHelper = null;
    }

    @Override
    public IAccountData getAccountData() {
        return fireHelper;
    }

    @Override
    public FireEventHandler getEventHandler() {
        return eventHandler;
    }

}
