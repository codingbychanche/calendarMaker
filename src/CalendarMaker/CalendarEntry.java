package CalendarMaker;

import java.util.Calendar;
import java.util.Date;

/**
 * Data model for one calendar entry.
 * 
 * @author Berthold
 *
 */
public class CalendarEntry {

	// When Comparing this entry to a given date, these fields represent the
	// result
	public static final int IS_SATURDAY = 2;
	public static final int IS_SUNDAY = 1;
	public static final int HAS_SAME_DATE = 3;
	public static final int IS_NOT_TODAY_OR_WEEKEND = 4;

	// General flags marking the properties of this entry.
	public boolean isValidEntry;
	public boolean isWeekend;
	public boolean isHoliday;
	public boolean isAnotherEventAtSameDay;

	// These two fields are needed when comparing two calendars, to detect
	// dates which have more than one event taking place at the same date.
	//
	// The method comparing two calendar files would detect differences between
	// such entrys because it compares one date against the same date twice.
	//
	public boolean isChildOfAnotherEntry; // For entrys with more than one event....
	public boolean hasAlreadyBeenComparedToAnotherEntry; // For entrys with more than one event.

	// When comparing this entry these fields are used to describe what has
	// changed....
	private boolean entryHasChanged, dateHasChanged, startTimeHasChanged, endTimeHasChanged, vagNumberHasChanged,
			courseNumberHasChanged, locationHasChanged;

	String date;
	String time;
	String type; // eq,uv etc
	String courseNumber; // taxxxx, büxxxx etc
	String vagNumber; // VAG------
	String location;
	String holiday;
	String travelDay;
	String orgiriginalEntry; // Complete entry from source file.

	/**
	 * Creates a new entry for a {@link CalendarMaker} instance.
	 * 
	 * @param isValidEntry     Must have date and course number or vag number.
	 * @param date             Day....
	 * @param type             EQ, UV etc...
	 * @param courseNumber     TA9989, BÜ0221 etc...
	 * @param vagNumber
	 * @param location         Town etc....
	 * @param holiday          If the entry was marked accordingly, this is
	 *                         initialized.... Remark or holidays...
	 * @param travelDay        If this entry is a travel day, this string describes
	 *                         it...
	 * @param orgiriginalEntry Unchanged entry from source file.
	 */
	public CalendarEntry(boolean isValidEntry, boolean isHoliday, String date, String time, String type,
			String courseNumber, String vagNumber, String location, String holiday, String travelDay,
			String orgiriginalEntry) {
		super();

		this.entryHasChanged = false;
		this.isChildOfAnotherEntry = false;
		this.hasAlreadyBeenComparedToAnotherEntry = false;

		this.isValidEntry = isValidEntry;
		this.isHoliday = isHoliday;

		this.date = date;
		this.time = time;
		this.type = type;
		this.courseNumber = courseNumber;
		this.vagNumber = vagNumber;
		this.location = location;
		this.holiday = holiday;
		this.travelDay = travelDay;
		this.orgiriginalEntry = orgiriginalEntry;
	}

	public boolean isValidEntry() {
		return isValidEntry;
	}

	public boolean isAnotherEventAtSameDay() {
		return isAnotherEventAtSameDay;
	}

