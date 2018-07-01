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

import com.techelevator.jdbc.JDBCSiteDAO;
import model.Site;

public class JDBCSiteDAOIntegrationTest {
	
	private static final Long TEST_SITE_ID = (long) 623;
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO siteDAO;
	
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
		String sqlInsertSite = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "+
				"VALUES (" + TEST_SITE_ID + ", 7, 286, 6, 'true', 0, 'true')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertSite);
		siteDAO = new JDBCSiteDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_sites() throws SQLException {
		Site theSite = getSite(TEST_SITE_ID, (long) 7, 286, 6, true, 0, true);
		
		List<Site> results = siteDAO.getAllSites();
		
		Assert.assertNotNull(results);
		Assert.assertEquals(623, results.size());
		assertSitesAreEqual(theSite, results.get(622));
	}
	
	@Test
	public void get_sites_by_campground_id_and_dates() throws SQLException {
		
	}
	
	private Site getSite(Long site_id, Long campground_id, int site_number, int max_occupancy, boolean accessible, int max_rv_length, boolean utilities) {
		Site theSite = new Site();
		theSite.setSite_id(site_id);;
		theSite.setCampground_id(campground_id);
		theSite.setSite_number(site_number);
		theSite.setMax_occupancy(max_occupancy);
		theSite.setAccessible(accessible);
		theSite.setMax_rv_length(max_rv_length);
		theSite.setUtilities(utilities);
		return theSite;
	}
	
	private void assertSitesAreEqual(Site expected, Site actual) {
		assertEquals(expected.getSite_id(), actual.getSite_id());
		assertEquals(expected.getCampground_id(), actual.getCampground_id());
		assertEquals(expected.getSite_number(), actual.getSite_number());
		assertEquals(expected.getMax_occupancy(), actual.getMax_occupancy());
		assertEquals(expected.isAccessible(), actual.isAccessible());
		assertEquals(expected.getMax_rv_length(), actual.getMax_rv_length());
		assertEquals(expected.isUtilities(), actual.isUtilities());
	}

}
