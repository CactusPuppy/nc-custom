package usa.cactuspuppy.nccustom.command.subcmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import usa.cactuspuppy.nccustom.Main;
import usa.cactuspuppy.nccustom.command.CustomCommand;

import java.util.ArrayList;
import java.util.List;

public class JoinLeave extends CustomCommand implements Listener {
    static {
        moreInfo.put(MoreInfoKey.ACCESS_LEVEL, "2");
    }

    /**
     * Whether to show join/leave messages
     */
    private static boolean allowMessages;

    public JoinLeave() {
        super("joinleave", "joinleave [true/false]", "Disable or enable default server join messages");
        allowMessages = Boolean.valueOf(Main.getMainConfig().get("joinleave.messages", "true"));
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /nc joinleave <true|false|status>");
            return;
        }
        boolean allow;
        switch (args[0].toLowerCase()) {
            case "true":
                allow = true;
                break;
            case "false":
                allow = false;
                break;
            case "status":
                commandSender.sendMessage(ChatColor.GREEN + "Join/leave messages are currently " +
                        ChatColor.WHITE + (allowMessages ? "on" : "off"));
            default:
                commandSender.sendMessage(ChatColor.RED + args[0] + " is not true or false.");
                return;
        }
        if (allow == allowMessages) {
            commandSender.sendMessage(ChatColor.YELLOW + "Join/leave messages are already " +
                    ChatColor.WHITE + (allow ? "enabled" : "disabled"));
            return;
        }
        allowMessages = allow;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            if ("true".startsWith(args[0].toLowerCase())) {
                result.add("true");
            } else if ("false".startsWith(args[0].toLowerCase())) {
                result.add("false");
            } else if ("status".startsWith(args[0].toLowerCase())) {
                result.add("status");
            }
        }
        return result;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (allowMessages) {
            return;
        }
        event.setJoinMessage("");
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (allowMessages) {
            return;
        }
        event.setQuitMessage("");
    }
}
