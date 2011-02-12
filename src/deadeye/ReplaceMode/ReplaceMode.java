package deadeye.ReplaceMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * ReplaceMode for Bukkit
 * 
 * @author deadeye
 */
public class ReplaceMode extends JavaPlugin {
    private final ReplaceModePlayerListener playerListener = new ReplaceModePlayerListener(this);
    private final ReplaceModeBlockListener blockListener = new ReplaceModeBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static Logger log;
    public static PermissionHandler Permissions = null;
    private final HashMap<Player, Integer> Replacers = new HashMap<Player, Integer>();

    public ReplaceMode(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder,
            File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        // TODO: Place any custom initialisation code here

        // NOTE: Event registration should be done in onEnable not here as all
        // events are unregistered when a plugin is disabled
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, this.blockListener, Priority.Normal, this);
        log = Logger.getLogger("Minecraft");
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        this.setupPermissions();
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
    }

    private Player matchPlayer(String split, CommandSender sender) {
        Player player;
        List<Player> players = getServer().matchPlayer(split);
        if (players.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Unknown player");
            player = null;
        } else {
            player = players.get(0);
        }
        return player;
    }

    private boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
    }

    public void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        PluginDescriptionFile pdfFile = this.getDescription();

        if (ReplaceMode.Permissions == null) {
            if (test != null) {
                ReplaceMode.Permissions = ((Permissions) test).getHandler();
            } else {
                log.info("[" + pdfFile.getName() + "]" + " Permission system not enabled. Disabling plugin.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {

        debugees.put(player, value);
    }

    public boolean isReplacer(Player player) {
        if (Replacers.containsKey(player)) {
            return true;
        }
        return false;
    }

    public boolean toggleReplacer(CommandSender sender, String[] split) {
        int id = 0;
        Player player = null;
        if (split.length > 1) {
            return false;
        }
        if (split.length == 0 && !(isReplacer(player))) {
            sender.sendMessage("Please Enter an item id!");
            return false;
        }
        try {
            id = Integer.parseInt(split[0]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(split[0] + "is not a valid number!");
            return false;
        }
        if (split.length == 1) {
            if (sender.isOp()) {
                player = matchPlayer(split[0], sender);
                if (player == null) {
                    return false;
                }
            }
        }

        if (isReplacer(player) && split.length == 0) {
            Replacers.remove(player);
            player.sendMessage("ReplaceMode deactivated!");
        }

        if (isReplacer(player) && split.length == 1) {
            Replacers.remove(player);
            Replacers.put(player, id);
            player.sendMessage("Item id changed!");
        } else {
            if (!(player.getInventory().contains(277))) {
                player.getInventory().addItem(new ItemStack(277, 1));
            }
            Replacers.put(player, id);
            player.sendMessage("ReplaceMode activated!");
        }

        return false;
    }

    public int getReplaceId(Player player) {
        if (isReplacer(player)) {
            return Replacers.get(player);
        }
        return 0;
    }
}
