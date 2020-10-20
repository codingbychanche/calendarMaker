package CalendarMaker;

import java.util.Calendar;

/**
 * Data model for one calendar entry.
 * 
 * @author Berthold
 *
 */

public class CalendarEntry {

	public boolean isValidEntry; 
	String date;
	String time;
	String type; // eq,uv etc
	String courseNumber; // taxxxx, büxxxx etc
	String vagNumber; // VAG------
	String location;
	String holiday;
	String orgiriginalEntry; // Complete enry from source file.

	/**
	 * Creates a new entry for a {@link CalendarMaker} instance.
	 * 
	 * @param isValidEntry
	 *            Must have date and course number or vag number.
	 * @param date
	 *            Day....
	 * @param type
	 *            EQ, UV etc...
	 * @param courseNumber
	 *            TA9989, BÜ0221 etc...
	 * @param vagNumber
	 * @param location
	 *            Town etc....
	 * @param holiday
	 *            If the entry was marked accordingly, this is initialized....
	 *            Remark or holidays...
	 * @param orgiriginalEntry
	 *            Unchanged entry from source file.
	 */
	public CalendarEntry(boolean isValidEntry, String date, String time, String type, String courseNumber,
			String vagNumber, String location, String holiday, String orgiriginalEntry) {
		super();

		this.isValidEntry = isValidEntry;

		this.date = date;
		this.time = time;
		this.type = type;
		this.courseNumber = courseNumber;
		this.vagNumber = vagNumber;
		this.location = location;
		this.holiday = holiday;
		this.orgiriginalEntry = orgiriginalEntry;
	}

	public boolean isValidEntry() {
		return isValidEntry;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public String getVagNumber() {
		return vagNumber;
	}

	public String getLocation() {
		return location;
	}

	public String getHoliday() {
		return holiday;
	}

	public String getOrgiriginalEntry() {
		return orgiriginalEntry;
	}

	/**
	 * Extracts day of month from a date string object. Date- string format:
	 * dd.mm.yyyy
	 * 
	 * @return Day of month if date was in the correct format. Returns 0 if not.
	 */
	public int getDay() {

		String[] d = date.split("\\.");

		try {
			if (d.length > 0)
				return Integer.valueOf(d[0]);
			else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Extracts month from a date string object. Date- string format: dd.mm.yyyy
	 * 
	 * @return Month if date was in the correct format. Returns 0 if not.
	 */
	public int getMonth() {
		String[] d = date.split("\\.");

		try {
			if (d.length > 1)
				return Integer.valueOf(d[1]);
			else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Extracts year from a date string object. Date- string format: dd.mm.yyyy
	 * 
	 * @return Year if date was in the correct format. Returns 0 if not.
	 */
	public int getYear() {
		String[] d = date.split("\\.");

		try {
			if (d.length > 2)
				return Integer.valueOf(d[2]);
			else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Asumes that the format of the time string is: |hh.mm|hh.mm|
	 * 
	 * @return Start time of event in: hh.mm
	 */
	public String getStartTime() {

		String t = time.replace("|", " ").trim();
		t = t.replace("_", "");
		String[] tParts = t.split(" ");
		System.out.println(t);

		if (tParts.length > 0)
			return tParts[0];
		else
			return "_";
	}

	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return Start time of he event in full hours. If time format is not
	 *         valid, returns 0.
	 */
	public int getStartTimeHours() {
		String h[] = this.getStartTime().split("\\.");
		String hr;

		try {
			if (h.length > 0) {
				hr = h[0].replace("_", "");
				return Integer.valueOf(hr);
			} else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return Start time of he event in minutes. If time format is not valid,
	 *         returns 0.
	 */
	public int getStartTimeMinutes() {
		String m[] = this.getStartTime().split("\\.");
		String mi;
		try {
			if (m.length > 1) {
				mi = m[1].replace("_", "");
				return Integer.valueOf(mi);
			} else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Asumes that the format of the time string is: |hh.mm|hh.mm|
	 * 
	 * @return End time of Event in hh.mm
	 */
	public String getEndTime() {
		String t = time.replace("|", " ").trim();
		String[] tParts = t.split(" ");

		if (tParts.length == 2)
			return tParts[1];
		else
			return "_";
	}

	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return End time of he event in full hours. If time format is not valid,
	 *         returns 0.
	 */
	public int getEndTimeHours() {
		String h[] = this.getEndTime().split("\\.");
		String hr;

		try {
			if (h.length > 0) {
				hr = h[0].replace("_", "");
				return Integer.valueOf(hr);
			} else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Assumes that the format of the time-string is hh.mm
	 * 
	 * @return End time of he event in minutes. If time format is not valid,
	 *         returns 0.
	 */
	public int getEndTimeMinutes() {
		String m[] = this.getEndTime().split("\\.");
		String mi;

		try {
			if (m.length > 1) {
				mi = m[1].replace("_", "");
				return Integer.valueOf(mi);
			} else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * This events date in milliseconds. Precision: Day (=0:00 until 24:00h)
	 * 
	 * @return This events date in milliseconds.
	 */
	public long getEventTimeInMillisec() {

        int year = this.getYear();
        int month = this.getMonth();
        int day = this.getDay();

        Calendar currentEventTime=Calendar.getInstance();
        currentEventTime.set(year+2000, month-1, day,0, 23);
        return currentEventTime.getTimeInMillis();
	}
	/**
	 * Day of week.
	 * 
	 * @return Integer idicating the day of week.
	 */
	public int getDayOfWeekForThisDate(){
		int year = this.getYear();
        int month = this.getMonth();
        int day = this.getDay();
        
		Calendar currentEventTime=Calendar.getInstance();
        currentEventTime.set(year+2000, month-1, day);
        
        return currentEventTime.get(Calendar.DAY_OF_WEEK);
	}
}