	public void setIsAnotherEventAtSameDay() {
		isAnotherEventAtSameDay = true;
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

	public String getTravelDay() {
		return travelDay;
	}

	public String getLocation() {
		return location;
	}

	/**
	 * Gets the entry describing this entry as an holiday.
	 * 
	 * @return Holiday description.
	 */
	public String getHoliday() {
		return holiday;
	}

	/**
	 * @return True if this entry is a holiday. False if not.
	 */
	public boolean isHoliday() {
		return isHoliday;
	}

	/**
	 * @return Raw entry from the source file.
	 */
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
			if (d.length > 2) {
				// In case there was an index added to the
				// year (when more than one event takes place at the same day)
				// extract the year.
				String theYear[] = d[2].split(" ");
				return Integer.valueOf(theYear[0]);
			} else
				return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Assumes that the format of the time string is: |hh.mm|hh.mm|
	 * 
	 * @return Start time of event in: hh.mm
	 */
	public String getStartTime() {

		String t = time.replace("|", " ").trim();
		t = t.replace("_", "");
		String[] tParts = t.split(" ");

		if (tParts.length > 0)
			return tParts[0];
		else
			return "_";
	}

	/**
	 * Assumes that the format of the time-string is hh.mm
	 * 
	 * @return Start time of he event in full hours. If time format is not valid,
	 *         returns 0.
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
	 * Assumes that the format of the time-string is hh.mm
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
	 * Assumes that the format of the time string is: |hh.mm|hh.mm|
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
	 * @return End time of he event in minutes. If time format is not valid, returns
	 *         0.
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

		Calendar currentEventTime = Calendar.getInstance();
		currentEventTime.set(year + 2000, month - 1, day, 0, 23);
		return currentEventTime.getTimeInMillis();
	}

	/**
	 * Compares this entry to a given entry.
	 * <p>
	 * 
	 * Start/-end time, vag number, course number and location are compared.
	 *
	 * Fields which are not equal can be retrieved by invoking their dedicated
	 * getter- methods.
	 * 
	 * @param calendarEntryToCheck
	 */
	public void compareThisCalendarEntryWith(CalendarEntry calendarEntryToCheck) {

		this.dateHasChanged = true;
		this.startTimeHasChanged = true;
		this.endTimeHasChanged = true;
		this.vagNumberHasChanged = true;
		this.courseNumberHasChanged = true;
		this.locationHasChanged = true;

		if (calendarEntryToCheck.getDate().equals(this.getDate()))
			dateHasChanged = false;

		if (calendarEntryToCheck.getStartTime().equals(this.getStartTime()))
			this.startTimeHasChanged = false;

		if (calendarEntryToCheck.getEndTime().equals(this.getEndTime()))
			this.endTimeHasChanged = false;

		if (calendarEntryToCheck.getVagNumber().equals(this.getVagNumber()))
			this.vagNumberHasChanged = false;

		if (calendarEntryToCheck.getCourseNumber().equals(this.getCourseNumber()))
			this.courseNumberHasChanged = false;

		if (calendarEntryToCheck.getLocation().equals(this.getLocation()))
			this.locationHasChanged = false;
	}

	/**
	 * @return True if one of this entry's fields have changed.
	 */
	public boolean entryHasChanged() {
		return entryHasChanged;
	}

	/**
	 * Marks this entry as changed.
	 */
	public void thisEntryHasChanged() {
		entryHasChanged = true;
	}

	/**
	 * @return True if this entry's date has changed.
	 */
	public boolean dateHasChanged() {
		return dateHasChanged;
	}

	/**
	 * @return True if this entry's start time has changed.
	 */
	public boolean startTimeHasChanged() {
		return startTimeHasChanged;
	}

	/**
	 * @return True if this entry's end time has changed.
	 */
	public boolean endTimeHasChanged() {
		return endTimeHasChanged;
	}

	/**
	 * @return True if this entry's vag numer has changed.
	 */
	public boolean vagNumberHasChanged() {
		return vagNumberHasChanged;
	}

	/**
	 * @return True if this entry's course number has changed.
	 */
	public boolean courseNumberHasChanged() {
		return courseNumberHasChanged;
	}

	/**
	 * @return True if this entry's location has changed.
	 */
	public boolean locationHasChanged() {
		return locationHasChanged;
	}

	/**
	 * Use this to check, if this entry is a second event taking place at the same
	 * date as another entry in the calendar it is to be added to.
	 * 
	 * @return True if so, false if this is enry is a single event at a certain
	 *         date.
	 */
	public boolean thisIsChildOfAnotherEntry() {
		return isChildOfAnotherEntry;
	}

	/**
	 * This must be invoked, if this entry is a second event taking place at a
	 * certain date.
	 */
	public void thisEntryIsChildOfAnotherEntry() {
		this.isChildOfAnotherEntry = true;
	}

	/**
	 * Checks this entry against another {@link CalendarEntry}- Object if it is
	 * either belonging to a weekend or it is the same day as in the object passed.
	 * 
	 * @param calToCheck {@link Calendar}- object of which the date to be compared
	 *                   with this entry is obtained from.
	 * 
	 * @return A static value declearing this entry belonging to a weekend or it is
	 *         the same day as given in the calendar passed.
	 */
	public int compareThisEntrysDateWith(Calendar calToCheck) {

		int dayOfMonth = calToCheck.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = calToCheck.get(Calendar.DAY_OF_WEEK);
		int month = calToCheck.get(Calendar.MONTH) + 1;
		int year = calToCheck.get(Calendar.YEAR);

		if (dayOfWeek == Calendar.SATURDAY)
			return IS_SATURDAY;

		if (dayOfWeek == Calendar.SUNDAY)
			return IS_SUNDAY;

		// doto: +2000 seems to be a very dirty hack :-(
		if ((dayOfMonth == this.getDay()) && (year == this.getYear() + 2000) && (month == this.getMonth()))
			return HAS_SAME_DATE;

		return IS_NOT_TODAY_OR_WEEKEND;
	}

	/**
	 * Day of week.
	 * 
	 * @return Integer idicating the day of week for this event.
	 */
	public int getDayOfWeekForThisDate() {
		int year = this.getYear();
		int month = this.getMonth();
		int day = this.getDay();

		Calendar currentEventTime = Calendar.getInstance();
		currentEventTime.set(year + 2000, month - 1, day);

		return currentEventTime.get(Calendar.DAY_OF_WEEK);
	}
}
