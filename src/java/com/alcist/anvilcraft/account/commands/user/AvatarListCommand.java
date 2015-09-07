package com.alcist.anvilcraft.account.commands.user;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.commands.BukkitHelpFormatter;
import com.alcist.anvilcraft.account.commands.Command;
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

import java.util.List;

/**
 * Created by istar on 04/09/15.
 */
@CommandInfo(
        name = "avatarlist",
        shortInv = "a",
        longInv = "avatars",
        usage = "/ac -a [filters]",
        desc = "list all avatars for the current user",
        permission = "anvilcraft.account.avatar.list")
public class AvatarListCommand extends SubCommand {

    Plugin plugin;

    public AvatarListCommand() {
        this.plugin = JavaPlugin.getPlugin(Plugin.class);
    }

    @Override
    public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {
        List<Avatar> avatars;

        if(commandLine.hasOption("h")) {
            BukkitHelpFormatter.printHelp(sender, info.usage(), getOptions());
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("This can't be used from terminal");
        }
        else {
            Player player = (Player) sender;
            plugin.getAccountData().getAvatars(player.getUniqueId().toString(), map -> {
                map.forEach((key, avatar) -> {
                    sender.sendMessage(avatar.toString());
                });
            });
        }
        return true;
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("shows this message").build())
                .addOption(Option.builder("u")
                        .longOpt("user")
                        .desc("shows the avatars for the given user").build());
    }

    @Override
    public Class<? extends Command> getChildClass() {
        return getClass();
    }
}
