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

		System.out.println("Total:" + myCalendar.getTotalNumberOfLinesRead());
		System.out.println("Valid:" + myCalendar.getNumberOfLinesValid());
		System.out.println("Not Valid:" + myCalendar.getNumberOfLinesNotValid());

		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());

		if (myCalendar.hasError()) {
			System.out.println("Error reading:" + myCalendar.getErorrDescription());
		} else {
			for (CalendarEntry e : calendar) {

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

				String line = "Day:" + day + " Month:" + month + " Year:" + year + " " + date + "  Start:" + startTime
						+ " End:" + endTime + "  " + courseNumber + "  " + vagNumber + "  " + location + "  " + holiday
						+ "   " + type + "\n" + sourceLine + "\n";

				if (e.compareWith(today) == e.IS_TODAY)
					System.out.println("---------- T O D A Y-----------------");
				if (e.compareWith(today) == e.IS_SATURDAY)
					System.out.println("SATURDAY");
				if (e.compareWith(today) == e.IS_SUNDAY)
					System.out.println("SUNDAY");
				if (e.compareWith(today) == e.IS_NOT_TODAY_OR_WEEKEND)
					System.out.println("nope");

				System.out.println(ConvertUmlaut.toHtml(line));

			}
		}
		for (CalendarEntry ee : calendar) {
			String loc = ee.getLocation();
			System.out.println("-----" + ConvertUmlaut.toHtml(loc));
		}
	}
}
