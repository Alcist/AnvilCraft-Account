package com.alcist.anvilcraft.account.commands;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.commands.user.AvatarCreateCommand;
import com.alcist.anvilcraft.account.commands.user.AvatarListCommand;
import com.alcist.anvilcraft.account.commands.user.AvatarRemoveCommand;
import com.alcist.anvilcraft.account.commands.user.AvatarUseCommand;
import com.alcist.commandapi.CommandHandler;
import com.alcist.commandapi.CommandInfo;
import com.alcist.commandapi.SubCommand;

/**
 * Created by istar on 07/09/15.
 */

@CommandInfo(
        name = "account",
        shortInv = "ac",
        longInv = "account",
        usage = "/ac <subcommand> [subcommand options]",
        desc = "Base command for AnvilCraft Account",
        permission = "anvilcraft.account")
public class AccountCommandHandler extends CommandHandler {

    public AccountCommandHandler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Class<? extends SubCommand> [] getCommands() {
        return new Class[]{
                AvatarListCommand.class,
                AvatarCreateCommand.class,
                AvatarUseCommand.class,
                AvatarRemoveCommand.class
        };
    }

}
