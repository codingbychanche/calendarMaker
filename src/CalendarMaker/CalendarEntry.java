package CalendarMaker;

/**
 * Data model for one calendar entry.
 * 
 * @author Berthold
 *
 */

public class CalendarEntry {

	//
	// The data for this entry
	//
	public boolean isValidEntry; // Must have at least date and course number or VAG
							// number
	String date;
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
	 * @param type
	 *            EQ, UV etc...
	 * @param courseNumber
	 *            TA9989, BÜ0221 etc...
	 * @param vagNumber
	 * @param location
	 * @param holiday
	 *            Remark or holidays...
	 * @param orgiriginalEntry
	 *            Unchanged entry from source file.
	 */
	public CalendarEntry(boolean isValidEntry, String date, String type, String courseNumber, String vagNumber,
			String location, String holiday, String orgiriginalEntry) {
		super();
		
		this.isValidEntry=isValidEntry;

		this.date = date;
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
}
