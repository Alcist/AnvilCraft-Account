package com.alcist.anvilcraft.account.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo
{
    /**
     * The actual name of the command. Not really used anywhere.
     */
    public String name();

    /**
     * The short name to invoke the command
     */
    public String shortInv();

    /**
     * The long name to invoke the command
     */
    public String longInv();

    /**
     * The usage message, i.e. how the command should be used.
     */
    public String usage();

    /**
     * A description of what the command does.
     */
    public String desc();

    /**
     * The permission required to execute this command.
     */
    public String permission();
}