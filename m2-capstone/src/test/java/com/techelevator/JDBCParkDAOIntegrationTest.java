package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.jdbc.JDBCParkDAO;

public class JDBCParkDAOIntegrationTest {
	
	private static final String TEST_PARK_NAME = "Test Park";
	private static final Long TEST_PARK_ID = (long) 4;
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO parkDAO;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) "+
				"VALUES (" + TEST_PARK_ID + ", 'Test Park', 'Columbus', '2018-01-01', 50000, 1000000, 'This is a test park.')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		parkDAO = new JDBCParkDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_parks() throws SQLException {
		Park thePark = getPark(TEST_PARK_ID, "Test Park", "Columbus", LocalDate.of(2018, 01, 01), 50000, 1000000, "This is a test park.");
		
		List<Park> results = parkDAO.getAllParks();
		
		Assert.assertNotNull(results);
		Assert.assertEquals(4, results.size());
		assertParksAreEqual(thePark, results.get(3));
	}
	
	@Test
	public void search_park_name() throws SQLException {
		Park thePark = getPark(TEST_PARK_ID, "Test Park", "Columbus", LocalDate.of(2018, 01, 01), 50000, 1000000, "This is a test park.");
		
		List<Park> results = parkDAO.searchParkName(TEST_PARK_NAME);
		
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		assertParksAreEqual(thePark, results.get(0));
	}
	
	private Park getPark(Long park_id, String name, String location, LocalDate established_date, int area, int visitors, String description) {
		Park thePark = new Park();
		thePark.setId(park_id);
		thePark.setName(name);
		thePark.setLocation(location);
		thePark.setEstablishedDate(established_date);
		thePark.setArea(area);
		thePark.setVisitors(visitors);
		thePark.setDescription(description);
		return thePark;
	}
	
	private void assertParksAreEqual(Park expected, Park actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getLocation(), actual.getLocation());
		assertEquals(expected.getEstablishedDate(), actual.getEstablishedDate());
		assertEquals(expected.getArea(), actual.getArea());
		assertEquals(expected.getVisitors(), actual.getVisitors());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

}
