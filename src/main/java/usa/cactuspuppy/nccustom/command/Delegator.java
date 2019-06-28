package usa.cactuspuppy.nccustom.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import usa.cactuspuppy.nccustom.command.subcmd.JoinLeave;
import usa.cactuspuppy.nccustom.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@org.bukkit.plugin.java.annotation.command.Command(
    name = "nccustom",
    desc = "Custom command for the Noteblock Corner server",
    usage = "/nccustom <subcommand> [args]", aliases = {"nc"})
public class Delegator implements CommandExecutor, TabCompleter {
    private static Map<String, CustomCommand> subcmds = new HashMap<>();
    private static Map<String, CustomCommand> aliasMap = new HashMap<>();
    // Create all of the new subcommands, which automatically adds them to the subcmds map
    static {
        new JoinLeave();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String subCmd = args[0];
        if (subCmd.equalsIgnoreCase("help")) {
            //TODO: Help
            return true;
        }
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        CustomCommand handler = subcmds.get(subCmd);
        if (handler == null) {
            handler = aliasMap.get(subCmd);
            if (handler == null) {
                commandSender.sendMessage(ChatColor.RED + "Unknown subcommand " + subCmd);
                return false;
            }
        }
        if (!handler.hasPermission(commandSender, alias, newArgs)) {
            commandSender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform that command. Please contact and administrator if you believe this is in error.");
            return true;
        }
        if (!handler.onCommand(commandSender, command, alias, newArgs)) {
            commandSender.sendMessage(ChatColor.RED + "Usage: " + handler.getUsage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subcmds.keySet().stream().filter(s -> s.startsWith(args[0].toLowerCase())).sorted().collect(Collectors.toList());
        } else if (args.length == 2) {
            String subCmd = args[0];
            CustomCommand handler = subcmds.get(subCmd);
            if (handler == null) {
                return new ArrayList<>();
            }
            return handler.onTabComplete(commandSender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
        return new ArrayList<>();
    }

    static void registerSubcommand(String cmdName, CustomCommand subcmd) {
        if (subcmds.containsKey(cmdName)) {
            Logger.logSevere(Delegator.class, cmdName + " is already registered");
            return;
        }
        subcmds.put(cmdName, subcmd);
    }

    static void registerAlias(String cmdAlias, CustomCommand subcmd) {
        if (subcmds.containsKey(cmdAlias)) {
            Logger.logSevere(Delegator.class, cmdAlias + " is already a subcommand");
            return;
        } else if (aliasMap.containsKey(cmdAlias)) {
            Logger.logSevere(Delegator.class, cmdAlias + " is already a registered alias");
            return;
        }
        aliasMap.put(cmdAlias, subcmd);
    }
}
