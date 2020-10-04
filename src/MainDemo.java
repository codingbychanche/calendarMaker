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
		
		if (args.length>0) {
			path=args[0];
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
				String courseNumber = e.getCourseNumber();
				String vagNumber = e.getVagNumber();
				String location = e.getLocation();
				String holiday = e.getHoliday();
				String type = e.getType();

				String line = date + "  " + courseNumber + "  " + vagNumber + "  " + location + "  " + holiday + "   "
						+ type;

				if (e.isValidEntry)
					System.out.println(line);
			}
		
		}
	}
}
