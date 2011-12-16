package nl.tuincraft.blaatz0r.MageCraft;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.material.MaterialData;


public class MageCraftPlayerListener extends PlayerListener {

	public MageCraft plugin;
	
	public MageCraftPlayerListener(MageCraft plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		Player p = event.getPlayer();
		
		if (plugin.mages.containsKey(p)) {
			Role r = plugin.mages.get(p);
			if (r instanceof Mage) {
				if (p.getItemInHand().getType() == Material.BOOK) {
					switch(p.getInventory().getHeldItemSlot()) {
						case 0:
							thunder(p);
							break;
						
						case 1:
							fireball(p);
							break;
						
						case 2:
							meteorShower(p);
							break;
							
						case 3:
							iceTrap(p);
							break;
							
						case 4:
							fireTrail(p, 15);
							break;
							
						case 5:
							web(p);
							break;
							
						case 6:
							spawn(p);
							break;
						
						case 7:
							enderTNT(p);
							break;
							
						case 8:
							freeze(p, 5);
							break;
					}
				}
			}	
		}
	}
	
	
	private void spawn(Player p) {
		Block b = p.getTargetBlock(null, 100);
		World w = p.getWorld();
		
		w.spawnCreature(b.getLocation(), Util.getRandomFriendlyCreatureType());
		
		
	}

	private void web(Player p) {
		
		Block b = p.getTargetBlock(null, 100);
		BlockFace bf = b.getFace(p.getLocation().getBlock());

		if (bf == null) {
			bf = BlockFace.UP;
		}
		
		b.getRelative(bf).setType(Material.WEB);
		
	}

	private void fireball(Player p) {
		Location l = p.getLocation();
		Location loc = l.add(l.getDirection().normalize().multiply(3).toLocation(p.getWorld(), l.getYaw(), l.getPitch()))
						.add(0, 1D, 0);
		
		Fireball f = p.getWorld().spawn(loc, Fireball.class);
		f.setIsIncendiary(false);
		f.setShooter(p);
	}

	private void meteorShower(Player p) {
		Block b = p.getTargetBlock(null, 200);
		
		Location l = b.getLocation();
		Location loc = l.add(0, 40D, 0);
		loc.setPitch(90);

		Fireball f1 = p.getWorld().spawn(loc.add(-3,0,-3), Fireball.class);
		Fireball f2 = p.getWorld().spawn(loc.add(3,0,-3), Fireball.class);
		Fireball f3 = p.getWorld().spawn(loc.add(3,0,3), Fireball.class);
		Fireball f4 = p.getWorld().spawn(loc.add(-3,0,3), Fireball.class);
		f1.setShooter(p);
		f2.setShooter(p);
		f3.setShooter(p);
		f4.setShooter(p);
	}
	
	public void thunder(Player p) {
		Block b = p.getTargetBlock(null, 200);
		
		Location l = b.getLocation();
		b.getWorld().strikeLightning(l);
		Block up = b.getRelative(BlockFace.UP);
		if (up.getType() == Material.AIR) {
			up.setType(Material.FIRE);
		}
	}
	
	public void freeze(Player p, int radius) {
		Block t = p.getTargetBlock(null,200);
		
		HashSet<Block> blocks = Util.getBlocksInRadius(t, radius);
		
		for (Block b : blocks) {
			if (b.getType() == Material.WATER) {
				b.setType(Material.ICE);
			} else if (b.getRelative(BlockFace.UP).getType() == Material.AIR) {
				b.getRelative(BlockFace.UP).setType(Material.SNOW);
			}
			
		}
	}
	
	
	public void iceTrap(Player p) {
		Arrow a = p.shootArrow();
		a.setShooter(p);
		a.setVelocity(p.getLocation().getDirection().multiply(4));
		plugin.iceTrapArrows.add(a);
		
	}
	
	public void fireTrail(Player p, int length) {
		List<Block> blocks = p.getLineOfSight(null, length);
		
		for (Block b : blocks) {
			if (b.getLocation().distance(p.getLocation()) > 2)
				fireBelow(b, 10);
		}
	
	}

	public void enderTNT(Player p) {
		Block b = p.getTargetBlock(null, 100);
		World w = p.getWorld();
		
		Enderman e = (Enderman)w.spawnCreature(b.getLocation(), CreatureType.ENDERMAN);
		e.setCarriedMaterial(new MaterialData(Material.TNT));
	}
	
	/**
	 * Set fire on the world surface under a current block
	 * @param b The block under which a fire will be set
	 * @param maxDist The max distance below the block
	 */
	private void fireBelow(Block b, int maxDist) {
		if  (maxDist == 0) {
			return;
		}
		
		else if (!b.getRelative(BlockFace.DOWN).isEmpty()) {
			if (b.isEmpty()) b.setType(Material.FIRE);
		} else {
			fireBelow(b.getRelative(BlockFace.DOWN), maxDist-1);
		}
		
	}
	
}
