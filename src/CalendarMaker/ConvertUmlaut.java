package CalendarMaker;

/**
 * Class helping to convert German umlauts.
 * 
 * @author Berthold
 *
 */
public class ConvertUmlaut {

	// Just to remember:
	// The byte  in java represents a signed 8- bit integer! So: Range is
	// from 0 to 127! Char type is a 16- bit integer.
	// 
	// German umlauts are represented by 8- bit integers greater than 127
	// E.g. ö is 246. Since (for reasons I don't know yet) the higher 8- bit
	// of the integer are set, so ö= 65526
	private static final int aUml = 65508;
	private static final int uUml = 65533;
	private static final int oUml = 65526;
	private static final int AUml = 65476;
	private static final int OUml = 65494;
	private static final int UUml = 65500;

	/**
	 * Converts umlauts to their html- equivalent.
	 * 
	 * @param suspectedUmlautString
	 *            String suspected containing German umlauts
	 * @return String containing html- equivalent of the umlauts found.
	 */
	public static String toHtml(String suspectedUmlautString) {
		
		StringBuilder umlString = new StringBuilder();
		char array[] = suspectedUmlautString.toCharArray();

		for (char suspectedUmlaut : array) {
		
			switch (suspectedUmlaut) {
			case aUml:
				umlString.append("&auml;");
				break;
			case uUml:
				umlString.append("&uuml;");
				break;
			case oUml:
				umlString.append("&ouml;");
				break;
			case AUml:
				umlString.append("&Auml;");
				break;
			case OUml:
				umlString.append("&Ouml;");
				break;
			case UUml:
				umlString.append("&Uuml;");
				break;
			default:
				umlString.append(suspectedUmlaut);
				break;
			}
		}
		return umlString.toString();
	}
}
