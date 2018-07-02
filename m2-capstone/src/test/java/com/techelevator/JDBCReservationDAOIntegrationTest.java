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

import com.techelevator.jdbc.JDBCCampgroundDAO;
import com.techelevator.jdbc.JDBCReservationDAO;
import com.techelevator.jdbc.JDBCSiteDAO;

import model.Reservation;
import model.Site;

public class JDBCReservationDAOIntegrationTest {
	
	private static final String TEST_RESERVATION_NAME = "Smith Test Family";
	private static final String TEST_RESERVATION_NAME2 = "Tech Elevator";
	private static final int TEST_SITE_ID = 1;
	private static final Long TEST_RESERVATION_ID = (long) 61;
	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO reservationDAO;
	

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
		String sqlInsertReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "+
				"VALUES (" + TEST_RESERVATION_ID + ", 1, 'Smith Test Family', '2018-07-01', '2018-07-15', '2018-07-01')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertReservation);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_reservationId() throws SQLException {
		Reservation theReservation = getReservation(TEST_RESERVATION_ID, 1, "Smith Test Family", LocalDate.of(2018, 07, 01), LocalDate.of(2018, 07, 15), LocalDate.of(2018, 07, 01));
		
		List<Reservation> results = reservationDAO.getReservationId(TEST_RESERVATION_NAME);
		
		assertNotNull(results);
		assertEquals(1, results.size());
		assertReservationsAreEqual(theReservation, results.get(0));
		
	}

	private Reservation getReservation(Long reservation_id, int site_id, String name, LocalDate from_date, LocalDate to_date, LocalDate create_date) {
		Reservation theReservation = new Reservation();
		theReservation.setReservation_id(reservation_id);
		theReservation.setSite_id(site_id);
		theReservation.setName(name);
		theReservation.setFrom_date(from_date);
		theReservation.setTo_date(to_date);
		theReservation.setCreate_date(create_date);
		return theReservation;
	}
	
	private void assertReservationsAreEqual(Reservation expected, Reservation actual) {
		assertEquals(expected.getReservation_id(), actual.getReservation_id());
		assertEquals(expected.getSite_id(), actual.getSite_id());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getFrom_date(), actual.getFrom_date());
		assertEquals(expected.getTo_date(), actual.getTo_date());
		assertEquals(expected.getCreate_date(), actual.getCreate_date());
	}

}
