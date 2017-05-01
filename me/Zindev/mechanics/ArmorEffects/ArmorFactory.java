package me.Zindev.mechanics.ArmorEffects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zindev.mechanics.ArmorEffects.ArmorMap.WrappedArmorSet;
import me.Zindev.mechanics.ArmorEffects.SetAction.ChainSetAction;
import me.Zindev.mechanics.ArmorEffects.SetAction.DiamondSetAction;
import me.Zindev.mechanics.ArmorEffects.SetAction.GoldenSetAction;
import me.Zindev.mechanics.ArmorEffects.SetAction.IronSetAction;
import me.Zindev.mechanics.ArmorEffects.SetAction.LeatherSetAction;

public class ArmorFactory {

	private JavaPlugin jp;
	private Listener listener;
	private ArmorMap map;
	HashMap<ArmorSet, SetAction> actionMap;
	private Integer taskId;


	ArmorFactory(JavaPlugin jp) {
		this.jp = jp;
		map = new ArmorMap();
		actionMap = new HashMap<>(
				Arrays.asList(new LeatherSetAction(), new IronSetAction(), new ChainSetAction(), new GoldenSetAction(),
						new DiamondSetAction()).stream().collect(Collectors.toMap(SetAction::getType, o -> o)));

	}

	ArmorMap getMap() {
		return map;
	}

	void initialize() {
		if (jp != null) {
			jp.getLogger().info("Initializing Armor Factory");
			taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(jp, new Runnable() {

				@Override
				public void run() {

					for (Player p : Bukkit.getOnlinePlayers().stream()
							.filter(p -> !p.getGameMode().equals(GameMode.CREATIVE)).toArray(i -> new Player[i])) {
						WrappedArmorSet set = map.computeAndManage(p);
						ArmorSet raw = set.getSet();
						if (set.isGarbage() && raw != null) {
							Optional.ofNullable(actionMap.get(raw)).ifPresent(o -> o.clearEffects(p));
						} else if (!set.isGarbage()) {
							Optional.ofNullable(actionMap.get(raw)).ifPresent(o -> o.whileEffects(p));
						}
					}

				}
			}, 20L, 20l);
			listener = new Listener() {

				@EventHandler
				public void onDamage(EntityDamageByEntityEvent e) {
					SetAction.sendEvent(ArmorFactory.this, e);
				}
			};
			Bukkit.getPluginManager().registerEvents(listener, jp);
			jp.getLogger().info("Initalization successfull");
		}
	}

	void uninitialize() {
		if (jp != null) {
			jp.getLogger().info("Uninitializing Armor Factory");
			if (taskId != null)
				Bukkit.getScheduler().cancelTask(taskId);

			if (listener != null)
				HandlerList.unregisterAll(listener);
			Bukkit.getOnlinePlayers().stream().filter(p -> map.getRaw().containsKey(p))
					.collect(Collectors.toMap(p -> p, p -> map.get(p)))
					.forEach((k, v) -> Optional.ofNullable(actionMap.get(v)).ifPresent(o -> o.clearEffects(k)));

			jp.getLogger().info("Uninitalization successfull");
		}

	}
}
