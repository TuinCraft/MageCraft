package nl.tuincraft.blaatz0r.MageCraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Mage implements Role {

	public Player player;
	
	public static final String NAME = "Mage";
	
	public Mage(Player p) {
		this.player = p;
		setInventory();
	}
	
	public void setInventory() {
		PlayerInventory inv = player.getInventory();
		inv.clear();
		for (int i = 0; i <= 8; i++) {
			inv.setItem(i, new ItemStack(Material.BOOK));
		}

	}

}
