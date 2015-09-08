package com.alcist.anvilcraft.account.commands.user;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.models.Location;
import com.alcist.commandapi.CommandInfo;
import com.alcist.commandapi.SubCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by istar on 08/09/15.
 */
@CommandInfo(name = "remove",
        shortInv = "rm",
        longInv = "remove",
        usage = "/ac rm -a <avatar name> [-u <user>]",
        desc = "Remove the avatar with the name proved",
        permission = "account.avatar.remove"
)
public class AvatarRemoveCommand extends SubCommand {

    Plugin plugin;

    public AvatarRemoveCommand() {
        plugin = JavaPlugin.getPlugin(Plugin.class);
    }

    @Override
    public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {

        if(commandLine.hasOption("u")) {
            //TODO Check permission
            sender.sendMessage("You don't have permission to do this");
        }
        else if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You have to specify the user parameter");
        }
        else {
            String playerId = ((Player) sender).getUniqueId().toString();
            String avatarName = commandLine.getOptionValue("a");

            removeAvatar(sender, playerId, avatarName);
        }

        return true;
    }

    public void removeAvatar(CommandSender sender, String playerId, String avatarName) {
        plugin.getAccountData().getUser(playerId, user -> {
            plugin.getAccountData().getAvatarByName(avatarName, avatarBundle -> {
                avatarBundle.forEach((key, avatar) -> {
                    if(user.currentAvatar.equals(key)) {
                        sender.sendMessage("You can't remove a character you are using!");
                    }
                    else {
                        plugin.getAccountData().removeAvatar(key);
                        sender.sendMessage("Character " + avatarName + " removed");
                    }
                });
            });
        });
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder("a")
                        .longOpt("avatar")
                        .desc("remove the avatar with this name")
                        .argName("avatar name")
                        .hasArg()
                        .required()
                        .build())
                .addOption(Option.builder("u")
                        .longOpt("user")
                        .desc("the username parent of the avatar")
                        .argName("minecraft name")
                        .hasArg()
                        .build())
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("shows this message").build());
    }
}
