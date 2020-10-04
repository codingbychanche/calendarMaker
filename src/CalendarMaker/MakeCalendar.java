package CalendarMaker;

import java.io.BufferedReader;
import java.io.FileReader;
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
	List<CalendarEntry> calendarEntrys = new ArrayList();

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
	Pattern courseNumberPattern = Pattern.compile("(((?i)[a-z\\?]){2}\\d{4})");
	Pattern vagNumberPattern = Pattern.compile("\\d{2}/\\d{4}");
	Pattern timePattern = Pattern.compile("(\\d{1,2}(.)\\d{1,2})(!=.)");

	Pattern locationPattern = Pattern.compile(
			"(?i)" + "(Karlsruhe)|" + "(München)|" + "(Hannover)|" + "(Berlin)|" + "(Freiburg)|" + "(Wuppertal)");

	Pattern holidayPattern = Pattern.compile("(?i)(urlaub)");
	Pattern typePattern = Pattern.compile("(?i)(re)|(uv)|(kl)|(eq)|(up)|(arbeitszeitausgleich)");

	//
	// Result
	//
	static final boolean IS_VALID_ENTRY = true;
	static final boolean IS_UNVALID_ENTRY = false;
	String foundDate, foundVagNumber, foundCourseNumber, foundLocation, foundHoliday, foundType;
	boolean hasDate, hasCourseNumber, hasVagNumber, hasHoliday, hasLocation, hasType;

	//
	// Error
	//
	boolean hasError;
	String errorDescription;

	/**
	 * Reads the source file containing the entry's, extracts them by pasing
	 * line by line and creates a new entry for this calendar.
	 * <p>
	 * 
	 * If a line contains at least a date, it is marked as a valid entry. if
	 * not, it is marked as not valid.
	 * 
	 */
	public MakeCalendar(String path) {
		//
		// Read file line by line
		//
		hasError = false;
		
		totalNumberOfLines=getNumberOfLines(path);

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			Matcher matcher;
			
			while ((lineRead = br.readLine()) != null) {

				//
				// Check line read for entrys that form a valid calendar entry
				//
				matcher = datumPattern.matcher(lineRead);
				hasDate = hasVagNumber = hasCourseNumber = hasLocation = hasHoliday = hasType = false;
				foundDate = foundCourseNumber = foundVagNumber = foundLocation = foundHoliday = foundType = "-";

				while (matcher.find()) {
					// This is the line containing the search pattern
					foundDate = matcher.group(0);
					hasDate = true;
				}

				matcher = courseNumberPattern.matcher(lineRead);
				while (matcher.find()) {
					// This is the line containing the search pattern
					foundCourseNumber = matcher.group(0);
					hasCourseNumber = true;
				}

				matcher = vagNumberPattern.matcher(lineRead);
				while (matcher.find()) {
					// This is the line containing the search pattern
					foundVagNumber = matcher.group(0);
					hasVagNumber = true;
				}

				matcher = locationPattern.matcher(lineRead);
				while (matcher.find()) {
					// This is the line containing the search pattern
					foundLocation = matcher.group(0);
					hasLocation = true;
				}

				matcher = holidayPattern.matcher(lineRead);
				while (matcher.find()) {
					// This is the line containing the search pattern
					foundHoliday = matcher.group(0);
					hasHoliday = true;
				}

				matcher = typePattern.matcher(lineRead);
				while (matcher.find()) {
					// This is the line containing the search pattern
					foundType = matcher.group(0);
					hasType = true;
				}

				//
				// Check data obtained from line just read for valid contitions
				// and create a calendar entry.
				//
				if (hasDate && (hasCourseNumber || hasVagNumber || hasHoliday || hasType)) {
					CalendarEntry calendarEntry = new CalendarEntry(true, foundDate, foundType,
							foundCourseNumber, foundVagNumber, foundLocation, foundHoliday, lineRead);
					this.addEntry(calendarEntry);
					numberOfLinesValid++;
				} else {
					//
					// The line does not have valid conditions for a
					// calendar entry, but, you'll never know.
					// Store line, let the user decide...
					//
					CalendarEntry calendarEntry = new CalendarEntry(false, foundDate, foundType,
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

	public int getNumberOfLinesValid() {
		return numberOfLinesValid;
	}

	public int getNumberOfLinesNotValid() {
		return numberOfLinesNotValid;
	}

	public int getTotalNumberOfLinesRead() {
		return totalNumberOfLines;
	}

	public boolean hasError() {
		return hasError;
	}
	
	public String getErorrDescription (){
		return errorDescription;
	}

	/**
	 * @return The calendar created containing all valid and unvalid entry's as
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
			hasError=true;
			errorDescription=e.toString();
		}
		return lines;
	}
}
