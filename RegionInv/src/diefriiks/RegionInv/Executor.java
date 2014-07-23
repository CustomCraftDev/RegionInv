package diefriiks.RegionInv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Executor {
	
	private Connection connection;
	private FileConfiguration config;
	private boolean debug;
	
	public Executor(Connection connection, FileConfiguration config, boolean debug) {
		this.connection = connection;
		this.config = config;
		this.debug = debug;
	}
	

	public String toggleregion(boolean use_mysql, String regionname) {

		return null;
	}
	
	public boolean gettoggleregion(){
		
		return false;
	}


	public String toggleinv(boolean use_mysql, String p, boolean status) {

		return null;
	}

	public boolean gettoggleinv(){
		
		return false;
	}
	
    public ResultSet getmysql(String query){
    	try {
            Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery(query);
	    	return res;
        } catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
    	return null;
    }
    
    public void setmysql(String query){
 	   	try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
        } catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
    }


	public boolean checktoggleinv(boolean use_mysql, Player p) {

		return false;
	}
	
}

/*
if (getConfig().contains("region." + args[0])) {
getConfig().set("region." + args[0], null);
saveConfig();

}
else {
getConfig().set("region." + args[0], "");
saveConfig();

}
*/