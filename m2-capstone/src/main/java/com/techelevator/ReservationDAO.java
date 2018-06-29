package com.techelevator;

import java.time.LocalDate;
import java.util.List;


public interface ReservationDAO {
	
	public void setReservation(int site_id, String name, String from_date, String to_date, LocalDate create_date);
	
}
