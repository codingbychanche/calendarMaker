package CalendarMaker;

/**
 * Class helping to convert German umlauts.
 * 
 * @author Berthold
 *
 */
public class ConvertUmlaut {

	/**
	 * Converts umlauts to their html- equivalent.
	 * 
	 * @param suspectedUmlautString String suspected containing German umlauts
	 * @return String containing html- equivalent of the umlauts found.
	 */
	public static String toHtml(String suspectedUmlautString) {

		StringBuilder umlString = new StringBuilder();
		char array[] = suspectedUmlautString.toCharArray();

		for (char suspectedUmlaut : array) {
			switch (suspectedUmlaut) {
			case 228:
				umlString.append("&auml");
				break;
			case 252:
				umlString.append("&uuml");
				break;
			case 246:
				umlString.append("&ouml");
				break;

			case 196:
				umlString.append("&Auml");
				break;
			case 214:
				umlString.append("&Ouml");
				break;
			case 220:
				umlString.append("&Uuml");
				break;

			default:
				umlString.append(suspectedUmlaut);
				break;
			}
		}
		return umlString.toString();
	}
}
