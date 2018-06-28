package com.techelevator;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.jdbc.JDBCCampgroundDAO;
import com.techelevator.jdbc.JDBCParkDAO;
import com.techelevator.menu.Menu;


public class CampgroundCLI {

	
	private static final String MAIN_MENU_OPTION_PARKS = "View Parks";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, 
																	 MAIN_MENU_OPTION_EXIT};
	
	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	
	private static final String PARK_ACADIA = "Acadia";
	private static final String PARK_ARCHES = "Arches";
	private static final String PARK_CUYAHOGA = "Cuyahoga";
	private static final String[] PARK_OPTIONS = new String[] {PARK_ACADIA, PARK_ARCHES, PARK_CUYAHOGA, MENU_OPTION_RETURN_TO_MAIN};
	
	private static final String CAMPGROUND_COMMAND = "View Campgrounds";
	private static final String CAMPGROUND_RESERVATION = "Search for Reservation";
	private static final String CAMPGROUND_RETURN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_OPTIONS = new String[] {CAMPGROUND_COMMAND, CAMPGROUND_RESERVATION, CAMPGROUND_RETURN, MENU_OPTION_RETURN_TO_MAIN};

	private static final String BOOK_RETURN = "Search for Reservation";
	
	private static final String BOOK_COMMAND = "Search for Available Reservations";
	private static final String[] BOOK_OPTIONS = new String[] {CAMPGROUND_COMMAND, BOOK_RETURN};
	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	
	public static void main(String[] args) {
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		//dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
	}
	
	public void run() {
		System.out.println("Select a Park for Further Details");
		while(true) {
			printHeading("National Park Service");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(choice.equals(MAIN_MENU_OPTION_PARKS)) {
				handleSelectAllParks();
			} else if(choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}
	
	private void handleSelectAllParks() {
		List<Park> allParks = parkDAO.getAllParks();
		if(allParks.size() > 0) {
			System.out.println("\n*** Choose a Park ***");
			String choice = (String)menu.getChoiceFromOptions(PARK_OPTIONS);
			//String choice = (String)menu.getChoiceFromParkOptions(allParks);
			if(choice.equals(PARK_ACADIA)) {
				handleAcadia(choice);
			} else if(choice.equals(PARK_ARCHES)) {
				handleArches(choice);
			} else if(choice.equals(PARK_CUYAHOGA)) {
				handleCuyahoga(choice);
			}
		}
	}

	private void handleAcadia(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
		System.out.println("Select a Command");
		String campgroundChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
		if(campgroundChoice.equals(CAMPGROUND_COMMAND)) {
			handleCampgrounds(choice);
		} else if(campgroundChoice.equals(CAMPGROUND_RESERVATION)) {
			handleReservations();
		} 
	}	

	

	private void handleArches(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
		System.out.println("Select a Command");
		String campgroundChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
		if(campgroundChoice.equals(CAMPGROUND_COMMAND)) {
			handleCampgrounds(choice);
		} else if(campgroundChoice.equals(CAMPGROUND_RESERVATION)) {
			handleReservations();
		} 
	}	
	
	
	private void handleCuyahoga(String choice) {
		printHeading("Park Information Screen");
		List<Park> parks = parkDAO.searchParkName(choice);
		listParkInfo(parks);
	}
	
	private void handleCampgrounds(String campgroundChoice) {
		printHeading("All Campgrounds");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(campgroundChoice);
		listCampgrounds(allCampgrounds);
		String campYesNo = (String)menu.getChoiceFromOptions(BOOK_OPTIONS);
		if(campgroundChoice.equals(BOOK_COMMAND)) {
			handleBooking(campgroundChoice);
		} 
	}
	
	private void handleBooking(String campgroundChoice) {
		printHeading("All Campgrounds");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgrounds(campgroundChoice);
		listCampgrounds(allCampgrounds);
		String campgroundNum = getUserInput("Which campground (enter 0 to cancel)?");
		String arrivalDate = getUserInput("Which campground (enter 0 to cancel)?");
		String departureDate = getUserInput("Which campground (enter 0 to cancel)?");
		
		
	}

	private void handleReservations() {
		// TODO Auto-generated method stub
		
	}
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private void listParkInfo(List<Park> parks) {
		System.out.println();
		if(parks.size() > 0) {
			for(Park park : parks) {
				System.out.println(park.getName().toString());
				System.out.println(park.getLocation().toString());
				System.out.println(park.getEstablishedDate().toString());
				System.out.println(park.getArea());
				System.out.println(park.getVisitors());
			}
		}
	}
	
	private void listParks(List<Park> parks) {
		System.out.println();
		if(parks.size() > 0) {
			for(Park park : parks) {
				System.out.println(park.getName());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private void listCampgrounds(List<Campground> campgrounds) {
		System.out.println();
		if(campgrounds.size() > 0) {
			for(Campground camp : campgrounds) {
				System.out.println(camp.getName().toString());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
	
}
