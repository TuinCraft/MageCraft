package nl.tuincraft.blaatz0r.MageCraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.CreatureType;
public class Util {

	public static List<CreatureType> getFriendlyCreatureTypes() {
		List<CreatureType> l = new ArrayList<CreatureType>();
		
		l.add(CreatureType.CHICKEN);
		l.add(CreatureType.COW);
		l.add(CreatureType.SHEEP);
		l.add(CreatureType.PIG);
		l.add(CreatureType.WOLF);
		
		return l;
	}
	
	public static CreatureType getRandomFriendlyCreatureType() {
		List<CreatureType> l = getFriendlyCreatureTypes();
		Random r = new Random();
		return l.get(r.nextInt(l.size()));
	}
	
	public static HashSet<Block> getBlocksInRadius(Block b, int radius) {
		HashSet<Block> res = new HashSet<Block>();
		Block a = b.getRelative(BlockFace.WEST, (int)(radius/2)).getRelative(BlockFace.NORTH, (int)(radius/2));
		
		for (int i = 0; i < 2*radius; i++) {
			for (int j = 0; j < 2*radius; j++) {
				res.add(a);
				a = a.getRelative(BlockFace.EAST);
			}
			a = a.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST,2*radius);
		}
		
		return res;
	}
}
