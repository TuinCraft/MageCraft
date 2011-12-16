package nl.tuincraft.blaatz0r.MageCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class MageCraft extends JavaPlugin {
	private final MageCraftEntityListener entityListener = new MageCraftEntityListener(this);
	private final MageCraftPlayerListener playerListener = new MageCraftPlayerListener(this);
    public static Logger log;    
    
    public String name;
    public String version;
        
    public HashMap<Player,Role> mages;
    
    public List<Arrow> iceTrapArrows;
	
	public void onDisable() {
        log = Logger.getLogger("Minecraft");
        log.info(name + " " + version + " disabled");
		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			// Class
			if (cmd.getName().equalsIgnoreCase("class")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("mage")) {
						sender.sendMessage("Mage class selected.");
						
						mages.put(p, new Mage(p));
					}
				} else {
					sender.sendMessage("Usage: /class warrior|archer|mage|warlock|healer");
					
				}
			} 
			
			// Help
			else if(cmd.getName().equalsIgnoreCase("help")) {
				sender.sendMessage("1 - Thunderbolt");
				sender.sendMessage("2 - Fireball");
				sender.sendMessage("3 - Meteor");
				sender.sendMessage("4 - Ice Trap");
				sender.sendMessage("5 - Fire Trail");
				sender.sendMessage("6 - Web");
				
			}
			
			
			return true;
		} else {
			return false;
		}
		
	}
	
	public void onEnable() {
		name = this.getDescription().getName();
		version = this.getDescription().getVersion();
		
		mages = new HashMap<Player,Role>();
		iceTrapArrows = new ArrayList<Arrow>();
		
        this.getDataFolder().mkdir();
                
        PluginManager pm = getServer().getPluginManager();        
        //pm.registerEvent(Event.Type.ENTITY_DAMAGEDBY_PROJECTILE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_ANIMATION, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        
        //pm.registerEvent(Event.Type.ENTITY_DAMAGEDBY_BLOCK, entityListener, Priority.Normal, this);
        log = Logger.getLogger("Minecraft");
        log.info(name + " " + version + " enabled");
        
		
	}

	
}
