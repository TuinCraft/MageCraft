package nl.tuincraft.blaatz0r.MageCraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.material.MaterialData;

public class MageCraftEntityListener extends EntityListener  {
	

    private final MageCraft plugin;
    
    
	public MageCraftEntityListener(MageCraft plugin) {
		this.plugin = plugin;
	}
	
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.isCancelled()) return;
		Entity e = event.getEntity();
		
		if (e instanceof LivingEntity) {
			
			if (e instanceof Enderman) { 
				Enderman end = (Enderman) e;
				if (end.getCarriedMaterial().getItemType() == Material.TNT) {
					end.setCarriedMaterial(new MaterialData(Material.AIR));
					end.getWorld().spawn(end.getLocation(), TNTPrimed.class);
				}
			}
			
			if (event instanceof EntityDamageByEntityEvent) {
				if (event.getCause() == DamageCause.PROJECTILE) {
			
					LivingEntity l = (LivingEntity) e;
				    EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent)event;
					Projectile p = (Projectile) dmgEvent.getDamager();
					
					if (plugin.iceTrapArrows.contains(p)) {
						event.setDamage(0);
						iceTrap(l);
					}
				}
			}

			
		}
		
		/*
		if (e instanceof LivingEntity && event.getCause() == DamageCause.ENTITY_ATTACK) {
			LivingEntity l = (LivingEntity) e;
			
			if (event instanceof EntityDamageByEntityEvent) {
			    EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent)event;
				Entity d = dmgEvent.getDamager();
				
				kill(l,d);
			}
			
			else if (event instanceof EntityDamageByBlockEvent && plugin.getConfig().getBoolean("by-environment")){
				this.kill(l);
			}
		}*/
		
	}
	
	private void iceTrap(LivingEntity l) {
		Location loc = l.getLocation();
		Block b = loc.getBlock();
		List<Block> toIce = new ArrayList<Block>();
		for (int i = -1; i < 3; i++) {
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.NORTH));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.NORTH_EAST));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.NORTH_WEST));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.WEST));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.EAST));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH_EAST));
			toIce.add(b.getRelative(BlockFace.UP, i).getRelative(BlockFace.SOUTH_WEST));
		}
		
		toIce.add(b.getRelative(BlockFace.UP, 2));
		toIce.add(b.getRelative(BlockFace.DOWN, 1));
		
		for (Block block : toIce) {
			if (block.isEmpty()) {
				block.setType(Material.GLASS);				
			}
		
		}
		l.teleport(b.getLocation());
	}

	public void kill(LivingEntity l, Entity d) {
		if(d instanceof LivingEntity) {
			if (d instanceof HumanEntity && plugin.getConfig().getBoolean("by-players")) {
				HumanEntity h = (HumanEntity) d;
				if (plugin.getConfig().getBoolean("all-weapons") || plugin.getConfig().getIntegerList("instagib-weapons").contains(h.getItemInHand().getTypeId()))
						this.kill(l);
			} else if (d instanceof Creature && plugin.getConfig().getBoolean("by-creepers")) {
				this.kill(l);
			}
		} 
	}
	
	public void kill(LivingEntity l) {
		if (l instanceof HumanEntity && plugin.getConfig().getBoolean("kill-players")) {
			l.setHealth(0);	
		} else if (l instanceof Creature && plugin.getConfig().getBoolean("kill-creepers")) {
			l.setHealth(0);	
		}
	}
	
}
