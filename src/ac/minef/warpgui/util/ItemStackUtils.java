package ac.minef.warpgui.util;

import ac.minef.warpgui.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ItemStackUtils {
    private static List<String> replaceColors(List<String> list) {
        List<String> listTemp = new ArrayList<>();
        for (String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            listTemp.add(s);
        }
        return listTemp;
    }

    public static Material loadMaterial(String configInput) {
        try {
            ItemStack stack = null;
            stack = Main.getInstance().getEss().getItemDb().get(configInput, 1);
            return stack.getType();
        } catch (Exception ignore) {
            Main.getInstance().getLogger().severe(ChatColor.stripColor(ignore.getMessage()));
            return null;
        }
    }

    public static short getData(String configInput) {
        try {
            ItemStack stack = null;
            stack = Main.getInstance().getEss().getItemDb().get(configInput, 1);
            return stack.getDurability();
        } catch (Exception ignore) {
            Main.getInstance().getLogger().severe(ChatColor.stripColor(ignore.getMessage()));
            return 0;
        }
    }

    public static ItemStack load(Map<String, Object> keys) {
        try {
            ItemStack stack = null;
            String item = "";
            if (keys.containsKey("material"))
                if (keys.get("material") instanceof List) {
                    List<String> list = (List<String>)keys.get("material");
                    item = list.get(keys.containsKey("index") ? ((Integer)keys.get("index")).intValue() : ThreadLocalRandom.current().nextInt(list.size()));
                } else {
                    item = keys.get("material").toString();
                }
            if (keys.containsKey("material") && keys.containsKey("amount")) {
                stack = Main.getInstance().getEss().getItemDb().get(item, Integer.parseInt(keys.get("amount").toString()));
            } else {
                stack = Main.getInstance().getEss().getItemDb().get(item, 1);
            }
            ItemMeta meta = stack.getItemMeta();
            if (keys.containsKey("name"))
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', keys.get("name").toString()));
            if (keys.containsKey("playerhead"))
                ((SkullMeta)meta).setOwner(keys.get("playerhead").toString());
            if (keys.containsKey("lore")) {
                List<String> lore = replaceColors((List<String>)keys.get("lore"));
                meta.setLore(lore);
            }
            if (keys.containsKey("enchants")) {
                List<String> enchants = (List<String>)keys.get("enchants");
                for (String s : enchants) {
                    String[] parts = s.split(":");
                    if (EnchantUtils.argsToEnchant(parts[0]) == null)
                        continue;
                    if (meta instanceof EnchantmentStorageMeta) {
                        ((EnchantmentStorageMeta)meta).addStoredEnchant(EnchantUtils.argsToEnchant(parts[0]), Integer.parseInt(parts[1]), true);
                        continue;
                    }
                    meta.addEnchant(EnchantUtils.argsToEnchant(parts[0]), Integer.parseInt(parts[1]), true);
                }
            }
            stack.setItemMeta(meta);
            if (keys.containsKey("enchanted")) {
                boolean enchanted = Boolean.valueOf(keys.get("enchanted").toString()).booleanValue();
                if (enchanted)
                    EnchantGlow.addGlow(stack);
            }
            return stack;
        } catch (Exception ignore) {
            Main.getInstance().getLogger().severe(ChatColor.stripColor(ignore.getMessage()));
            return null;
        }
    }

    public static boolean isSimilar(ItemStack item, ItemStack compare) {
        if (item == null || compare == null)
            return false;
        if (item == compare)
            return true;
        if (item.getTypeId() != compare.getTypeId())
            return false;
        if (item.getDurability() != compare.getDurability())
            return false;
        if (item.hasItemMeta() != compare.hasItemMeta())
            return false;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().hasDisplayName() != compare.getItemMeta().hasDisplayName())
                return false;
            if (!item.getItemMeta().getDisplayName().equals(compare.getItemMeta().getDisplayName()))
                return false;
        }
        return true;
    }
}