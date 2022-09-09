import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.*;

import CalendarMaker.*;

/**
 * Demo
 * 
 * Parses a text- file and converts it into a calendar entry....
 * 
 * @author Berthold
 */

public class MainDemo {

	static String path, path2;
	static boolean compareTwoCalandarsIsPossible = false;

	public static void main(String args[]) {

		// Load file and parse entries...
		MakeCalendar myCalendar = null;
		MakeCalendar mySecondCalendar = null;

		// Check if at least one path was passed and if so, read
		if (args.length > 0) {
			myCalendar = readJobSchedule(args[0]);
		} else {
			System.out.println("No filename... Abording!");
			return;
		}
		if (myCalendar == null) {
			System.out.println("Error reading.....");
			return;
		}

		// If a second path was given, read second calendar...
		// The second file read, second calendar created is
		// used to show how to calendars can be compared using
		// this library.
		if (args.length > 1) {
			mySecondCalendar = readJobSchedule(args[1]);
			compareTwoCalandarsIsPossible = true;
		}

		//
		// DEMO
		//
		// Shows how to use the library
		//
		// Search and display todays calendar entry
		//
		if (compareTwoCalandarsIsPossible) {
			System.out.println("Zwei Kalender vergleichen:");
			System.out.println("Erster Kalender:");
			System.out.println(myCalendar.getCalendarHeader());
			System.out.println();
			System.out.println("Zweiter Kalender:");
			System.out.println(mySecondCalendar.getCalendarHeader());

			List<CalendarEntry> comparedList = new ArrayList<>();

			comparedList = myCalendar.compareThisCalWith(mySecondCalendar);

			System.out.println("Result has:" + comparedList.size());

			for (CalendarEntry e : comparedList) {
				// Show only the entries which differ.....
				if (e.dateHasChanged() || e.startTimeHasChanged() || e.endTimeHasChanged() || e.vagNumberHasChanged()
						|| e.courseNumberHasChanged() || e.locationHasChanged()) {
					System.out.print(e.getDate() + " Änderungen:");
					if (e.dateHasChanged())
						System.out.print("Date has changed  ");
					if (e.startTimeHasChanged())
						System.out.print("Start time changed  ");
					if (e.endTimeHasChanged())
						System.out.print("End time changed  ");
					if (e.vagNumberHasChanged())
						System.out.print("VAG changed  ");
					if (e.courseNumberHasChanged())
						System.out.print("Course number changed  ");
					if (e.locationHasChanged())
						System.out.print("Location changed  ");
					System.out.println();
				}
			}
		}

		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("Heute:");
		getEntryForToday(myCalendar.getRawCalendar());
		System.out.println("-------------------------------------------------------------------------------");

		//
		// Get and display header data and status of the parsing process...
		//
		System.out.println("Allgemeines:");
		System.out.println("Kopfzeile:" + myCalendar.getCalendarHeader());
		System.out.println("Bearbeitungsstand:" + myCalendar.getCalendarRevisionDate());
		System.out.println("Bearbeitungsstand:" + myCalendar.getCalendarRevisionTime());
		System.out.println("Total:" + myCalendar.getTotalNumberOfLinesRead());
		System.out.println("Valid:" + myCalendar.getNumberOfLinesValid());
		System.out.println("Not Valid:" + myCalendar.getNumberOfLinesNotValid());
		System.out.println("-------------------------------------------------------------------------------");
		
		//
		// Get and display all entries...
		//
		System.out.println("Alle Einträge:");
		for (CalendarEntry e : myCalendar.getRawCalendar()) {
			if (e.isValidEntry)
				printCalendarEntry(e);
		}
		System.out.println("-------------------------------------------------------------------------------");

		//
		// Get and display all entries matching a certain VAG- number
		//
		String vag = "21/22416";
		System.out.println("");
		System.out.println("Alle zu VAG:" + vag + " passenden Einträge");

		List<CalendarEntry> entrysForASingleCourse = new ArrayList<CalendarEntry>();
		entrysForASingleCourse = myCalendar.getCalenderEntrysMatchingCourseNumber(vag);

		if (entrysForASingleCourse.size() > 0)
			for (CalendarEntry e1 : entrysForASingleCourse)
				printCalendarEntry(e1);
		else
			System.out.println("Keine Einträge für diesen Kurs gefunden");

		System.out.println("-------------------------------------------------------------------------------");

		//
		// Show a list of all VAG- numbers
		//
		System.out.println("Alle VAG- Nummern:");
		List<String> vagNumbers = new ArrayList<>();
		vagNumbers = myCalendar.getListOfAllVAGNumbers("Ta9989");

		for (String v : vagNumbers)
			System.out.println("VAG # " + v);

		System.out.println("-------------------------------------------------------------------------------");

		//
		// Show a list of all course numbers.
		//
		System.out.println("Alle Kursnummern:");
		List<String> courseNumbers = new ArrayList<>();
		courseNumbers = myCalendar.getListOfAllCourseNumbers("");

		for (String v : courseNumbers)
			System.out.println("Course # " + v);

		System.out.println("-------------------------------------------------------------------------------");

		//
		// Show a list of all courses, sorted by VAG- number
		//
		System.out.println("Alle Lehrgänge, sortiert nach VAG- Nummer:");
		List<String> courses = new ArrayList<>();
		courses = myCalendar.getCourseList();

		for (String cc : courses)
			System.out.println(cc);

		System.out.println("-------------------------------------------------------------------------------");

		//
		// Check if given search criteria is a valid VAG- number
		//
		System.out.println("Test ob ein bestimmtes Muster zutrifft:");
		String vagToTest = "45/234544";
		if (myCalendar.checkIfRegexPatternMatches(myCalendar.vagNumberPattern, vagToTest))
			System.out.println(vagToTest + " is VAG");
		else
			System.out.println(vagToTest + " is not a valid VAG");

		System.out.println("-------------------------------------------------------------------------------");

		//
		// Compare Test
		// boolean isValidEntry, boolean isHoliday, String date, String time,
		// String type, String courseNumber, String vagNumber, String location, String
		// holiday,
		// String orgiriginalEntry
		//
		System.out.println("Vergleich:");
		CalendarEntry a = new CalendarEntry(true, false, "1.1.2029", "6:30", "UV", "BÜ221", "213456", "Karlsruhe", "b",
				"c","d");
		CalendarEntry b = new CalendarEntry(true, false, "1.1.2029", "6:31", "UV", "BÜ222", "213453", "Karlsruhe", "b",
				"c","d");

		a.compareThisCalendarEntryWith(b);

		if (a.dateHasChanged())
			System.out.println("Date has changed");
		if (a.startTimeHasChanged())
			System.out.println("Start time changed");
		if (a.endTimeHasChanged())
			System.out.println("End time changed");
		if (a.vagNumberHasChanged())
			System.out.println("VAG changed");
		if (a.courseNumberHasChanged())
			System.out.println("Course number changed");
		if (a.locationHasChanged())
			System.out.println("Location changed");

		System.out.println("-------------------------------END OF DEMO-------------------------------------");

	}

