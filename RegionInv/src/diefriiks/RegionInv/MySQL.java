package diefriiks.RegionInv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL{

	public Connection connection;

	public MySQL(String hostname, String port, String database,
			String username, String password) throws SQLException, ClassNotFoundException {

		this.connection = null;
		
		if (connection != null && !connection.isClosed()) {
			// do nothing \o/
		}
		else {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://"
				+ hostname + ":" + port + "/" + database,
				username, password);
		}
	}
}
