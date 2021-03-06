package CalendarMaker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a list of calendar entrys from a text file and provides methods to
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
	Pattern claendarHeaderPattern = Pattern
			.compile("(?i)(lehrperson)(\\.|\\s|-)(?i)(einsatz)\\s+((\\d+)(\\.|\\s+))+(\\d+:)\\d+");

	Pattern datePattern = Pattern.compile("(\\d{1,2}\\.){2}\\d{2,4}");
	Pattern courseNumberPattern = Pattern.compile("((.){2}\\d{4})");
	Pattern vagNumberPattern = Pattern.compile("\\d{2}/\\d{4,}");

	Pattern timeFormatRegularPattern = Pattern.compile("\\d+:\\d+"); // e.g. 06:45
	Pattern timeFormatInCalendarSourceFilePattern = Pattern
			.compile("(\\|\\s?_?\\d{1,2}\\.\\d{1,2}\\|)(\\d{1,2}\\.\\d{1,2}\\|)");

	Pattern locationPattern = Pattern
			.compile("(?i)(Karlsruhe)|(M.nchen)|(Hannover)|(Berlin)|(Freiburg)|(Wuppertal)|(Saarbr.cken)|(Ludwigsburg)|"
					+ "(Witten)|(Fulda)|(Virtueller Raum)|(Online)|(K.ln)");

	Pattern holidayPattern = Pattern.compile("(?i)(urlaub)");
	Pattern typePattern = Pattern.compile("(?i)(re)|(uv)|(kl)|(eq)|(up)|(arbeitszeitausgleich)");

	//
	// Result
	//
	static final boolean IS_VALID_ENTRY = true;
	static final boolean IS_INVALID_ENTRY = false;

	String calendarHeader, revisionDate, revisionTime, foundDate, foundTime, foundVagNumber, foundCourseNumber,
			foundLocation, foundHoliday, foundType;
	
	//
	// Let us remember the last date read in case we found a line without a date
	// which then must be another course at the same day (the last line with date read).
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
	 * Reads the source file containing the calendar entry's, extracts them by
	 * parsing line by line and creates a new entry for this calendar.
	 *
	 * @param path Path of the text file containing the job schedule.
	 */
	public MakeCalendar(String path) {
	
		hasError = false;
		totalNumberOfLines = getNumberOfLines(path);

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

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
					lastDateRead=foundDate;
					hasDate = true;
				}

				matcher = timeFormatInCalendarSourceFilePattern.matcher(lineRead);
				while (matcher.find()) { // start time
					foundTime = matcher.group(0);
					hasTime=true;
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
					// If the line just read contains no date but start and/ or end time and either of
					// the strings describing a valid course, then it is declared as a valid entry
					// for the last line with date read => another course at the same day....
					//
					if ( hasTime && (hasCourseNumber || hasVagNumber || hasHoliday || hasType)) {
						CalendarEntry calendarEntry = new CalendarEntry(IS_VALID_ENTRY, hasHoliday,lastDateRead,
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
	 * @return The calendar created containing all valid and invalid entry's in raw
	 *         text.
	 */
	public List<CalendarEntry> getRawCalendar() {
		return calendarEntrys;
	}

	/**
	 * Gets all calendar entries matching the given VAG- number.
	 * 
	 * @param vagNumber
	 * @return List of matching courses
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
	 * @return Number of lines which represent a valid calendar entry.
	 */
	public int getNumberOfLinesValid() {
		return numberOfLinesValid;
	}

	/**
	 * @return Number of lines which represent an invalid calendar entry.
	 */
	public int getNumberOfLinesNotValid() {
		return numberOfLinesNotValid;
	}

	/**
	 * @return Total number of lines read and converted from the textfile containing
	 *         the job schedule.
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
}
