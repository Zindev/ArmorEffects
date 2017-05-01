package me.Zindev.mechanics.ArmorEffects;

import java.util.HashMap;

import org.bukkit.entity.Player;

class ArmorMap {
	
	private HashMap<Player, ArmorSet> armors;
	
	ArmorMap(){
		this.armors = new HashMap<>();
	}
	public void put(Player p,ArmorSet set){
		armors.put(p, set);
	}
	public ArmorSet remove(Player p){
		return armors.remove(p);
	}
	
	public ArmorSet get(Player p){
		return armors.get(p);
	}
	
	public ArmorSet compute(Player p){
		if(!ArmorSet.hasFullSet(p))return null;
		for(ArmorSet set : ArmorSet.values())
			if(set.isWearing(p))return set;
		return null;
	}

	public WrappedArmorSet computeAndManage(Player p){
		
		ArmorSet raw = compute(p);
		WrappedArmorSet set = new WrappedArmorSet(raw,raw ==null);
		if(!set.isGarbage()){
			put(p, set.set);
		}
		else{
			raw = remove(p);
			set.set = raw;
			set.garbage = raw !=null;
		}
		return set;
	}
	
	
	public HashMap<Player, ArmorSet> getRaw(){
		return armors;
	}
	static class WrappedArmorSet{
		private ArmorSet set;
		private boolean garbage;
		public WrappedArmorSet(ArmorSet set, boolean garbage) {
			this.set = set;
			this.garbage = garbage;
		}
		public ArmorSet getSet() {
			return set;
		}
		public boolean isGarbage() {
			return garbage;
		}
		
		
	}
	
	
	
}
