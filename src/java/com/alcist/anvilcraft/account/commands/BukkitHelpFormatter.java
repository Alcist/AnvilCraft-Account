package com.alcist.anvilcraft.account.commands;

import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.apache.commons.cli.Options;
import org.bukkit.command.CommandSender;

/**
 * Created by istar on 06/09/15.
 */
public class BukkitHelpFormatter {

    public static void printHelp(CommandSender sender, String header, Options options) {
        sender.sendMessage(header);
        options.getOptions().forEach(option -> {
           StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-");
            stringBuilder.append(option.getOpt());
            if (option.hasLongOpt()) {
                stringBuilder.append(",--");
                stringBuilder.append(option.getLongOpt());
            }
            if (option.hasArg()) {
                stringBuilder.append(" <");
                stringBuilder.append(option.getArgName());
                stringBuilder.append(">");
            }
            if (option.hasArgs()) {
                stringBuilder.append(" [");
                stringBuilder.append(option.getArgName());
                stringBuilder.append("]");
            }
            stringBuilder.append(" ");
            stringBuilder.append(option.getDescription());

            sender.sendMessage(stringBuilder.toString());
        });
    }
}
