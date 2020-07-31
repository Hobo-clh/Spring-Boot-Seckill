package com.clh.seckill.zother;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {



	private static Properties props;
	
	static {
		try {
			InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.yml");
			props = new Properties();
			props.load(in);
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConn() throws Exception {
		String url = "jdbc:mysql://localhost:3306/hobo-seckill?serverTimezone=Asia/Shanghai";
		String username = "root";
		String password = "123456";
		String driver = "com.mysql.cj.jdbc.Driver";

		// String url = props.getProperty("spring.datasource.url");
		// String username = props.getProperty("spring.datasource.username");
		// String password = props.getProperty("spring.datasource.password");
		// String driver = props.getProperty("spring.datasource.driver-class-name");
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}
}
