package usa.cactuspuppy.nccustom.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class CustomCommand {
    @Getter
    private String cmdName;
    @Getter
    private String usage;
    @Getter
    private String description;
    protected static Map<MoreInfoKey, String> moreInfo;

    public enum MoreInfoKey {
        /**
         * Access Levels:<br>
         * 0 - everyone<br>
         * 1 - specific permission required (see {@link MoreInfoKey#REQUIRED_PERMISSION})<br>
         * 2 - nccustom.admin permission required<br>
         * 3 - operators only<br>
         * 4 - console only<br>
         */
        ACCESS_LEVEL,
        /**
         * Required permission for {@link MoreInfoKey#ACCESS_LEVEL} 1
         */
        REQUIRED_PERMISSION
    }

    public CustomCommand(String cmdName, String usage, String description) {
        this.cmdName = cmdName;
        this.usage = usage;
        this.description = description;
        fixMissingMoreInfo();
        Delegator.registerSubcommand(cmdName, this);
    }

    public Map<MoreInfoKey, String> getMoreInfo() {
        return Collections.unmodifiableMap(moreInfo);
    }

    public boolean hasPermission(CommandSender sender, String alias, String[] args) {
        switch (moreInfo.getOrDefault(MoreInfoKey.ACCESS_LEVEL, "0")) {
            case "1":
                String permission = moreInfo.getOrDefault(MoreInfoKey.REQUIRED_PERMISSION, "nccustom.admin");
                return sender.hasPermission(permission);
            case "2":
                return sender.hasPermission("nccustom.admin");
            case "3":
                return sender.isOp();
            case "4":
                return (sender instanceof ConsoleCommandSender);
            default:
                return true;
        }
    }

    private void fixMissingMoreInfo() {
        //ACCESS_LEVEL
        moreInfo.putIfAbsent(MoreInfoKey.ACCESS_LEVEL, "0");
        if (!isInteger(moreInfo.get(MoreInfoKey.ACCESS_LEVEL))) {
            moreInfo.put(MoreInfoKey.ACCESS_LEVEL, "0");
        }

        //REQUIRED_PERMISSION
        if (moreInfo.get(MoreInfoKey.ACCESS_LEVEL).equals("1")) {
            moreInfo.putIfAbsent(MoreInfoKey.REQUIRED_PERMISSION, "nccustom.admin");
        }
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public abstract void onCommand(CommandSender commandSender, Command command, String alias, String[] args);

    public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args);
}
