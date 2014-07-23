package diefriiks.RegionInv;

import java.sql.Connection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionInv extends JavaPlugin {
	
    public WorldGuardPlugin worldguard;
    public Connection connection;
    public FileConfiguration config;
    public Executor exec;
    public Boolean debug;
    public Boolean usemysql;
    
    private Boolean output;
    private String host;
    private String port;
    private String user;
    private String password;
	private String database;

	
	/**
     * on Plugin enable
     */
	public void onEnable() {
		
        worldguard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
    	if(debug){
    		say("worldguard hooked");
    	}
		new EventListener(this, exec);
    	if(debug){
    		say("PlayermoveEvent registered");
    	}
		loadConfig();
    	if(debug){
    		say("Config loaded");
    	}
		exec = new Executor(connection, config, debug);
    	if(debug){
    		say("Executor registered");
    	}
		say("enabled");
	}

	/**
     * on Plugin disable
     */
	public void onDisable() {
		try {
			connection.close();
			saveConfig();
		} catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
		}
		say("disabled");
	}

	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param label
     * @return true or false 
     */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
			Player p = (Player)sender;
			
			// disableregioninv
			if (((cmd.getName().equalsIgnoreCase("disableregioninv")) && (p.hasPermission("regioninv.disable"))) || (p.isOp())) {
				this.setEnabled(false);
				say("disableregion was executed, by " + p.getName());
				return true;
			}
			
			// toggleregion
			if (((cmd.getName().equalsIgnoreCase("toggleregion")) && (p.hasPermission("regioninv.toggle.region"))) || (p.isOp())) {
				if (args.length == 1) {
					if(usemysql){
						p.sendMessage(exec.toggleregion(true, args[0]));
						return true;
					}
					else{
						p.sendMessage(exec.toggleregion(false, args[0]));
						return true;
					}
				}
				else {
					p.sendMessage("[RegionInv] /toggleregion <region>");
					return true;
				}
			}
			
			// toggleinv
			if (((cmd.getName().equalsIgnoreCase("toggleinv")) && (p.hasPermission("regioninv.toggle.inv"))) || (p.isOp())) {
				if (args.length == 1) {
					if(usemysql){
						p.sendMessage(exec.toggleinv(true, args[0], exec.checktoggleinv(true, p)));
						return true;
					}
					else{
						p.sendMessage(exec.toggleinv(false, args[0], exec.checktoggleinv(false, p)));
						return true;
					}
				}
				else {
					p.sendMessage("[RegionInv] /toggleinv <name>");
					return true;
				}
			}	
		}
		
		// nothing to do here \o/
		return false;
	}
	
	private void loadConfig() {
		config = getConfig();
		config.options().copyDefaults(true);
		
		output = config.getBoolean("config.consoleoutput");
		debug = config.getBoolean("config.debug");
		usemysql = config.getBoolean("use_mysql");
    	if(debug){
    		say("Use_mysql: " + usemysql);
    	}
		
			if(usemysql){
				say("Using Mysql to save Data");
				host = config.getString("mysql.host");
				port = config.getString("mysql.port");
				user = config.getString("mysql.user");
				password = config.getString("mysql.password");
				database = config.getString("mysql.database");
				loadMYSQL();
			}	
		saveConfig();
	}
	
    /**
     * open mysql connection
     * @exception mysql exception
     */
	private void loadMYSQL() {
        try {
            MySQL mysql = new MySQL(host, port, database, user, password);
            connection = mysql.connection;
            
            createtable();
        } catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
	}
	
    
    /**
     * create mysql table on first use
     * @query to send
     * @exception mysql exception
     */
    public void createtable(){
 	   	try {
 	   	if(config.getBoolean("use_mysql.first_time")){	
 	   		exec.setmysql("CREATE TABLE `"+ database +"`.`users` (`username` VARCHAR(45) NOT NULL,`toggle` TINYINT NULL,`inv1` VARCHAR(512) NULL,`inv2` VARCHAR(512) NULL,PRIMARY KEY (`username`));CREATE TABLE `"+ database +"`.`regions` (`regionname` VARCHAR(45) NOT NULL,`toggle` TINYINT NULL,PRIMARY KEY (`regionname`));");
 	   		config.set("use_mysql.first_time", false);
 	   	}	
 	   	} catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
    }
   
    /**
     * print to console
     * @param message to print
     */
	private void say(String out) {
		if(output){
			System.out.println("[RegionInv] " + out);
		}
	}
}