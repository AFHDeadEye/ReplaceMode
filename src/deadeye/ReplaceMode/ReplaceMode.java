package deadeye.ReplaceMode;

import java.io.File;
import java.util.HashMap;
//import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.ChatColor;
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
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.playerListener, Priority.Normal, this);
        log = Logger.getLogger("Minecraft");
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        this.setupPermissions();
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
    }
/*      !!!!!!!!!!Auch im Import wieder einkommentieren!!!!!!!!!!
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
*/
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
    
    public boolean idChecker (int id){
        int failId[] = new int[28];
        
        failId[0] = 6;
        failId[1] = 37;
        failId[2] = 38;
        failId[3] = 39;
        failId[4] = 40;
        failId[5] = 44;
        failId[6] = 50;
        failId[7] = 51;
        failId[8] = 52;
        failId[9] = 55;
        failId[10] = 59;
        failId[11] = 63;
        failId[12] = 64;
        failId[13] = 65;
        failId[14] = 66;
        failId[15] = 68;
        failId[16] = 69;
        failId[17] = 70;
        failId[18] = 71;
        failId[19] = 72;
        failId[20] = 75;
        failId[21] = 76;
        failId[22] = 77;
        failId[23] = 81;
        failId[24] = 83;
        failId[25] = 85;
        failId[26] = 90;
        failId[27] = 92;
        for (int i = 0 ; i <= 27 ; i++){
            if (id == failId[i]){
                return false;
            }
        }
        return true;
    }

    public boolean toggleReplacer(CommandSender sender, String[] split) {
        int id = 0;
        Player player = null;
        if (!(sender instanceof Player)) {
            return false;
        }
        player = (Player) sender;
        if (!(Permissions.has(player, "replacemode.replacemode"))) {
            return false;
        }
        if (split.length > 1) {
            return false;
        }
        if (split.length == 0) {
            if (!(isReplacer(player))) {
                player.sendMessage("Please Enter an item id!");
                return false;
            } else {
                Replacers.remove(player);
                player.sendMessage("ReplaceMode deactivated!");
                return true;
            }
        }
        try {
            id = Integer.parseInt(split[0]);
        } catch (NumberFormatException ex) {
            player.sendMessage(split[0] + "is not a valid number!");
            return false;
        }
        if (split.length == 1) {
            if (!(idChecker(id))){
                player.sendMessage("Please enter a different id!");
                return false;
            }
            if (isReplacer(player)) {
                if (id == 93){
                    if (idChecker(player.getItemInHand().getTypeId())){
                        Replacers.remove(player);
                        Replacers.put(player, player.getItemInHand().getTypeId());
                        player.sendMessage("Item id changed!");
                        return true;
                    }
                    player.sendMessage("Not that item pal!");
                    return false;
                }
                Replacers.remove(player);
                Replacers.put(player, id);
                player.sendMessage("Item id changed!");
                return true;
            } else {
                if (!(player.getInventory().contains(277))) {
                    player.getInventory().addItem(new ItemStack(277, 1));
                }
                Replacers.put(player, id);
                player.sendMessage("ReplaceMode activated!");
                return true;
            }
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
