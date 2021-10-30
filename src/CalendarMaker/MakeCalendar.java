package CalendarMaker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a list of calendar entries from a text file and provides methods to
 * convert this calendar (e.g. into csv- or .ics - files).
 * 
 * @author Berthold
 *
 */
public class MakeCalendar {

	//
	// This calendars entry's
	//
	List<CalendarEntry> calendarEntrys = new ArrayList<CalendarEntry>();

	//
	// File
	//
	StringBuffer buffer = new StringBuffer();
	String lineRead;

	// Values telling us about the status of the calendar.
	int totalNumberOfLines = 0, numberOfLinesValid = 0, numberOfLinesNotValid = 0;

	//
	// Regex- patterns
	//
	public static final Pattern claendarHeaderPattern = Pattern
			.compile("(?i)(lehrperson)(\\.|\\s|-)(?i)(einsatz)\\s+((\\d+)(\\.|\\s+))+(\\d+:)\\d+");

	public static final Pattern datePattern = Pattern.compile("(\\d{1,2}\\.){2}\\d{2,4}");

	public static final Pattern courseNumberPattern = Pattern.compile("((.){2}\\d{4})");

	public static final Pattern vagNumberPattern = Pattern.compile("\\d{2}/\\d{4,}");

	public static final Pattern timeFormatRegularPattern = Pattern.compile("\\d+:\\d+"); // e.g. 06:45
	public static final Pattern timeFormatInCalendarSourceFilePattern = Pattern
			.compile("(\\|\\s?_?\\d{1,2}\\.\\d{1,2}\\|)(\\d{1,2}\\.\\d{1,2}\\|)");

	public static final Pattern locationPattern = Pattern
			.compile("(?i)(Karlsruhe)|(M.nchen)|(Hannover)|(Berlin)|(Freiburg)|(Wuppertal)|(Saarbr.cken)|(Ludwigsburg)|"
					+ "(Witten)|(Fulda)|(Virtueller Raum)|(Online)|(K.ln)|(Bad Homburg)");

	public static final Pattern holidayPattern = Pattern.compile("(?i)(urlaub)");
	public static final Pattern typePattern = Pattern.compile("(?i)(re)|(uv)|(kl)|(eq)|(up)|(arbeitszeitausgleich)");

	//
	// Result
	//
	static final boolean IS_VALID_ENTRY = true;
	static final boolean IS_INVALID_ENTRY = false;

	String calendarHeader, revisionDate, revisionTime, foundDate, foundTime, foundVagNumber, foundCourseNumber,
			foundLocation, foundHoliday, foundType;

	//
	// Let us remember the last date read in case we found a line without a date
	// which then must be another course at the same day (the last line with date
	// read).
	//
	String lastDateRead;

	// Tells us what we have found inside the last line read.
	boolean hasDate, hasTime, hasCourseNumber, hasVagNumber, hasHoliday, hasLocation, hasType;

	//
	// Error
	//
	boolean hasError;
	String errorDescription;

