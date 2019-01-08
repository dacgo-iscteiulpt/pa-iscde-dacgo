package outline.extensibility;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;

import outline.extensibility.models.OutlineElement;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public interface IOutlineService {

	/**
	 * Parses SourceElement if it is a file, if it's a package does nothing.
	 * 
	 * @param editorServices, JavaEditorServices that is used to parse the file
	 * @param element, SourceElement to be parsed and visited by the OutlineAST
	 */
	void parseSelectedSourceElement(JavaEditorServices editorService, SourceElement element);

	/**
	 * Parses and visits file with OutlineAST.
	 * 
	 * @param editorServices, JavaEditorServices that is used to parse the file
	 * @param file, File to be parsed and visited by the OutlineAST
	 */
	void parseSelectedFile(JavaEditorServices editorService, File file);

	/**
	 * @return IOutlineAST, the AST being used.
	 */
	IOutlineAST getOutlineVisitor();

	/**
	 * 
	 * @return List<OutlineElement>, returns the current list the AST has stored of
	 *         OutlineElements.
	 */
	List<OutlineElement> getOutlineElementList();

	/**
	 * Sets AST to be used by this OutlineService when parsing/visiting.
	 * 
	 * @param outlineVisitor, a visitor that implements the IOutlineAST interface.
	 */
	void setOutlineVisitor(IOutlineAST outlineVisitor);

	/**
	 * @return String,the currently selected package's name. Example:
	 *         "outline.extensibility".
	 */
	String getCurrentSelectedPackage();

	/**
	 * Returns the current filepath previously acquired using the method
	 * "java.io.File.getPath()".
	 * 
	 * @return String,the currently open file path.
	 */
	String getCurrentFile();

	/**
	 * For each OutlineElement in a list creates a corresponding TreeItem.
	 * 
	 * @param outlineElements, list of OutlineElements that will be represented in
	 *        the Tree.
	 * @param tree, base tree where TreeItems will be added.
	 */
	void addTreeItems(List<OutlineElement> outlineElements, Tree tree);

	/**
	 * Sets the imageMap to be used by the application.
	 * 
	 * @param imageMap, the image map to be used.
	 */
	void setImageMap(Map<String, Image> imageMap);

}