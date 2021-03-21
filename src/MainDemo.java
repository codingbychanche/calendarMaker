import java.io.BufferedReader;
import java.io.FileReader;
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

	public static void main(String args[]) {

		// Compare Test
		CalendarEntry a = new CalendarEntry(true, false, "1.1.2029", "UV", "ta9989", "229069", "Karlsruhe", "-", "-",
				"-");
		CalendarEntry b = new CalendarEntry(true, false, "1.1.2020", "UV", "ta9989", "229068", "Karlsruhe", "-", "-",
				"-");

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

		// Load file and parse entries...
		MakeCalendar myCalendar;
		List<CalendarEntry> calendar = new ArrayList<CalendarEntry>();

		if (args.length > 0) {
			myCalendar = new MakeCalendar(args[0]);

			if (myCalendar.hasError()) {
				System.out.println("Abording, error reading:" + myCalendar.getErorrDescription());
				return;
			} else
				calendar = myCalendar.getRawCalendar();
		} else {
			System.out.println("Not path/ filename.... Abbording.");
			return;
		}

		// Search and display todays calendar entry
		getEntryForToday(calendar);

		// Get and display header data and status of the parsing process...
		System.out.println("Kopfzeile:" + myCalendar.getCalendarHeader());
		System.out.println("Bearbeitungsstand:" + myCalendar.getCalendarRevisionDate());
		System.out.println("Bearbeitungsstand:" + myCalendar.getCalendarRevisionTime());
		System.out.println("Total:" + myCalendar.getTotalNumberOfLinesRead());
		System.out.println("Valid:" + myCalendar.getNumberOfLinesValid());
		System.out.println("Not Valid:" + myCalendar.getNumberOfLinesNotValid());

		// Get and display all entries...
		for (CalendarEntry e : calendar) {
			if (e.isValidEntry)
				printCalendarEntry(e);
		}

		// Get and display all entrys matching a certain VAG- number
		String vag="21/3104";
		System.out.println("");
		System.out.println("Zeige:"+vag);
		
		List<CalendarEntry> entrysForASingleCourse = new ArrayList<CalendarEntry>();

		entrysForASingleCourse = myCalendar.getCalenderEntrysMatchingCourseNumber(vag);

		if (entrysForASingleCourse.size()>0)
		for (CalendarEntry e1 : entrysForASingleCourse)
			printCalendarEntry(e1);
		else
			System.out.println("Keine Einträge für diesen Kurs gefunden");
	}

	/**
	 * Prints a single entry of the calendar.
	 * 
	 * @param e
	 *            {@link CalendarEntry}
	 */

	private static void printCalendarEntry(CalendarEntry e) {
		String date = e.getDate();

		String startTime = e.getStartTime();
		String endTime = e.getEndTime();
		String courseNumber = e.getCourseNumber();
		String vagNumber = e.getVagNumber();
		String location = e.getLocation();
		String holiday = e.getHoliday();
		String type = e.getType();
		int day = e.getDay();
		int month = e.getMonth();
		int year = e.getYear();
		String sourceLine = e.getOrgiriginalEntry();

		String line = "Day:" + day + " Month:" + month + " Year:" + year + " " + date + "  Start:" + startTime + " End:"
				+ endTime + "  " + courseNumber + "  " + vagNumber + "  " + location + "  " + holiday + "   " + type
				+ "\n" + sourceLine + "\n";

		System.out.println(ConvertUmlaut.toHtml(line));
	}

	/**
	 * An example of how to get the entry with today's date.
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
