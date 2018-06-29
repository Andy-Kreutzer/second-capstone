package com.techelevator.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Park;
import com.techelevator.Reservation;
import com.techelevator.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<Reservation> checkReservation() {
		// TODO Auto-generated method stub
		return null;
	}

	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation;
		reservation = new Reservation();
		reservation.setReservation_id(results.getLong("reservation_id"));
		reservation.setSite_id(results.getLong("site_id"));
		reservation.setName(results.getString("name"));
		if (results.getDate("from_date") != null) {
			reservation.setFrom_date((results.getDate("from_date").toLocalDate()));
		}
		if (results.getDate("to_date") != null) {
			reservation.setTo_date((results.getDate("to_date").toLocalDate()));
		}
		if (results.getDate("create_date") != null) {
			reservation.setCreate_date((results.getDate("create_date").toLocalDate()));
		}
		return reservation;
	}
	
}
