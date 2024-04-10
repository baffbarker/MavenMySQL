package com.caveofprogramming.db.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		Properties props = new Properties();
		
		String propertiesFile = "/config/db.properties";
		try {
			props.load(App.class.getResourceAsStream(propertiesFile));
		} catch (IOException e) {
			System.out.println("Cannot load properties" + propertiesFile);
			return;
		}
		
		var db = Database.instance();

		try {
			db.connect(props);
		} catch (SQLException e) {
			System.out.println("Cannont connect");
			e.printStackTrace();
		}

		System.out.println("Connected");

		UserDao userDao = new UserDaoImpl();

		// userDao.save(new User("Mars"));
		// userDao.save(new User("Saturn"));
		// userDao.save(new User("Pluto"));

		var users = userDao.getAll();
		users.forEach(System.out::println);

		var userOpt = userDao.findByID(9);

		if (userOpt.isPresent()) {
			
			User user = userOpt.get();
			System.out.println("Retrieved: " + user);
			user.setName("Snoopy");
			userDao.update(user);
			
		} else {
			System.out.println("No user");
		}

		userDao.delete(new User(5, null));

		try {
			db.close();
		} catch (SQLException e) {
			System.out.println("Cannot close");
			e.printStackTrace();
		}
	}
}
