package usa.cactuspuppy.nccustom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@org.bukkit.plugin.java.annotation.command.Command(name = "nccustom", desc = "Custom command for the Noteblock Corner server", usage = "/nccustom <subcommand> [args]")
public class Delegator implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
