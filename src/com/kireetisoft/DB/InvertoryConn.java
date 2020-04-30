package com.kireetisoft.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class InvertoryConn {
  
	Connection con;
	public InvertoryConn() {
		
		con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public Connection GetConnection(){
		try {
			con=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=Invertory;SelectMethod=Direct","Parectic","Parectic");
		}catch (Exception e) {
			// TODO: handle exception
		}
		return con;
		
	}
}
