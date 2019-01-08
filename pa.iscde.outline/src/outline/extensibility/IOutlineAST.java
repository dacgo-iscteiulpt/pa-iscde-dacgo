package outline.extensibility;

import java.util.List;

import outline.extensibility.models.OutlineElement;

public interface IOutlineAST {

	//Reset the element list stored by the AST.
	void resetOutlineElementList();

	//Return all OutlineElements.
	List<OutlineElement> getOutlineElementList();
	
	//Get the current package's name. Example: "outline.extensibility".
	String getCurrentPackage();
}