	/**
	 * Reads the source file containing the calendar entries, extracts them by
	 * parsing line by line and creates a new entry for this calendar.
	 *
	 * @param path Path of the text file containing the job schedule.
	 */
	public MakeCalendar(InputStream stream) {

		hasError = false;
		// totalNumberOfLines = getNumberOfLines(path);

		try {
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);

			calendarHeader = "-";

			Matcher matcher;

			//
			// Read file line by line
			//
			while ((lineRead = br.readLine()) != null) {

				//
				// Check line read for entries that form a valid calendar entry
				//
				hasDate = hasTime = hasVagNumber = hasCourseNumber = hasLocation = hasHoliday = hasType = false;
				foundDate = foundTime = foundCourseNumber = foundVagNumber = foundLocation = foundHoliday = foundType = "-";

				// Collect calendars header information
				matcher = claendarHeaderPattern.matcher(lineRead);
				while (matcher.find()) {
					calendarHeader = matcher.group(0);
				}

				matcher = datePattern.matcher(calendarHeader);
				while (matcher.find()) {
					revisionDate = matcher.group(0);
				}

				matcher = timeFormatRegularPattern.matcher(calendarHeader);
				while (matcher.find()) {
					revisionTime = matcher.group(0);
				}

				matcher = datePattern.matcher(lineRead);
				while (matcher.find()) {
					foundDate = matcher.group(0);
					lastDateRead = foundDate;
					hasDate = true;
				}

				matcher = timeFormatInCalendarSourceFilePattern.matcher(lineRead);
				while (matcher.find()) { // start time
					foundTime = matcher.group(0);
					hasTime = true;
				}

				matcher = courseNumberPattern.matcher(lineRead);
				while (matcher.find()) {
					foundCourseNumber = matcher.group(0);
					hasCourseNumber = true;
				}

				matcher = vagNumberPattern.matcher(lineRead);
				while (matcher.find()) {
					foundVagNumber = matcher.group(0);
					hasVagNumber = true;
				}

				matcher = locationPattern.matcher(lineRead);
				while (matcher.find()) {
					foundLocation = matcher.group(0);
					hasLocation = true;
				}

				matcher = holidayPattern.matcher(lineRead);
				while (matcher.find()) {
					foundHoliday = matcher.group(0);
					hasHoliday = true;
				}

				matcher = typePattern.matcher(lineRead);
				while (matcher.find()) {
					foundType = matcher.group(0);
					hasType = true;
				}

				//
				// Check data obtained from line just read for valid conditions
				// and create a calendar entry.
				//
				if (hasDate && (hasCourseNumber || hasVagNumber || hasHoliday || hasType)) {

					CalendarEntry calendarEntry = new CalendarEntry(IS_VALID_ENTRY, hasHoliday, foundDate, foundTime,
							foundType, foundCourseNumber, foundVagNumber, foundLocation, foundHoliday, lineRead);
					this.addEntry(calendarEntry);
					numberOfLinesValid++;
				} else {

					//
					// If the line just read contains no date but start and/ or end time and either
					// of
					// the strings describing a valid course, then it is declared as a valid entry
					// for the last line with date read => another course at the same day....
					//
					if (hasTime && (hasCourseNumber || hasVagNumber || hasHoliday || hasType)) {
						CalendarEntry calendarEntry = new CalendarEntry(IS_VALID_ENTRY, hasHoliday, lastDateRead,
								foundTime, foundType, foundCourseNumber, foundVagNumber, foundLocation, foundHoliday,
								lineRead);
						this.addEntry(calendarEntry);
						numberOfLinesValid++;
					} else {

						//
						// The line does not have valid conditions for a
						// calendar entry, but, you'll never know.
						// Store line, let the user decide...
						//
						CalendarEntry calendarEntry = new CalendarEntry(IS_INVALID_ENTRY, hasHoliday, foundDate,
								foundTime, foundType, foundCourseNumber, foundVagNumber, foundLocation, foundHoliday,
								lineRead);
						this.addEntry(calendarEntry);
						numberOfLinesNotValid++;
					}
				}
			}
			br.close();

		} catch (Exception e) {
			hasError = true;
			errorDescription = e.toString();
		}
	}

	/**
	 * The calendar created containing all valid and invalid entries.
	 * 
	 * @return A list of {@link CalendarEntry} objects.
	 */
	public List<CalendarEntry> getRawCalendar() {
		return calendarEntrys;
	}

	/**
	 * Gets all calendar entries matching the given VAG- number.
	 * 
	 * @param vagNumber
	 * @return List of {@link CalendarEntry} objects containing all matching
	 *         courses.
	 * 
	 */
	public List<CalendarEntry> getCalenderEntrysMatchingVAG(String vagNumber) {

		List<CalendarEntry> filtered = new ArrayList();
		for (CalendarEntry e : calendarEntrys) {
			if (e.getVagNumber().endsWith(vagNumber))
				filtered.add(e);
		}
		return filtered;
	}

	/**
	 * Builds a list containing all unique vag numbers from all valid entries of
	 * this calendar. If a course number was passed as a parameter then only the vag
	 * numbers assigned to the passed course number are added.
	 * 
	 * @param courseNumber
	 * @return A list of {@link CalendarEntry} objects. If no course number was
	 *         passed a list of all unique vag numbers of this calendar. If a course
	 *         number was passed a list of unique vag numbers assigned to this
	 *         course number.
	 */
	public List<String> getListOfAllVAGNumbers(String courseNumber) {
		HashSet<String> v = new HashSet<>();

		for (CalendarEntry e : calendarEntrys) {
			if (e.isValidEntry) {
				if (courseNumber.isEmpty())
					v.add(e.getVagNumber());
				else if (e.getCourseNumber().equals(courseNumber))
					v.add(e.getVagNumber());
			}
		}

		ArrayList<String> vagNumbers = new ArrayList<>(v);

		return vagNumbers;
	}

	/**
	 * Gets all calendar entries matching the given course- number.
	 * 
	 * @param courseNumber
	 * @return List of matching courses
	 */
	public List<CalendarEntry> getCalenderEntrysMatchingCourseNumber(String courseNumber) {

		List<CalendarEntry> filtered = new ArrayList();
		for (CalendarEntry e : calendarEntrys) {
			if (e.getVagNumber().endsWith(courseNumber))
				filtered.add(e);
		}
		return filtered;
	}

	/**
	 * Builds a list of unique course numbers from valid calendar entries. If a vag
	 * number is passed, only the course number matching this vag number is added.
	 * 
	 * @param vagNumber If not empty, only the course with the matching vag number
	 *                  is added.
	 * @return List of all unique course numbers or just the course number matching
	 *         the passed vag number.
	 */
	public List<String> getListOfAllCourseNumbers(String vagNumber) {
		HashSet<String> c = new HashSet<>();

		for (CalendarEntry n : calendarEntrys) {
			if (n.isValidEntry) {
				if (vagNumber.isEmpty())
					c.add(n.getCourseNumber());
				else if (n.getVagNumber().equals(vagNumber))
					c.add(n.getCourseNumber());
			}
		}

		ArrayList<String> courseNumbers = new ArrayList<>(c);

		return courseNumbers;
	}

	/**
	 * Gets all calendar entries representing holidays.
	 * 
	 * @param courseNumber
	 * @return List of all calendar entries that represent holidays.
	 */
	public List<CalendarEntry> getCalenderEntrysMatchingHoliday(String courseNumber) {

		List<CalendarEntry> filtered = new ArrayList();
		for (CalendarEntry e : calendarEntrys) {
			if (e.getVagNumber().endsWith(courseNumber))
				filtered.add(e);
		}
		return filtered;
	}

	/**
	 * Builds a list of all courses and their associated vag numbers from valid
	 * entries.
	 * 
	 * @return Course list containing course number and vag number.
	 */
	public List<String> getCourseList() {
		LinkedHashSet<String> courseList = new LinkedHashSet<>();
		courseList.add("*");

		List<String> courseNumbers = new ArrayList<>();
		courseNumbers = this.getListOfAllCourseNumbers("");

		for (String course : courseNumbers) {
			for (CalendarEntry calEntry : calendarEntrys) {
				if (calEntry.getCourseNumber().equals(course))
					courseList.add(course + "," + calEntry.getVagNumber());
			}
		}
		ArrayList<String> c = new ArrayList<>(courseList);
		return c;
	}

	/**
	 * Adds a {@link CalendarEntry} instance to this calendar.
	 * 
	 * @param clendarEntry
	 */
	public void addEntry(CalendarEntry clendarEntry) {
		calendarEntrys.add(clendarEntry);
	}

	/**
	 * @return Description of the calendar containing it's revision date.
	 */
	public String getCalendarHeader() {
		return calendarHeader;
	}

	/**
	 * @return Date of last revision of this calendar
	 */
	public String getCalendarRevisionDate() {
		return revisionDate;
	}

	/**
	 * @return Time of last revision of this calendar
	 */
	public String getCalendarRevisionTime() {
		return revisionTime;
	}

	/**
	 * @return Number of lines which represent a valid calendar entrys.
	 */
	public int getNumberOfLinesValid() {
		return numberOfLinesValid;
	}

	/**
	 * @return Number of lines which represent an invalid calendar entrys.
	 */
	public int getNumberOfLinesNotValid() {
		return numberOfLinesNotValid;
	}

	/**
	 * @return Total number of lines read and converted from the text file
	 *         containing the job schedule.
	 */
	public int getTotalNumberOfLinesRead() {
		return totalNumberOfLines;
	}

	/**
	 * @return True, if calendar could not be read from the file system.
	 */
	public boolean hasError() {
		return hasError;
	}

	/**
	 * @return Description of the file i/o error.
	 */
	public String getErorrDescription() {
		return errorDescription;
	}

	/**
	 * Sole purpose, count number of lines the file specified contains.
	 * 
	 * @param path Files location.
	 * @return Number of lines the file contains.
	 */
	public int getNumberOfLines(String path) {
		int lines = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.readLine() != null)
				lines++;
			br.close();
		} catch (Exception e) {
			hasError = true;
			errorDescription = e.toString();
		}
		return lines;
	}

	/**
	 * Performs a regex- check.
	 * 
	 * @param pattern
	 * @param string
	 * @return true if the given string contains the matching pattern, false if not.
	 */
	public boolean checkIfRegexPatternMatches(Pattern pattern, String string) {
		Matcher matcher = pattern.matcher(string);
		while (matcher.find())
			return true;
		return false;
	}

	/**
	 * Gets the matching group.
	 * 
	 * @param pattern
	 * @param string
	 * @return The string found or null if no match.
	 */
	public String getMatcherGroup(Pattern pattern, String string) {
		Matcher matcher = pattern.matcher(string);
		while (matcher.find())
			return matcher.group(0);
		return null;
	}
}
