package com.alcist.anvilcraft.account.commands;

import java.util.*;

import com.alcist.anvilcraft.account.Plugin;
import com.alcist.anvilcraft.account.permissions.Permissions;
import org.apache.commons.cli.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandHandler extends Command implements CommandExecutor {

    private final Map<Option, SubCommand> commands;
    private final Options options;
    private final Permissions permissions;

    public CommandHandler(Plugin plugin) {
        this.permissions = new Permissions();
        this.options = new Options();
        this.commands = new HashMap<>();
        registerCommands();
        plugin.getCommand(info.longInv()).setExecutor(this);
        plugin.getCommand(info.shortInv()).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bcmd, String label, String[] args) {

        // Grab the base and arguments.
        String base = (args.length > 0 ? args[0] : "");

        // If there's no base argument, show a helpful message.
        if (base.equals("")) {
            BukkitHelpFormatter.printHelp(sender, info.usage(), options);
        }

        if(this.options.hasOption(base)) {
            Option option = this.options.getOption(base);
            SubCommand helpCommand = commands.get(options.getOption("help"));
            try {
                String[] subcommandArgs = trimFirstArg(args);
                SubCommand command = commands.getOrDefault(option, helpCommand);
                CommandLine commandLine = new DefaultParser().parse(command.getOptions(), subcommandArgs);
                if(!command.execute(sender, commandLine, subcommandArgs)) {
                    CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
                    String usage = "Usage: " + info.usage();
                    BukkitHelpFormatter.printHelp(sender, usage, command.getOptions());
                }
            } catch (ParseException e) {
                helpCommand.execute(sender, null, "");
                e.printStackTrace();
            }

        }

        return true;
    }

    /**
     * Remove the first argument of a string. This is because the very first
     * element of the arguments array will be the command itself.
     * @param args an array of length n
     * @return the same array minus the first element, and thus of length n-1
     */
    private String[] trimFirstArg(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }


    /**
     * Register all the commands directly.
     * This could also be done with a somewhat dirty classloader/resource reader
     * method, but this is neater, albeit more manual work.
     */
    private void registerCommands() {
//        commands = new LinkedHashMap<String,Command>();
//        register(HelpCommand.class);
        register(new HelpCommand());

        Arrays.asList(getCommands()).forEach(this::register);
    }

    /**
     * Register a command.
     * The Command's CommandInfo annotation is queried to find its pattern
     * string, which is used to map the commands.
     * @param c a Command
     */
    private void register(Class<? extends SubCommand> c) {
        try {
            register(c.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(SubCommand c) {
        CommandInfo info = c.getClass().getAnnotation(CommandInfo.class);
        if (info == null) return;

        try {
            Option subcommand = Option.builder(info.shortInv()).build();
            if(info.longInv() != null && !info.longInv().isEmpty()) {
                subcommand.setLongOpt(info.longInv());
            }

            if(info.desc() != null && !info.desc().isEmpty()) {
                subcommand.setDescription(info.desc());
            }

            options.addOption(subcommand);
            commands.put(subcommand, c);
//            commands.put(info.pattern(), c.newInstance());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract Class<? extends SubCommand>[] getCommands();

    @CommandInfo(
            name = "help",
            shortInv = "h",
            longInv = "help",
            usage = "help",
            desc = "Shows this message",
            permission = "*"
    )
    public class HelpCommand extends SubCommand {

        @Override
        public boolean execute(CommandSender sender, CommandLine commandLine, String... args) {
            BukkitHelpFormatter.printHelp(sender, info.usage(), options);
            return true;
        }

        @Override
        public Options getOptions() {
            return new Options();
        }

    }
}