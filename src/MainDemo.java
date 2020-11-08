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

	static String path;

	public static void main(String args[]) {

		if (args.length > 0) {
			path = args[0];
		} else {
			System.out.println("Not path/ filename.... Abbording.");
			return;
		}

		List<CalendarEntry> calendar = new ArrayList();
		MakeCalendar myCalendar = new MakeCalendar(path);

		calendar = myCalendar.getRawCalendar();

		getEntryForToday(calendar);

		if (myCalendar.hasError()) {
			System.out.println("Error reading:" + myCalendar.getErorrDescription());
		} else {

			System.out.println("Total:" + myCalendar.getTotalNumberOfLinesRead());
			System.out.println("Valid:" + myCalendar.getNumberOfLinesValid());
			System.out.println("Not Valid:" + myCalendar.getNumberOfLinesNotValid());

			for (CalendarEntry e : calendar) {

				if (e.isValidEntry) {
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

					String line = "Day:" + day + " Month:" + month + " Year:" + year + " " + date + "  Start:"
							+ startTime + " End:" + endTime + "  " + courseNumber + "  " + vagNumber + "  " + location
							+ "  " + holiday + "   " + type + "\n" + sourceLine + "\n";

					System.out.println(ConvertUmlaut.toHtml(line));
				}

			}
		}
	}

	/**
	 * Get and show todays entry...
	 */
	public static void getEntryForToday(List<CalendarEntry> calendar) {

		Calendar todaysDate = Calendar.getInstance();
		todaysDate.setTimeInMillis(System.currentTimeMillis());

		// Optain todays date.
		//
		// Month: January=0!
		int day = todaysDate.get(Calendar.DAY_OF_MONTH);
		int month = todaysDate.get(Calendar.MONTH)+1;
		int year = todaysDate.get(Calendar.YEAR);
		System.out.println("Today is:" + day + "." + month + "." + year);

		int result;
		for (CalendarEntry entry : calendar) {

			result = entry.compareThisEntrysDateWith(todaysDate);
			if (result == entry.IS_TODAY) {
				System.out.println("Found");
				System.out.println(entry.getOrgiriginalEntry());
				break;
			}
			if (result==entry.IS_SATURDAY){
				System.out.println("Today is a saturday, have fun!");
				break;
			}
			if (result==entry.IS_SUNDAY){
				System.out.println("Today is a sunday, have fun!");
				break;
			}
			
		}
	}
}
