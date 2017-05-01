package me.Zindev.mechanics.ArmorEffects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

abstract class SetAction {

	private static HashMap<ArmorSet, Set<Class<? extends Event>>> events = new HashMap<>();

	static void sendEvent(ArmorFactory af,Event e) {
		for (SetAction sa : events.entrySet().stream().filter(o -> o.getValue().contains(e.getClass()))
				.map(o -> af.actionMap.get(o.getKey())).toArray(i -> new SetAction[i]))
			sa.onEvent(e);

	}

	public final ArmorFactory f() {
		return MainArmor.factoryA();
	}

	public abstract void giveEffects(Player p);

	public abstract void whileEffects(Player p);

	public abstract void clearEffects(Player p);

	public void onEvent(Event event) {
	}

	protected void requestEvent(Class<? extends Event> clazz) {
		events.computeIfAbsent(getType(), v->new HashSet<>()).add(clazz);
	}

	public ArmorSet getType() {
		return this.getClass().getAnnotation(SetType.class).type();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	static @interface SetType {
		ArmorSet type() default ArmorSet.LEATHER;
	}

	private static boolean is18() {
		return Bukkit.getVersion().startsWith("1[.][6-8].*");
	}

	private static void soundAnvil(Player p) {
		p.playSound(p.getLocation(), Sound.valueOf(is18() ? "ANVIL_LAND" : "BLOCK_ANVIL_LAND"), 2f, 1.2f);
	}

	private static LivingEntity damagerToLiving(EntityDamageByEntityEvent e) {
		return e.getDamager() instanceof Projectile
				? (((Projectile) e.getDamager()).getShooter() instanceof LivingEntity
						? (LivingEntity) ((Projectile) e.getDamager()).getShooter() : null)
				: e.getDamager() instanceof LivingEntity ? (LivingEntity) e.getDamager() : null;
	}

	@SetType(type = ArmorSet.LEATHER)
	static class LeatherSetAction extends SetAction {

		public LeatherSetAction() {
			requestEvent(EntityDamageByEntityEvent.class);
		}

		@Override
		public void giveEffects(Player p) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, amplifier()));
		}

		@Override
		public void whileEffects(Player p) {
			if (!p.getActivePotionEffects().stream().filter(o -> o.getType().equals(PotionEffectType.SPEED)).findFirst()
					.isPresent())
				giveEffects(p);
		}

		@Override
		public void clearEffects(Player p) {
			p.removePotionEffect(PotionEffectType.SPEED);
		}

		private static double damageMultiplier() {
			return MainArmor.conf().getDouble("sets.leather.damage-multiply", 1.5);
		}

		private static int amplifier() {
			return MainArmor.conf().getInt("sets.leather.speed-level", 0);
		}

		@Override
		public void onEvent(Event event) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				LivingEntity en = damagerToLiving(e);
				if (en != null && en instanceof Player) {
					Player p = (Player) e.getDamager();
					ArmorSet set = f().getMap().get(p);
					if (set == getType())
						e.setDamage(DamageModifier.BASE, e.getDamage() * damageMultiplier());

				}
			}
		}
	}
	@SetType(type = ArmorSet.IRON)
	static class IronSetAction extends SetAction {

		public IronSetAction() {
			requestEvent(EntityDamageByEntityEvent.class);
		}
		@Override
		public void giveEffects(Player p) {

		}

		@Override
		public void whileEffects(Player p) {

		}

		@Override
		public void clearEffects(Player p) {

		}

		private static double damageMultiplier() {
			return MainArmor.conf().getDouble("sets.iron.damage-multiply", 0.8);
		}

		private static double missChance() {
			return Double.valueOf(MainArmor.conf().getInt("sets.iron.miss-chance", 20)) / 100;
		}

		@Override
		public void onEvent(Event event) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				LivingEntity en = damagerToLiving(e);
				if (en != null && en instanceof Player) {
					Player p = (Player) e.getDamager();
					ArmorSet set = f().getMap().get(p);
					if (set == getType())
						e.setDamage(DamageModifier.BASE, e.getDamage() * damageMultiplier());

				}
				if (e.getEntity() instanceof Player && en != null) {
					Player p = (Player) e.getEntity();

					ArmorSet set = f().getMap().get(p);
					if (set == getType()) {
						if (Math.random() <= missChance()) {
							if (en instanceof Player)
								soundAnvil(((Player) en));
							soundAnvil(p);
							e.setCancelled(true);
						}

					}
				}

			}
		}

	}
	@SetType(type = ArmorSet.CHAIN)
	static class ChainSetAction extends SetAction {
		public ChainSetAction(){
			requestEvent(EntityDamageByEntityEvent.class);
		}
		@Override
		public void giveEffects(Player p) {

		}

		@Override
		public void whileEffects(Player p) {

		}

		@Override
		public void clearEffects(Player p) {

		}

		private static double damageMultiplier() {
			return MainArmor.conf().getDouble("sets.chain.damage-multiply", 1.1);
		}

		private static double missChance() {
			return Double.valueOf(MainArmor.conf().getInt("sets.chain.miss-chance", 30)) / 100;
		}

		@Override
		public void onEvent(Event event) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				LivingEntity en = damagerToLiving(e);
				if (en != null && en instanceof Player) {
					Player p = (Player) e.getDamager();
					ArmorSet set = f().getMap().get(p);
					if (set == getType())
						e.setDamage(DamageModifier.BASE, e.getDamage() * damageMultiplier());

				}
				if (e.getEntity() instanceof Player && en != null) {
					Player p = (Player) e.getEntity();

					ArmorSet set = f().getMap().get(p);
					if (set == getType()) {
						if (Math.random() <= missChance()) {
							if (en instanceof Player)
								soundAnvil(((Player) en));
							soundAnvil(p);
							e.setCancelled(true);
						}

					}
				}

			}
		}
	}
	@SetType(type = ArmorSet.GOLD)
	static class GoldenSetAction extends SetAction {

		@Override
		public void giveEffects(Player p) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, absLoop() * 20, absAmt()), true);
		}

		private static int absAmt() {
			return MainArmor.conf().getInt("sets.golden.absorption.amplifier", 10);
		}

		private static int absLoop() {
			return MainArmor.conf().getInt("sets.golden.absorption-seconds", 10);
		}

		private Date date;

		@Override
		public void whileEffects(Player p) {
			if (date == null || new Date().getTime() >= date.getTime()) {
				date = new Date(new Date().getTime() + absLoop() * 1000);
				giveEffects(p);
			}
		}

		@Override
		public void clearEffects(Player p) {
			p.removePotionEffect(PotionEffectType.ABSORPTION);
		}

	}
	@SetType(type = ArmorSet.DIAMOND)
	static class DiamondSetAction extends SetAction {
		private static final List<PotionEffectType> badEffects = Arrays.asList(PotionEffectType.BLINDNESS,
				PotionEffectType.CONFUSION, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.WEAKNESS,
				PotionEffectType.UNLUCK, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WITHER);

		@Override
		public void giveEffects(Player p) {

		}

		@Override
		public void whileEffects(Player p) {
			badEffects.forEach(o->p.removePotionEffect(o));
		}

		@Override
		public void clearEffects(Player p) {

		}

	}

}
