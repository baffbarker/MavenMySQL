package com.caveofprogramming.db.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

public class UserDaoImplTest {
	
	private Connection conn;
	private List<User> users;
	
	private static final int NUM_TEST_USERS = 1000;
	
	private List<User> loadUsers() throws IOException {
		
		//@formatter:off
		
		return Files
			.lines(Paths.get("../greatexpectations.txt"))
			.map(line -> line.split("[^A-Za-z]"))
			.map(Arrays::asList)
			.flatMap(List -> List.stream())
			.filter(word -> word.length() > 3 & word.length() < 20)
			.map(word -> new User(word))
			.limit(NUM_TEST_USERS)
			.collect(Collectors.toList());
		
		//@formatter:on
		
	}	
		

	@Before
	public void setUp() throws SQLException, IOException {
		
		users = loadUsers();

		System.out.println(users);
		System.out.println(users.size());
		
		var props = Profile.getProperties("db");
		
		var db = Database.instance();
		
		db.connect(props);
		
		conn = db.getConnection();
		conn.setAutoCommit(false);
		
	}
	
	@After
	public void tearDown() throws SQLException {
		Database.instance().close();
	}
	
	
	
	@Test
	public void testSave() throws SQLException {
		
		User user = new User("Jupiter");
		
		UserDao userDao = new UserDaoImpl();
		
		userDao.save(user);
		
		var stmt = conn.createStatement();
		
		var rs = stmt.executeQuery("select id, name from user order by id desc");
		
		var result = rs.next();
		
		assertTrue("cannot retrieve inserted user", result);
		
		var name = rs.getString("name");
		
		assertEquals("user name doesn't match retrieved", user.getName(), name);
		
		stmt.close();
		
		
		
		
	}
	
}
