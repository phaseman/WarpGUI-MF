package ac.minef.warpgui.menus;

import ac.minef.warpgui.Main;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class Menu implements InventoryHolder {
    protected ConcurrentMap<Integer, MenuItem> items;

    private Inventory inventory;

    protected String title;

    protected int rows;

    protected boolean exitOnClickOutside;

    protected MenuAPI.MenuCloseBehaviour menuCloseBehaviour;

    protected boolean bypassMenuCloseBehaviour;

    protected Menu parentMenu;

    public Menu(String title, int rows) {
        this(title, rows, null);
    }

    public Menu(String title, int rows, Menu parentMenu) {
        this.items = Maps.newConcurrentMap();
        this.exitOnClickOutside = false;
        this.bypassMenuCloseBehaviour = false;
        this.title = title;
        this.rows = rows;
        this.parentMenu = parentMenu;
    }

    public void setMenuCloseBehaviour(MenuAPI.MenuCloseBehaviour menuCloseBehaviour) {
        this.menuCloseBehaviour = menuCloseBehaviour;
    }

    public MenuAPI.MenuCloseBehaviour getMenuCloseBehaviour() {
        return this.menuCloseBehaviour;
    }

    public void setBypassMenuCloseBehaviour(boolean bypassMenuCloseBehaviour) {
        this.bypassMenuCloseBehaviour = bypassMenuCloseBehaviour;
    }

    public boolean bypassMenuCloseBehaviour() {
        return this.bypassMenuCloseBehaviour;
    }

    public void setExitOnClickOutside(boolean exit) {
        this.exitOnClickOutside = exit;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return this.items;
    }

    public boolean addMenuItem(MenuItem item, int x, int y) {
        return addMenuItem(item, y * 9 + x);
    }

    public MenuItem getMenuItem(int index) {
        return this.items.get(Integer.valueOf(index));
    }

    public boolean addMenuItem(MenuItem item, int index) {
        ItemStack slot = getInventory().getItem(index);
        if (slot != null && slot.getType() != Material.AIR)
            removeMenuItem(index);
        item.setSlot(index);
        getInventory().setItem(index, item.getItemStack());
        this.items.put(Integer.valueOf(index), item);
        item.addToMenu(this);
        return true;
    }

    public boolean removeMenuItem(int x, int y) {
        return removeMenuItem(y * 9 + x);
    }

    public boolean removeMenuItem(int index) {
        ItemStack slot = getInventory().getItem(index);
        if (slot == null || slot.getType().equals(Material.AIR))
            return false;
        getInventory().clear(index);
        ((MenuItem)this.items.remove(Integer.valueOf(index))).removeFromMenu(this);
        return true;
    }

    protected void selectMenuItem(Player player, int index, InventoryClickType clickType) {
        if (this.items.containsKey(Integer.valueOf(index))) {
            MenuItem item = this.items.get(Integer.valueOf(index));
            item.onClick(player, clickType);
        }
    }

    public void openMenu(Player player) {
        if (!getInventory().getViewers().contains(player))
            player.openInventory(getInventory());
    }

    public void closeMenu(Player player) {
        if (getInventory().getViewers().contains(player)) {
            getInventory().getViewers().remove(player);
            player.closeInventory();
        }
    }

    public void scheduleUpdateTask(final Player player, int ticks) {
        (new BukkitRunnable() {
            public void run() {
                if (player == null || Bukkit.getPlayer(player.getName()) == null) {
                    cancel();
                    return;
                }
                if (player.getOpenInventory() == null || player.getOpenInventory().getTopInventory() == null || player.getOpenInventory().getTopInventory().getHolder() == null) {
                    cancel();
                    return;
                }
                if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof Menu)) {
                    cancel();
                    return;
                }
                Menu menu = (Menu)player.getOpenInventory().getTopInventory().getHolder();
                if (!menu.inventory.equals(Menu.this.inventory)) {
                    cancel();
                    return;
                }
                for (Map.Entry<Integer, MenuItem> entry : menu.items.entrySet())
                    Menu.this.getInventory().setItem(((Integer)entry.getKey()).intValue(), ((MenuItem)entry.getValue()).getItemStack());
            }
        }).runTaskTimer((Plugin) Main.getInstance(), ticks, ticks);
    }

    public Menu getParent() {
        return this.parentMenu;
    }

    public void setParent(Menu menu) {
        this.parentMenu = menu;
    }

    public Inventory getInventory() {
        if (this.inventory == null)
            this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        return this.inventory;
    }

    public boolean exitOnClickOutside() {
        return this.exitOnClickOutside;
    }

    protected Menu clone() {
        Menu clone = new Menu(this.title, this.rows);
        clone.setExitOnClickOutside(this.exitOnClickOutside);
        clone.setMenuCloseBehaviour(this.menuCloseBehaviour);
        for (Map.Entry<Integer, MenuItem> entry : this.items.entrySet())
            clone.addMenuItem(entry.getValue(), ((Integer)entry.getKey()).intValue());
        return clone;
    }

    public void updateMenu() {
        for (HumanEntity entity : getInventory().getViewers())
            ((Player)entity).updateInventory();
    }
}