package io.github.lumine1909;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TickTimeHandler implements Listener {
    long lastTickNanos = 0;
    @EventHandler
    public void onTickEnd(ServerTickEndEvent e) {
        TickInfo.playerInfos.forEach((k, v) -> {
            v.addTime(EntityPlayer.lastTickMap.getOrDefault(k, 0L));
        });
        if (lastTickNanos == 0) {
            lastTickNanos = System.nanoTime();
        }
        else {
            long tickNanos = System.nanoTime();
            long tickTimeNanos = tickNanos - lastTickNanos;
            lastTickNanos = tickNanos;
            TickInfo.tick(tickTimeNanos);
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        TickInfo.playerInfos.remove(e.getPlayer());
        EntityPlayer.lastTickMap.remove(e.getPlayer());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new TickInfo(e.getPlayer());
        EntityPlayer.lastTickMap.put(e.getPlayer(), 0L);
    }
}
