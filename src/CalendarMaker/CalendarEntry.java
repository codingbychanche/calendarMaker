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
	 * Extracts month from a date string object. Date- string format: dd.mm.yyyy
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
	 * Extracts year from a date string object. Date- string format: dd.mm.yyyy
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

	/**
	 * Asumes that the format of the time string is: |hh.mm|hh.mm|
	 * 
	 * @return Start time of event in: hh.mm
	 */
	public String getStartTime() {

		String t = time.replace("|", " ").trim();
		String[] tParts = t.split(" ");
		System.out.println(t);

		if (tParts.length >0)
			return tParts[0];
		else
			return "_";
	}
	
	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return Start time of he event in full hours.
	 */
	public String getStartTimeHours(){
		String h[]=this.getStartTime().split("\\.");
		if (h.length>0)
			return h[0];
		else 
			return "-";
	}
	
	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return End time of he event in minutes.
	 */
	public String getStartTimeMinutes(){
		String h[]=this.getStartTime().split("\\.");
		if (h.length>1)
			return h[1];
		else 
			return "-";
	}

	/**
	 * Asumes that the format of the time string is: |hh.mm|hh.mm|
	 * 
	 * @return End time of Event in hh.mm
	 */
	public String getEndTime() {
		String t = time.replace("|", " ").trim();
		String[] tParts = t.split(" ");
		
		if (tParts.length ==2)
			return tParts[1];
		else
			return "_";
	}
	
	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return End time of he event in full hours.
	 */
	public String getEndTimeHours(){
		String h[]=this.getEndTime().split("\\.");
		if (h.length>0)
			return h[0];
		else 
			return "-";
	}
	
	/**
	 * Asumes that the format of the time-string is hh.mm
	 * 
	 * @return End time of he event in minutes.
	 */
	public String getEndTimeMinutes(){
		String h[]=this.getEndTime().split("\\.");
		if (h.length>1)
			return h[1];
		else 
			return "-";
	}
}
