package ac.minef.warpgui.menus;

import ac.minef.warpgui.util.Util;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuAPI implements Listener {
    private static MenuAPI instance;

    public static MenuAPI getInstance() {
        if (instance == null)
            synchronized (MenuAPI.class) {
                if (instance == null)
                    instance = new MenuAPI();
            }
        return instance;
    }

    public void putBorder(Menu menu, int slot) {
        final ItemStack grayPane = Util.createBorder(15);
        MenuItem pane = new MenuItem.UnclickableMenuItem() {
            public ItemStack getItemStack() {
                return grayPane;
            }
        };
        menu.addMenuItem(pane, slot);
    }

    public void putBorder(Menu menu, int x, int y) {
        final ItemStack grayPane = Util.createBorder(15);
        MenuItem pane = new MenuItem.UnclickableMenuItem() {
            public ItemStack getItemStack() {
                return grayPane;
            }
        };
        menu.addMenuItem(pane, x, y);
    }

    public Menu createMenu(String title, int rows) {
        return new Menu(title, rows);
    }

    public Menu cloneMenu(Menu menu) {
        return menu.clone();
    }

    public void removeMenu(Menu menu) {
        for (HumanEntity viewer : menu.getInventory().getViewers()) {
            if (viewer instanceof Player) {
                menu.closeMenu((Player)viewer);
                continue;
            }
            viewer.closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuItemClicked(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Menu && inventory.getTitle().equals(((Menu)inventory.getHolder()).title)) {
            event.setCancelled(true);
            ((Player)event.getWhoClicked()).updateInventory();
            switch (event.getAction()) {

            }
            Menu menu = (Menu)inventory.getHolder();
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player)event.getWhoClicked();
                if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                    if (menu.exitOnClickOutside())
                        menu.closeMenu(player);
                } else {
                    int index = event.getRawSlot();
                    if (index < inventory.getSize()) {
                        if (event.getAction() != InventoryAction.NOTHING)
                            menu.selectMenuItem(player, index, InventoryClickType.fromInventoryAction(event.getAction()));
                    } else if (menu.exitOnClickOutside()) {
                        menu.closeMenu(player);
                    }
                }
            }
        }
    }

    public static interface MenuCloseBehaviour {
        void onClose(Player param1Player, Menu param1Menu, boolean param1Boolean);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMenuClosed(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (inventory.getHolder() instanceof Menu) {
                Menu menu = (Menu)inventory.getHolder();
                MenuCloseBehaviour menuCloseBehaviour = menu.getMenuCloseBehaviour();
                if (menuCloseBehaviour != null)
                    menuCloseBehaviour.onClose((Player)event.getPlayer(), menu, menu.bypassMenuCloseBehaviour());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLogoutCloseMenu(PlayerQuitEvent event) {
        if (event.getPlayer().getOpenInventory() == null || !(event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof Menu))
            return;
        Menu menu = (Menu)event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        menu.setBypassMenuCloseBehaviour(true);
        menu.setMenuCloseBehaviour(null);
        event.getPlayer().closeInventory();
    }
}