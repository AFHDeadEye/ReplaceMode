package deadeye.ReplaceMode;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handle events for all Player related events
 * @author deadeye
 */
public class ReplaceModePlayerListener extends PlayerListener {
    private final ReplaceMode plugin;

    public ReplaceModePlayerListener(ReplaceMode instance) {
        plugin = instance;
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(plugin.isReplacer(player)) {
            player.getInventory().addItem(new ItemStack(277, 1));
        }
    }
}

