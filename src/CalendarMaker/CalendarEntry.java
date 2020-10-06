package CalendarMaker;

/**
 * Data model for one calendar entry.
 * 
 * @author Berthold
 *
 */

public class CalendarEntry {

	public boolean isValidEntry; // Must have at least date and course number or
									// VAG
	// number
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
	 * @param isValidENtry
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
	
	public String getTime() {
		return time;
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
	 * 09.20.2020
	 * 
	 * @return Day of month if date was in the correct format. Returns "-" if
	 *         not.
	 */
	public String getDay() {
		String[] d = date.split("\\.");
		if (d.length > 0)
			return d[0];
		else
			return "-";
	}

	/**
	 * Extracts month from a date string object. Date- string format: 09.20.2020
	 * 
	 * @return Month if date was in the correct format. Returns "-" if not.
	 */
	public String getMonth() {
		String[] d = date.split("\\.");
		if (d.length > 1)
			return d[1];
		else
			return "-";
	}

	/**
	 * Extracts year from a date string object. Date- string format: 09.20.2020
	 * 
	 * @return Year if date was in the correct format. Returns "-" if not.
	 */
	public String getYear() {
		String[] d = date.split("\\.");
		if (d.length > 2)
			return d[2];
		else
			return "-";
	}
}
