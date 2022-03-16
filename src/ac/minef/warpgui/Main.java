package ac.minef.warpgui;

import ac.minef.warpgui.menus.MenuAPI;
import ac.minef.warpgui.util.EnchantGlow;
import com.earth2me.essentials.Essentials;
import ac.minef.warpgui.listeners.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private Essentials ess;

    private File messages;

    private FileConfiguration messagesConfig;

    public void onEnable() {
        setup();
        this.ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new MenuAPI(), (Plugin)this);
        pm.registerEvents((Listener)new CommandListener(), (Plugin)this);
    }

    private void setup() {
        try {
            saveDefaultConfig();
            this.messages = new File(getDataFolder(), "messages.yml");
            if (!this.messages.exists()) {
                this.messages.createNewFile();
                saveResource("messages.yml", true);
            }
            this.messagesConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.messages);
        } catch (Exception exception) {}
    }

    public void onLoad() {
        EnchantGlow.getGlow();
    }

    public Essentials getEss() {
        return this.ess;
    }

    public FileConfiguration getMessages() {
        return this.messagesConfig;
    }

    public void saveMessages() {
        try {
            this.messagesConfig.save(this.messages);
        } catch (Exception exception) {}
    }

    public void reloadMessages() {
        try {
            this.messagesConfig.load(this.messages);
            this.messagesConfig.save(this.messages);
        } catch (Exception exception) {}
    }

    public String getPrefix() {
        return String.valueOf(ChatColor.translateAlternateColorCodes('&', this.messagesConfig.getString("Prefix"))) + " ";
    }

    public String getErrorPrefix() {
        return String.valueOf(ChatColor.translateAlternateColorCodes('&', this.messagesConfig.getString("ErrorPrefix"))) + " ";
    }

    public static Main getInstance() {
        return (Main)JavaPlugin.getPlugin(Main.class);
    }
}