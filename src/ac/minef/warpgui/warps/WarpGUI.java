package ac.minef.warpgui.warps;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ac.minef.warpgui.Main;
import ac.minef.warpgui.menus.InventoryClickType;
import ac.minef.warpgui.menus.Menu;
import ac.minef.warpgui.menus.MenuItem;
import ac.minef.warpgui.util.ConfigUtils;
import ac.minef.warpgui.util.ItemStackUtils;
import ac.minef.warpgui.util.Message;
import ac.minef.warpgui.util.Util;

import org.apache.commons.lang.WordUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpGUI {
    private int inventorySize;

    private Menu mainMenu;

    private Player player;

    private String title;

    public WarpGUI(Player player) {
        this.player = player;
        init();
    }

    private void init() {
        List<MenuItem> warpItems = Main.getInstance().getEss().getWarps().getList().stream().map(this::getWarpIcon).collect(Collectors.toList());
        this.inventorySize = ConfigUtils.getInstance().fit(warpItems.size());
        this.title = Util.color(Main.getInstance().getConfig().getString("Options.title").replaceAll("%amount%", NumberFormat.getInstance().format(warpItems.size())));
        this.mainMenu = new Menu(this.title, this.inventorySize);
        try {
            for (MenuItem item : warpItems) {
                if (item != null) this.mainMenu.addMenuItem(item, warpItems.indexOf(item));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ItemStack borderStack = ItemStackUtils.load(Main.getInstance().getConfig().getConfigurationSection("Options.Format.Border").getValues(true));
        for (int free = 0; free < this.mainMenu.getInventory().getSize(); free++) {
            if (this.mainMenu.getInventory().getItem(free) == null) {
                MenuItem item = new MenuItem() {
                    public void onClick(Player p0, InventoryClickType p1) {
                        getMenu().closeMenu(p0);
                    }

                    public ItemStack getItemStack() {
                        return borderStack;
                    }
                };
                this.mainMenu.addMenuItem(item, free);
            }
        }
    }

    public void open() {
        this.mainMenu.openMenu(this.player);
        ConfigUtils.getInstance().playParticle(this.player, "menuOpen");
        ConfigUtils.getInstance().playWarpSound(this.player, "openMenu");
    }

    public MenuItem getWarpIcon(final String warp) {
        Optional<Location> warpLocationOptional = Optional.empty();
        try {
            warpLocationOptional = Optional.of(Main.getInstance().getEss().getWarps().getWarp(warp));
        } catch (Exception exception) {}
        if (!warpLocationOptional.isPresent())
            return null;
        Location warpLocation = warpLocationOptional.get();
        String warpStatus = this.player.hasPermission(Main.getInstance().getConfig().getString("Options.permission").replaceAll("%warp%", warp)) ? "Available" : "Locked";
        final ItemStack icon = ItemStackUtils.load(Main.getInstance().getConfig().getConfigurationSection("Options.Format." + warpStatus).getValues(true));
        if (icon != null && icon.hasItemMeta()) {
            ItemMeta meta = icon.getItemMeta();
            if (meta.hasDisplayName())
                meta.setDisplayName(meta.getDisplayName()
                        .replaceAll("%name%", WordUtils.capitalize(warp))
                        .replaceAll("%world%", WordUtils.capitalize(warpLocation.getWorld().getName()))
                        .replaceAll("%x%", NumberFormat.getInstance().format(warpLocation.getBlockX()))
                        .replaceAll("%y%", NumberFormat.getInstance().format(warpLocation.getBlockY()))
                        .replaceAll("%z%", NumberFormat.getInstance().format(warpLocation.getBlockZ()))
                        .replaceAll("%warmup%", NumberFormat.getInstance().format(Main.getInstance().getEss().getConfig().getDouble("teleport-delay"))));
            if (meta.hasLore())
                meta.setLore((List)meta.getLore().stream()
                        .map(line -> line.replaceAll("%name%", WordUtils.capitalize(warp)).replaceAll("%world%", WordUtils.capitalize(warpLocation.getWorld().getName())).replaceAll("%x%", NumberFormat.getInstance().format(warpLocation.getBlockX())).replaceAll("%y%", NumberFormat.getInstance().format(warpLocation.getBlockY())).replaceAll("%z%", NumberFormat.getInstance().format(warpLocation.getBlockZ())).replaceAll("%warmup%", NumberFormat.getInstance().format(Main.getInstance().getEss().getConfig().getDouble("teleport-delay"))))

                        .collect(Collectors.toList()));
            Main.getInstance().getConfig().getConfigurationSection("Warps").getKeys(false).forEach(sectionWarp -> {
                if (sectionWarp.equalsIgnoreCase(warp)) {
                    Material newIcon = ItemStackUtils.loadMaterial(Main.getInstance().getConfig().getString("Warps." + sectionWarp + ".material"));
                    if (newIcon != null) {
                        icon.setType(newIcon);
                        icon.setDurability(ItemStackUtils.getData(Main.getInstance().getConfig().getString("Warps." + sectionWarp + ".material")));
                    }
                }
            });
            icon.setItemMeta(meta);
        }
        switch (warpStatus) {
            case "Available":
                MenuItem item = new MenuItem() {
                    public void onClick(Player p0, InventoryClickType p1) {
                        getMenu().closeMenu(p0);
                        ConfigUtils.getInstance().playWarpSound(p0, "warpSuccess");
                        // Message.sendMessage((CommandSender)p0, Message.generate("WARP-SUCCESS").replace("%name%", warp));
                        ConfigUtils.getInstance().playParticle(WarpGUI.this.player, "warpSuccess");
                        p0.performCommand("warp " + warp);
                    }

                    public ItemStack getItemStack() {
                        return icon;
                    }
                };
                return item;
        }
        MenuItem item = new MenuItem() {
            public void onClick(Player p0, InventoryClickType p1) {
                getMenu().closeMenu(p0);
                ConfigUtils.getInstance().playWarpSound(p0, "warpLocked");
                ConfigUtils.getInstance().playParticle(WarpGUI.this.player, "warpLocked");
                Message.sendErrorMessage((CommandSender)p0, Message.generate("WARP-LOCKED").replace("%name%", warp));
            }

            public ItemStack getItemStack() {
                return icon;
            }
        };
        return item;
    }
}