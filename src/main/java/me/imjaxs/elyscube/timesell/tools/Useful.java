package me.imjaxs.elyscube.timesell.tools;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Useful {
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(Useful::colorize).collect(Collectors.toList());
    }

    public static String replace(String string, String key, String value) {
        return string.replace(key, value);
    }

    public static List<String> replace(List<String> strings, String key, String value) {
        return strings.stream().map(string -> replace(string, key, value)).collect(Collectors.toList());
    }
}
