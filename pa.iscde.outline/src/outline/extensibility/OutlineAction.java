package outline.extensibility;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

public interface OutlineAction {
	
	/**
	 * Creates setters and getters for the fields received.
	 * The code will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param area, represents the outline components base composite.
	 * @param filePath, is the currently selected file that is open in java editor.
	 * @param selectedAttributes, is a list that contains all selected attributes in the outline tree, they are strings that take the form "VarType varName".
	 * @param selectedMethods, is a list that contains all selected methods in the outline tree, they are strings that take the form "ReturnType methodName".
	 * @param selectedElements, is a list that contains all selected elements in the outline tree.
	**/
	void run(Composite area, String filePath,List<String> selectedAttributes,List<String> selectedMethods,List<String> selectedElements);

}
