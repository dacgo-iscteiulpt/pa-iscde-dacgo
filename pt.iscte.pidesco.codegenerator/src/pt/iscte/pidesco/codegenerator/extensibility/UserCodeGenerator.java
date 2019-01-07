package pt.iscte.pidesco.codegenerator.extensibility;

import java.util.HashMap;


/** Class used to the implementation of the snippet extension-point.
 **/
public class UserCodeGenerator{
	
	private RangeScope rangeScope;
	private HashMap<String, String> macroToCode;
	
	/** Constructor of the UserCodeGenerator class, 
	 * @param HashMap of String, String, where the key value must be the macro that when selected and when "Generate code" is pressed, it will generate the respective code, being the value of the hashmap;
	 * @param RangeScope, needs to be defined to give the scope range of the functionality to be added.
	 */
	public UserCodeGenerator(HashMap<String, String> macroToCode, RangeScope rangeScope) {
		this.macroToCode = macroToCode;
		this.rangeScope = rangeScope;
	}

	public RangeScope getRangeScope() {
		return rangeScope;
	}

	public void setRangeScope(RangeScope rangeScope) {
		this.rangeScope = rangeScope;
	}

	public HashMap<String, String> getMacroToCode() {
		return macroToCode;
	}

	public void setMacroToCode(HashMap<String, String> macroToCode) {
		this.macroToCode = macroToCode;
	}
	
	
}
