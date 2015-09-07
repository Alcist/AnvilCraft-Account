package com.alcist.anvilcraft.account.commands;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.bukkit.command.CommandSender;

public abstract class Command {

    public final CommandInfo info;

    public Command() {
        this.info = getChildClass().getAnnotation(CommandInfo.class);
    }

    public abstract Class<? extends Command> getChildClass();
}