	/**
	 * Prints a single entry of the calendar.
	 * 
	 * @param e {@link CalendarEntry}
	 */
	private static void printCalendarEntry(CalendarEntry e) {
		String date = e.getDate();

		String startTime = e.getStartTime();
		String endTime = e.getEndTime();
		String courseNumber = e.getCourseNumber();
		String vagNumber = e.getVagNumber();
		String location = e.getLocation();
		String holiday = e.getHoliday();
		String travelDay=e.getTravelDay();
		String type = e.getType();
		int day = e.getDay();
		int month = e.getMonth();
		int year = e.getYear();
		String sourceLine = e.getOrgiriginalEntry();

		String line = "Day:" + day + " Month:" + month + " Year:" + year + " " + date + "  Start:" + startTime + " End:"
				+ endTime + "  " + courseNumber + "  " + vagNumber + "  " + location + " Reise:"+travelDay+"    "+holiday + "   " + type
				+ "\n" + sourceLine + "\n";

		System.out.println(ConvertUmlaut.toHtml(line));
	}

	/**
	 * Creates a new job schedule.
	 * 
	 * @param path Input file.
	 * @return Either a {@link MakeCalendar} object or 'null' if an IO- error
	 *         occurred.
	 */
	private static MakeCalendar readJobSchedule(String path) {

		MakeCalendar cal = null;
		try {
			InputStream inputStream;

			inputStream = new FileInputStream(path);
			cal = new MakeCalendar(inputStream);

			return cal;

		} catch (IOException e) {
			System.out.println(e.toString());
			return cal;
		}
	}

	/**
	 * An example on how to get the entry with today's date.
	 */
	public static void getEntryForToday(List<CalendarEntry> calendar) {

		Calendar todaysDate = Calendar.getInstance();
		todaysDate.setTimeInMillis(System.currentTimeMillis());

		// Optain todays date.
		//
		// Month: January=0!
		int day = todaysDate.get(Calendar.DAY_OF_MONTH);
		int month = todaysDate.get(Calendar.MONTH) + 1;
		int year = todaysDate.get(Calendar.YEAR);
		System.out.println("Today is:" + day + "." + month + "." + year);

		int result;
		for (CalendarEntry entry : calendar) {

			result = entry.compareThisEntrysDateWith(todaysDate);
			if (result == entry.HAS_SAME_DATE) {
				System.out.println("Found");
				System.out.println(entry.getOrgiriginalEntry());
				break;
			}
			if (result == entry.IS_SATURDAY) {
				System.out.println("Today is a saturday, have fun!");
				break;
			}
			if (result == entry.IS_SUNDAY) {
				System.out.println("Today is a sunday, have fun!");
				break;
			}
		}
	}
}
