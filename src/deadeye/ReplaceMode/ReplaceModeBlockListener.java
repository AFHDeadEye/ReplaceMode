package deadeye.ReplaceMode;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

/**
 * ReplaceMode block listener
 * 
 * @author deadeye
 */
public class ReplaceModeBlockListener extends BlockListener {
    private final ReplaceMode plugin;

    public ReplaceModeBlockListener(final ReplaceMode plugin) {
        this.plugin = plugin;
    }

    public void onBlockRightClick(BlockRightClickEvent event) {
        if (plugin.isReplacer(event.getPlayer()) && (event.getItemInHand().getTypeId() == 277)) {
            event.getBlock().setTypeId(plugin.getReplaceId(event.getPlayer()));
        }
    }
}
