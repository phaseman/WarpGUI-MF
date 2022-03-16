package ac.minef.warpgui.listeners;

import ac.minef.warpgui.warps.WarpGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.stream.Stream;

public class CommandListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void command(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage().trim();
        if (Stream.<String>of(new String[] { "/warp", "/ewarp", "/warps", "/ewarps" }).anyMatch(message::equalsIgnoreCase)) {
            e.setCancelled(true);
            WarpGUI warpGUI = new WarpGUI(e.getPlayer());
            warpGUI.open();
        }
    }
}