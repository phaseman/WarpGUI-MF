package ac.minef.warpgui.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EnchantGlow extends EnchantmentWrapper {
    private static Enchantment glow;

    public EnchantGlow(int id) {
        super(id);
    }

    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public int getMaxLevel() {
        return 10;
    }

    public String getName() {
        return "Glow";
    }

    public int getStartLevel() {
        return 1;
    }

    public static Enchantment getGlow() {
        try {
            if (glow != null)
                return glow;
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set((Object)null, Boolean.valueOf(true));
            Enchantment.registerEnchantment(glow = (Enchantment)new EnchantGlow(255));
            return glow;
        } catch (Exception e) {
            return null;
        }
    }

    public static void addGlow(ItemStack item) {
        try {
            Enchantment glow = getGlow();
            item.addEnchantment(glow, 1);
        } catch (Exception exception) {}
    }
}