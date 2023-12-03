package io.github.lumine1909;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class InvTickTimings extends JavaPlugin {
    public static JavaPlugin plugin;
    private static final List<String> tabs = List.of("reset", "get");
    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new TickTimeHandler(), this);
        Bukkit.getOnlinePlayers().forEach(p -> {
            EntityPlayer.lastTickMap.put(p, 0L);
            new TickInfo(p);
        });
        TickInfo.reset(Bukkit.getConsoleSender());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return true;
        }
        if (args[0].equals("reset")) {
            TickInfo.reset(sender);
        } else if (args[0].equals("get")) {
            TickInfo.print(sender);
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length < 2) {
            return tabs;
        } else {
            return Collections.emptyList();
        }
    }
}