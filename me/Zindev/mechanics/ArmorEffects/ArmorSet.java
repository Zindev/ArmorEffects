package me.Zindev.mechanics.ArmorEffects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public enum ArmorSet {
	LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
			Material.LEATHER_BOOTS), CHAIN(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
					Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS), IRON(Material.IRON_HELMET,
							Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS), GOLD(
									Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS,
									Material.GOLD_BOOTS), DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
											Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)

	;
	private final Material helm, chest, leggs, boots;

	private ArmorSet(Material helm, Material chest, Material leggs, Material boots) {
		this.helm = helm;
		this.chest = chest;
		this.leggs = leggs;
		this.boots = boots;
	}

	public Material[] asArray() {
		return new Material[] { getBoots(), getLeggings(), getChestplate(), getHelmet() };
	}

	public Material getHelmet() {
		return helm;
	}

	public Material getChestplate() {
		return chest;
	}

	public Material getLeggings() {
		return leggs;
	}

	public Material getBoots() {
		return boots;
	}

	public static boolean hasFullSet(Player p){
		PlayerInventory i = p.getInventory();
		List<Material> pset = Arrays.asList(i.getBoots(), i.getLeggings(), i.getChestplate(), i.getHelmet()).stream()
				.filter(o -> o != null).map(o -> o.getType()).collect(Collectors.toList());
		return pset.size() >= 4;
	}
	
	public boolean isWearing(Player p) {
		if (!p.isOnline())
			return false;
		PlayerInventory i = p.getInventory();
		List<Material> pset = new ArrayList<>(Arrays.asList(i.getBoots(), i.getLeggings(), i.getChestplate(), i.getHelmet()).stream()
				.filter(o -> o != null).map(o -> o.getType()).collect(Collectors.toList()));
		if(pset.size() < 4)return false;
		List<Material> self = new ArrayList<>(Arrays.asList(asArray()));
		self.retainAll(pset);
		return self.size() >= 4;
	}
	
}
