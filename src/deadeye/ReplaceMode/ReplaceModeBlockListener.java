package deadeye.ReplaceMode;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRightClickEvent;

/**
 * ReplaceMode block listener
 * @author deadeye
 */
public class ReplaceModeBlockListener extends BlockListener {
    private final ReplaceMode plugin;

    public ReplaceModeBlockListener(final ReplaceMode plugin) {
        this.plugin = plugin;
    }

    public void onBlockRightClick(BlockRightClickEvent event) {
        if (plugin.isReplacer(event.getPlayer())) {
        event.getBlock().setTypeId(plugin.getReplaceId(event.getPlayer()));
        }
    }
}
