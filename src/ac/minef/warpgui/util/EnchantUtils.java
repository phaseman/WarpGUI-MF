package ac.minef.warpgui.util;

import org.bukkit.enchantments.Enchantment;

public class EnchantUtils {
    public static Enchantment argsToEnchant(String args) {
        if (args.equalsIgnoreCase("Power"))
            return Enchantment.ARROW_DAMAGE;
        if (args.equalsIgnoreCase("Flame"))
            return Enchantment.ARROW_FIRE;
        if (args.equalsIgnoreCase("Infinity"))
            return Enchantment.ARROW_INFINITE;
        if (args.equalsIgnoreCase("Punch"))
            return Enchantment.ARROW_KNOCKBACK;
        if (args.equalsIgnoreCase("Sharpness"))
            return Enchantment.DAMAGE_ALL;
        if (args.equalsIgnoreCase("BaneofArthropods"))
            return Enchantment.DAMAGE_ARTHROPODS;
        if (args.equalsIgnoreCase("Smite"))
            return Enchantment.DAMAGE_UNDEAD;
        if (args.equalsIgnoreCase("Efficiency"))
            return Enchantment.DIG_SPEED;
        if (args.equalsIgnoreCase("Unbreaking"))
            return Enchantment.DURABILITY;
        if (args.equalsIgnoreCase("Fireaspect"))
            return Enchantment.FIRE_ASPECT;
        if (args.equalsIgnoreCase("Knockback"))
            return Enchantment.KNOCKBACK;
        if (args.equalsIgnoreCase("Fortune"))
            return Enchantment.LOOT_BONUS_BLOCKS;
        if (args.equalsIgnoreCase("Looting"))
            return Enchantment.LOOT_BONUS_MOBS;
        if (args.equalsIgnoreCase("LuckoftheSea"))
            return Enchantment.LUCK;
        if (args.equalsIgnoreCase("Lure"))
            return Enchantment.LURE;
        if (args.equalsIgnoreCase("Respiration"))
            return Enchantment.OXYGEN;
        if (args.equalsIgnoreCase("Protection"))
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        if (args.equalsIgnoreCase("BlastProtection"))
            return Enchantment.PROTECTION_EXPLOSIONS;
        if (args.equalsIgnoreCase("FeatherFalling"))
            return Enchantment.PROTECTION_FALL;
        if (args.equalsIgnoreCase("FireProtection"))
            return Enchantment.PROTECTION_FIRE;
        if (args.equalsIgnoreCase("ProjectileProtection"))
            return Enchantment.PROTECTION_PROJECTILE;
        if (args.equalsIgnoreCase("Silktouch"))
            return Enchantment.SILK_TOUCH;
        if (args.equalsIgnoreCase("Thorns"))
            return Enchantment.THORNS;
        if (args.equalsIgnoreCase("Aqua Affinity"))
            return Enchantment.WATER_WORKER;
        return Enchantment.getByName(args);
    }
}
