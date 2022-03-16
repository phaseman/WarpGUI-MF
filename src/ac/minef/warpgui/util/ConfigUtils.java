package ac.minef.warpgui.util;

import ac.minef.warpgui.Main;
import ac.minef.warpgui.particles.Particle;
import ac.minef.warpgui.particles.ParticleEffect;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigUtils {
    public static ConfigUtils instance;

    public static ConfigUtils getInstance() {
        if (instance == null)
            instance = new ConfigUtils();
        return instance;
    }

    public int fit(int slots) {
        if (slots < 10)
            return 1;
        if (slots < 19)
            return 2;
        if (slots < 28)
            return 3;
        if (slots < 37)
            return 4;
        if (slots < 46)
            return 5;
        return 6;
    }

    public void playSound(Player p, String section) {
        String str = "Commands." + section + ".Sound";
        if (!Main.getInstance().getConfig().getBoolean(str + ".enabled"))
            return;
        float volume = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".volume");
        float pitch = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".pitch");
        p.playSound(p.getLocation(), Sounds.valueOf(Main.getInstance().getConfig().getString(String.valueOf(str) + ".name").toUpperCase()).bukkitSound(), volume, pitch);
    }

    public void playWarpSound(Player p, String name) {
        String str = "Options.Sound." + name;
        float volume = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".volume");
        float pitch = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".pitch");
        p.playSound(p.getLocation(), Sounds.valueOf(Main.getInstance().getConfig().getString(String.valueOf(str) + ".name").toUpperCase()).bukkitSound(), volume, pitch);
    }

    public int getSlot(String path) {
        return Main.getInstance().getConfig().getInt(path);
    }

    public void displayCommand(Player p, String section, boolean centered) {
        List<String> message = (List<String>)Main.getInstance().getConfig().getStringList("Commands." + section + ".Message.lines").stream().map(Util::color).collect(Collectors.toList());
        if (centered) {
            message.forEach(line -> ChatCenter.sendCenteredMessage(p, line));
        } else {
            message.forEach(p::sendMessage);
        }
    }

    public void playParticle(Player p, String name) {
        String str = "Options.Particle." + name;
        String particle = String.valueOf(str) + ".particle";
        String enabled = String.valueOf(str) + ".enabled";
        String part = Main.getInstance().getConfig().getString(particle).toUpperCase();
        if (!Main.getInstance().getConfig().getBoolean(enabled))
            return;
        if (part.equalsIgnoreCase("none"))
            return;
        if (part.equalsIgnoreCase("firework") || part.contains(":"))
            return;
        float offsetX = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".offsetX");
        float offsetY = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".offsetY");
        float offsetZ = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".offsetZ");
        float speed = (float)Main.getInstance().getConfig().getDouble(String.valueOf(str) + ".speed");
        int amount = Main.getInstance().getConfig().getInt(String.valueOf(str) + ".amount");
        Particle.create(p.getLocation(), ParticleEffect.valueOf(part), offsetX, offsetY, offsetZ, speed, amount, false);
    }
}