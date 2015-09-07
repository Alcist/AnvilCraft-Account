package com.alcist.anvilcraft.account.commands.user;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.commands.CommandInfo;
import com.alcist.anvilcraft.account.commands.SubCommand;
import com.alcist.anvilcraft.account.models.Location;
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
@CommandInfo(name = "use",
        shortInv = "use",
        longInv = "use",
        usage = "/ac use -a <avatar name>",
        desc = "Log In as an avatar or switch",
        permission = "account.avatar.use"
)
public class AvatarUseCommand extends SubCommand {

    Plugin plugin;

    public AvatarUseCommand() {
        plugin = JavaPlugin.getPlugin(Plugin.class);
    }

    @Override
    public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You can't use this command from the console");
            return true;
        }

        String playerId = ((Player) sender).getUniqueId().toString();

        plugin.getAccountData().getUser(playerId, user -> {
            plugin.getAccountData().getAvatarByName(commandLine.getOptionValue("a"), avatarBundle -> {
                avatarBundle.forEach((key, avatar) -> {
                    if(user.avatars.get(key) != null) {

                        Location oldAvatarLocation = new Location(((Player) sender).getLocation());
                        String oldAvatarId = user.currentAvatar;
                        plugin.getAccountData().getAvatar(oldAvatarId, oldAvatar -> {
                            oldAvatar.location = oldAvatarLocation;
                            plugin.getAccountData().saveAvatar(oldAvatarId, oldAvatar);
                        });

                        user.currentAvatar = key;
                        plugin.getAccountData().saveUser(playerId, user);
                    }
                });
            });
        });
        return true;
    }

    @Override
    public Options getOptions() {
        return new Options()
                .addOption(Option.builder("a")
                        .longOpt("avatar")
                        .desc("switch to another avatar")
                        .argName("avatar name")
                        .hasArg()
                        .required()
                        .build())
                .addOption(Option.builder("h")
                        .longOpt("help")
                        .desc("shows this message").build());
    }
}
