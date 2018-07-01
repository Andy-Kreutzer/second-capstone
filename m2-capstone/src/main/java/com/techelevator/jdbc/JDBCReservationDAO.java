package com.techelevator.jdbc;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import model.Park;
import model.Reservation;
import model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public void setReservation(int site_id, String name, LocalDate from_date, LocalDate to_date, LocalDate create_date) {
		String sqlCreateReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date "
									  + "VALUES (?, ?, ?, ?, ?)";
			jdbcTemplate.update(sqlCreateReservation, site_id, name, from_date, to_date, create_date);
	}

	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation;
		reservation = new Reservation();
		reservation.setReservation_id(results.getLong("reservation_id"));
		reservation.setSite_id(results.getInt("site_id"));
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
