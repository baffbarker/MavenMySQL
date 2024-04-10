package com.caveofprogramming.db.mysql;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		var db = Database.instance();

		try {
			db.connect();
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
