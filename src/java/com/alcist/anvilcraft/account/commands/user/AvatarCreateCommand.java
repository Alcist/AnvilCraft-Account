package com.alcist.anvilcraft.account.commands.user;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.commands.BukkitHelpFormatter;
import com.alcist.anvilcraft.account.commands.CommandInfo;
import com.alcist.anvilcraft.account.commands.SubCommand;
import com.alcist.anvilcraft.account.models.Avatar;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by istar on 07/09/15.
 */
@CommandInfo(name = "create",
        shortInv = "cr" ,
        longInv = "create",
        usage = "/ac cr -n <name> [-u <username>]",
        desc = "creates a new avatar",
        permission = "account.avatar.create")
public class AvatarCreateCommand extends SubCommand {
    //TODO Define max avatars per user
    Plugin plugin;

    public AvatarCreateCommand() {
        this.plugin = JavaPlugin.getPlugin(Plugin.class);
    }

    @Override
    public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {
        if(commandLine.hasOption("h")) {
            BukkitHelpFormatter.printHelp(sender, info.usage(), getOptions());
        }
        else {

            if(sender instanceof ConsoleCommandSender) {

                plugin.getAccountData().getUserByName(commandLine.getOptionValue("u"), user -> {
                    if(user != null) {
                        createAvatar(sender,(String) user.keySet().toArray()[0], commandLine.getOptionValue("n"));
                    }
                    else {
                        sender.sendMessage("User not found");
                    }
                });
                return true;
            }
            else if(commandLine.hasOption("u")) {
                //TODO Check permission
                sender.sendMessage("You don't have permission to do this");
                return true;
            }

            String playerId = commandLine.getOptionValue("u");
            playerId = playerId == null ? ((Player)sender).getUniqueId().toString() : playerId;
            createAvatar(sender, playerId, commandLine.getOptionValue("n"));
        }
        return true;
    }

    private void createAvatar(CommandSender sender, String playerId, String avatarName) {
        plugin.getAccountData().getUser(playerId, user -> {
            Avatar avatar = new Avatar();
            avatar.name = avatarName;
            String avatarId = plugin.getAccountData().saveAvatar(avatar);

            if (user.avatars == null) {
                user.avatars = new HashMap<>();
            }
            user.avatars.put(avatarId, true);
            plugin.getAccountData().saveUser(playerId, user);
            sender.sendMessage("Avatar created");
        });
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder("n")
                        .longOpt("name")
                        .hasArg()
                        .required()
                        .argName("Avatar Name")
                        .desc("the avatar name")
                        .build())
                .addOption(Option.builder("u")
                        .longOpt("user")
                        .hasArg()
                        .argName("User Name")
                        .desc("The user name to wich add the avatar")
                        .build())
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("shows this message").build());
    }
}
