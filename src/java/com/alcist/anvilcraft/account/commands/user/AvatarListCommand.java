package com.alcist.anvilcraft.account.commands.user;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.commandapi.BukkitHelpFormatter;
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
 * Created by istar on 04/09/15.
 */
@CommandInfo(
        name = "avatarlist",
        shortInv = "ls",
        longInv = "list",
        usage = "/ac ls [-u <username>]",
        desc = "list all avatars for the current user",
        permission = "anvilcraft.account.avatar.list")
public class AvatarListCommand extends SubCommand {

    Plugin plugin;

    public AvatarListCommand() {
        this.plugin = JavaPlugin.getPlugin(Plugin.class);
    }

    @Override
    public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {

        if(commandLine.hasOption("h")) {
            BukkitHelpFormatter.printHelp(sender, info.usage(), getOptions());
        }
        else if (!commandLine.hasOption("u") && sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This can't be used from terminal");
        }
        else {
            if(sender instanceof ConsoleCommandSender) {
                String userName = commandLine.getOptionValue("u");
                getAvatarsByName(sender, userName);
            } else {
                if (commandLine.hasOption("u")) {
                    //TODO Check permission
                    sender.sendMessage("You don't have permission to do this.");
                }
                else {
                    Player player = (Player) sender;
                    getAvatars(sender, player.getUniqueId().toString());
                }
            }
        }
        return true;
    }

    private void getAvatarsByName(CommandSender sender,String userName) {
        plugin.getAccountData().getUserByName( userName, map -> {
            if(map == null) {
                sender.sendMessage("The user " + userName + " does not exist.");
            }
            else {
                map.values().forEach(user -> {
                    user.avatars.keySet().forEach(avatarId -> {
                        plugin.getAccountData().getAvatar(avatarId, avatar -> {
                            sender.sendMessage(avatar.name);
                        });
                    });
                });
            }
        });
    }

    private void getAvatars(CommandSender sender, String userId) {
        plugin.getAccountData().getUser(userId, user -> {
            user.avatars.keySet().forEach(avatarId -> {
                plugin.getAccountData().getAvatar(avatarId, avatar -> {
                    if(avatar != null) {
                        sender.sendMessage(avatar.name);
                    }
                });
            });
        });
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("shows this message").build())
                .addOption(Option.builder("u")
                        .longOpt("user")
                        .hasArg()
                        .desc("shows the avatars for the given user").build());
    }
}
