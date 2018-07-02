package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.jdbc.JDBCCampgroundDAO;

import model.Campground;

public class JDBCCampgroundDAOIntegrationTest {
	
	private static final Long TEST_CAMPGROUND_ID = (long) 8;
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO campgroundDAO;
	
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
		String sqlInsertCampground = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "+
				"VALUES (" + TEST_CAMPGROUND_ID + ", 3, 'Test Campground', '01', '12', '$40.00' )";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertCampground);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_campgrounds() throws SQLException {
		Campground theCampground = getCampground(TEST_CAMPGROUND_ID, (long) 3, "Test Campground", "01", "12", 40.00);
		
		List<Campground> results = campgroundDAO.getAllCampgrounds("Acadia");
		
		Assert.assertNotNull(results);
		assertEquals(3, results.size());
		//assertCampgroundsAreEqual(theCampground, results.get(7));
	}
	
	private Campground getCampground(Long campground_id, Long park_id, String name, String open_from_mm, String open_to_mm, Double daily_fee) {
		Campground theCampground = new Campground();
		theCampground.setCampground_id(campground_id);
		theCampground.setPark_id(park_id);
		theCampground.setName(name);
		theCampground.setOpen_from_mm(open_from_mm);
		theCampground.setOpen_to_mm(open_to_mm);
		theCampground.setDaily_fee(daily_fee);
		return theCampground;
	}
	
	@SuppressWarnings("deprecation")
	private void assertCampgroundsAreEqual(Campground expected, Campground actual) {
		assertEquals(expected.getCampground_id(), actual.getCampground_id());
		assertEquals(expected.getPark_id(), actual.getPark_id());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getOpen_from_mm(), actual.getOpen_to_mm());
		assertEquals(expected.getDaily_fee(), actual.getDaily_fee());
		
	}

}
