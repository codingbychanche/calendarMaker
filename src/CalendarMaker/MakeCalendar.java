package CalendarMaker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This contains all calender entry objects and provieds methods to convert
 * these objects (e.c. into csv- or .ics - files).
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

	int totalNumberOfLines = 0, numberOfLinesValid = 0, numberOfLinesNotValid = 0;

	//
	// Patterns
	//
	Pattern datumPattern = Pattern.compile("(\\d{1,2}\\.){2}\\d{2,4}");
	Pattern courseNumberPattern = Pattern.compile("((.){2}\\d{4})");
	Pattern vagNumberPattern = Pattern.compile("\\d{2}/\\d{4}");
	
	Pattern timePattern = Pattern.compile("(\\|\\s?_?\\d{1,2}\\.\\d{1,2}\\|)(\\d{1,2}\\.\\d{1,2}\\|)");

	Pattern locationPattern = Pattern.compile(
			"(?i)(Karlsruhe)|(M.nchen)|(Hannover)|(Berlin)|(Freiburg)|(Wuppertal)|(Saarbr.cken)|(Ludwigsburg)|"
			+ "(Witten)|(Fulda)");

	Pattern holidayPattern = Pattern.compile("(?i)(urlaub)");
	Pattern typePattern = Pattern.compile("(?i)(re)|(uv)|(kl)|(eq)|(up)|(arbeitszeitausgleich)");

	//
	// Result
	//
	static final boolean IS_VALID_ENTRY = true;
	static final boolean IS_INVALID_ENTRY = false;
	String foundDate, foundTime, foundVagNumber, foundCourseNumber, foundLocation, foundHoliday, foundType;
	boolean hasDate, hasTime, hasCourseNumber, hasVagNumber, hasHoliday, hasLocation, hasType;

	//
	// Error
	//
	boolean hasError;
	String errorDescription;

	/**
	 * Reads the source file containing the calendar entry's, extracts them
	 * by parsing line by line and creates a new entry for this calendar.
	 *
	 */
	public MakeCalendar(String path) {
		//
		// Read file line by line
		//
		hasError = false;

		totalNumberOfLines = getNumberOfLines(path);

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			Matcher matcher;

			while ((lineRead = br.readLine()) != null) {

				//
				// Check line read for entrys that form a valid calendar entry
				//
				hasDate = hasTime = hasVagNumber = hasCourseNumber = hasLocation = hasHoliday = hasType = false;
				foundDate = foundTime = foundCourseNumber = foundVagNumber = foundLocation = foundHoliday = foundType = "-";

				matcher = datumPattern.matcher(lineRead);
				while (matcher.find()) {
					foundDate = matcher.group(0);
					hasDate = true;
				}

				matcher = timePattern.matcher(lineRead);
				while (matcher.find()) {  // start time
					foundTime = matcher.group(0);
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
				// Check data obtained from line just read for valid contitions
				// and create a calendar entry.
				//
				if (hasDate && (hasCourseNumber || hasVagNumber || hasHoliday || hasType)) {
					CalendarEntry calendarEntry = new CalendarEntry(IS_VALID_ENTRY, foundDate, foundTime, foundType,
							foundCourseNumber, foundVagNumber, foundLocation, foundHoliday, lineRead);
					this.addEntry(calendarEntry);
					numberOfLinesValid++;
				} else {
					//
					// The line does not have valid conditions for a
					// calendar entry, but, you'll never know.
					// Store line, let the user decide...
					//
					CalendarEntry calendarEntry = new CalendarEntry(IS_INVALID_ENTRY, foundDate, foundTime, foundType,
							foundCourseNumber, foundVagNumber, foundLocation, foundHoliday, lineRead);
					this.addEntry(calendarEntry);
					numberOfLinesNotValid++;
				}
			}
			br.close();

		} catch (Exception e) {
			hasError = true;
			errorDescription = e.toString();
		}
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
	 * @return Total number of lines
	 */
	public int getTotalNumberOfLinesRead() {
		return totalNumberOfLines;
	}

	/**
	 * @return True, if calender could not be read from the filesystem.
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
	 * @return The calendar created containing all valid and invalid entry's in
	 *         raw text.
	 */
	public List<CalendarEntry> getRawCalendar() {
		return calendarEntrys;
	}

	/**
	 * Sole purpose, count number of lines the file specified contains.
	 * 
	 * @param path
	 *            Files location.
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
