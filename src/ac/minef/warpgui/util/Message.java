package ac.minef.warpgui.util;

import ac.minef.warpgui.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {
    public static void sendMessage(CommandSender p, MessagePart str) {
        if (str.getPart().isEmpty())
            return;
        p.sendMessage(String.valueOf(Main.getInstance().getPrefix()) + str.getPart());
    }

    public static void sendMessage(CommandSender p, String str) {
        MessagePart strPart = generate(str);
        if (strPart.getPart().isEmpty())
            return;
        p.sendMessage(String.valueOf(Main.getInstance().getPrefix()) + strPart.getPart());
    }

    public static void sendErrorMessage(CommandSender p, MessagePart str) {
        if (str.getPart().isEmpty())
            return;
        p.sendMessage(String.valueOf(Main.getInstance().getErrorPrefix()) + str.getPart());
    }

    public static void sendErrorMessage(CommandSender p, String str) {
        MessagePart strPart = generate(str);
        if (strPart.getPart().isEmpty())
            return;
        p.sendMessage(String.valueOf(Main.getInstance().getErrorPrefix()) + strPart.getPart());
    }

    public static MessagePart generate(String str) {
        return new MessagePart(str);
    }

    public static class MessagePart {
        public String str;

        public MessagePart(String str) {
            this.str = Main.getInstance().getMessages().isString(str.toUpperCase()) ? ChatColor.translateAlternateColorCodes('&', Main.getInstance().getMessages().getString(str.toUpperCase())) : "";
        }

        public MessagePart replace(Object placeholder, Object key) {
            this.str = this.str.replace(placeholder.toString(), key.toString());
            return this;
        }

        public String getPart() {
            return this.str;
        }
    }
}