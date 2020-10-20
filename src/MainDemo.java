import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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

				int startH = e.getStartTimeHours();
				int startM = e.getStartTimeMinutes();
				System.out.print("Start h:" + startH + "   StartM:" + startM);

				int endH = e.getEndTimeHours();
				int endM = e.getEndTimeMinutes();
				System.out.println("       End h:" + endH + "   EndM:" + endM);
				System.out.println("Day of week"+e.getDayOfWeek());

				String line = "Day:" + day + " Month:" + month + " Year:" + year + " " + date + "  Start:" + startTime
						+ " End:" + endTime + "  " + courseNumber + "  " + vagNumber + "  " + location + "  " + holiday
						+ "   " + type + "\n" + sourceLine + "\n";

				if (e.isValidEntry)
					System.out.println(line);
			}

		}
	}
}
