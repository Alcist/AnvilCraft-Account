package com.alcist.anvilcraft.account.api;

/**
 * Created by istar on 04/08/14.
 */
public interface AnvilCraftAccount {
    AccountEventHandler getEventHandler();
    AccountDataHelper getDataHelper();
}
