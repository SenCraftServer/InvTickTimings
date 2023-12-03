package io.github.lumine1909;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class TickInfo {
    public static Map<Player, TickInfo> playerInfos = new HashMap<>();
    public static long totalTime;
    Player player;
    long totalUsed = 0, currentUsed = 0;
    double totalRate = 0.0, currentRate = 0.0;
    public TickInfo(Player player) {
        this.player = player;
        playerInfos.put(player, this);
    }
    public static void reset(CommandSender sender) {
        totalTime = 0;
        playerInfos.forEach((k, v) -> {
            v.totalUsed = v.currentUsed = 0;
            v.totalRate = v.currentRate = 0.0;
        });
        sender.sendMessage(AQUA + "[InvTickTimings] 物品栏计时已重置");
    }
    public static void tick(long tickTime) {
        totalTime += tickTime;
        playerInfos.forEach((k, v) -> v.tickChildren(tickTime));
    }
    public static void print(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(InvTickTimings.plugin, () -> {
            sender.sendMessage(AQUA + "[InvTickTimings] 玩家背包tick占用时间如下:");
            sender.sendMessage(YELLOW + "玩家名称        当前刻占用        当前刻占比        总占比");
            List<TickInfo> info = new ArrayList<>(playerInfos.size() + 1);
            info.addAll(playerInfos.values());
            info.sort((t1, t2) -> {
                if (t1.currentUsed == t2.currentUsed) {
                    if (t1.totalRate <= t2.totalRate) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return t1.currentUsed > t2.currentUsed ? -1 : 1;
                }
            });
            info.forEach(i -> {
                StringBuilder builder = new StringBuilder(String.valueOf(GREEN));
                String name = i.player.getName();
                builder.append(name);
                int l = 18 - name.length();
                builder.append(" ".repeat(Math.max(0, l)));
                builder.append(String.format("%.2f", (i.currentUsed / 1000000.0))).append("ms            ").append(String.format("%.2f", i.currentRate * 100.0)).append("%             ").append(String.format("%.2f", i.totalRate * 100.0)).append("%");
                sender.sendMessage(builder.toString());
            });
        });
    }
    public void addTime(long timeUsed) {
        currentUsed = timeUsed;
        totalUsed += currentUsed;
    }
    public void tickChildren(long currentTime) {
        totalRate = totalUsed * 1.0 / totalTime;
        currentRate = currentUsed * 1.0 / currentTime;
    }
}
