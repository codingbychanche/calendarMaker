package CalendarMaker;
/**
 * Searces german "umlaut" in an ascii- string. Found "umlaut's" will
 * be converted to theit HTML- equivialent.</p>
 * 
 *  Such strings can be displayed correctly inside of a html- view.
 * 
 * @author Berthold
 *
 */
public class ConvertUmlaut {
	private static String testString;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args []){
		testString="Hallo äüöÄÖÜ";
		
		char array[]=testString.toCharArray();
		
		for (char b:array)
			System.out.println(b+"    "+(int)b);
		
		
		StringBuilder umlString=new StringBuilder();
		
		for (char b:array){
			switch (b){
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
						umlString.append(b);
						break;
			}
		}
		System.out.println(umlString);
	}
}
