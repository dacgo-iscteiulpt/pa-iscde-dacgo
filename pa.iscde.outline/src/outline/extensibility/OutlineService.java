package outline.extensibility;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import outline.model.OutlineAttribute;
import outline.model.OutlineElement;
import outline.model.OutlineMethod;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public class OutlineService {
	private static final String ENUM_IMAGE = "enum.png";
	private static final String INTERFACE_IMAGE = "interface.png";
	private static final String CLASS_IMAGE = "class.png";
	private static final String NO_TYPE_IMAGE = "no_type.png";
	private static final String CONSTRUCTOR_IMAGE = "constructor.png";
	private static final String METHOD_PRIVATE_IMAGE = "method_private.png";
	private static final String METHOD_PROTECTED_IMAGE = "method_protected.png";
	private static final String METHOD_PUBLIC_IMAGE = "method_public.png";
	private static final String FINAL_IMAGE = "final.png";
	private static final String STATIC_IMAGE = "static.png";
	private static final String STATIC_FINAL_IMAGE = "static_final.png";
	private static final String ABSTRACT_IMAGE = "abstract.png";
	private static final String NO_MODIFIER_IMAGE = "no_modifier.png";
	private static final String ATTRIBUTE_PRIVATE_IMAGE = "attribute_private.png";
	private static final String ATTRIBUTE_PROTECTED_IMAGE = "attribute_protected.png";
	private static final String ATTRIBUTE_PUBLIC_IMAGE = "attribute_public.png";
	private static final String PACKAGE_IMAGE = "package.png";
	
	private IOutlineAST outlineVisitor;
	private String currentFilePath;
	private Map<String, Image> imageMap;

	public OutlineService() {
		this.outlineVisitor = new OutlineAST();
	}

	/**
	 * Custom constructor for plug-in development
	 * @param customVisitor, the custom visitor.
	 */
	public OutlineService(IOutlineAST customVisitor) {
		this.outlineVisitor = customVisitor;
	}

	/** Parses SourceElement if it is a file, if it's a package does nothing.
	 * @param editorServices, JavaEditorServices that is used to parse the file
	 * @param element, SourceElement to be parsed and visited by the OutlineAST
	**/
	public void parseSelectedSourceElement(JavaEditorServices editorService, SourceElement element) {
		if (!element.isPackage()) {
			parseSelectedFile(editorService, element.getFile());
		}
	}
	
	/** Parses and visits file with OutlineAST.
	 * @param editorServices, JavaEditorServices that is used to parse the file
	 * @param file, File to be parsed and visited by the OutlineAST
	**/
	public void parseSelectedFile(JavaEditorServices editorService, File file) {
		this.outlineVisitor.resetOutlineElementList();
		editorService.parseFile(file, (ASTVisitor)outlineVisitor);
		this.currentFilePath = file.getPath();
	}

	/**
	 * @return IOutlineAST, the AST being used.
	 */
	public IOutlineAST getOutlineVisitor() {
		return outlineVisitor;
	}
	/**
	 * 
	 * @return List<OutlineElement>, returns the current list the AST has stored of OutlineElements.
	 */
	public List<OutlineElement> getOutlineElementList() {
		return outlineVisitor.getOutlineElementList();

	}
	/**
	 * Sets AST to be used by this OutlineService when parsing/visiting.
	 * @param outlineVisitor, a visitor that implements the IOutlineAST interface.
	 */
	public void setOutlineVisitor(IOutlineAST outlineVisitor) {
		this.outlineVisitor = outlineVisitor;
	}

	/**
	 * @return String,the currently selected package's name. Example: "outline.extensibility".
	 */
	public String getCurrentSelectedPackage() {
		return outlineVisitor.getCurrentPackage();
	}

	/**
	 * Returns the current filepath previously acquired using the method "java.io.File.getPath()".
	 * @return String,the currently open file path.
	 */
	public String getCurrentFile() {
		return currentFilePath;
	}

	/**
	 * Returns a list of the current OutlineElements stored by the visitor.
	 * @return List<OutlineElement>, the list of OutlineElements.
	 */
	public List<OutlineElement> getOutlineElements() {
		return outlineVisitor.getOutlineElementList();
	}

	/** For each OutlineElement in a list creates a corresponding TreeItem.
	 * @param outlineElements, list of OutlineElements that will be represented in the Tree.
	 * @param tree, base tree where TreeItems will be added.
	 */
	public void addTreeItems(List<OutlineElement> outlineElements, Tree tree) {
		tree.removeAll();
		TreeItem packageItem = new TreeItem(tree, 0);
		packageItem.setImage(0, this.imageMap.get(PACKAGE_IMAGE));
		packageItem.setText(2, this.getCurrentSelectedPackage());
		for (OutlineElement element : outlineElements) {
			TreeItem treeRoot = new TreeItem(tree, 0);
			this.setRootTypeAndModifierIcons(treeRoot, element);
			treeRoot.setText(2, element.getName());
			addTreeChildren(treeRoot, element);
		}
	}

	/**
	 * Given a root TreeItem, populate it with model data. 
	 * @param treeRoot, a TreeItem that corresponds to an OutlineElement.
	 * @param element, model used to populate the TreeItems with data.
	 */
	private void addTreeChildren(TreeItem treeRoot, OutlineElement element) {
		for (OutlineAttribute attribute : element.getAttributeList()) {
			TreeItem attributeItem = new TreeItem(treeRoot, 0);
			this.setChildrenIcons(attributeItem, attribute);
			attributeItem.setText(2, attribute.getName() + " : " + attribute.getType());
		}
		for (OutlineMethod method : element.getMethodList()) {
			TreeItem methodItem = new TreeItem(treeRoot, 0);
			this.setChildrenIcons(methodItem, method);
			methodItem.setText(2, method.getName() + "() : " + method.getReturnType());
		}

	}

	/**
	 * Sets icons for an outline element's attributes.
	 * @param childItem, the TreeItem corresponding to the OutlineElement's attribute.
	 * @param attribute, model used to populate TreeItem with data.
	 */
	private void setChildrenIcons(TreeItem childItem, OutlineAttribute attribute) {
		this.setChildrenAccessModifierIcon(childItem, attribute);
		this.setChildrenExtraModifierIcon(childItem, attribute);
	}
	
	/**
	 * Sets the access modifier icon. (Public/Protected/Private) 
	 * @param childItem, the TreeItem corresponding to the OutlineElement's attribute.
	 * @param attribute, model used to populate TreeItem with data.
	 */
	private void setChildrenAccessModifierIcon(TreeItem childItem, OutlineAttribute attribute){
		Image accessModifierIcon;
		if (Modifier.isPublic(attribute.getModifiers())) {
			accessModifierIcon = this.imageMap.get(ATTRIBUTE_PUBLIC_IMAGE);
		} else if (Modifier.isProtected(attribute.getModifiers())) {
			accessModifierIcon = this.imageMap.get(ATTRIBUTE_PROTECTED_IMAGE);
		} else if (Modifier.isPrivate(attribute.getModifiers())) {
			accessModifierIcon = this.imageMap.get(ATTRIBUTE_PRIVATE_IMAGE);
		} else {
			accessModifierIcon = this.imageMap.get(NO_MODIFIER_IMAGE);
		}
		childItem.setImage(0, accessModifierIcon);
	}
	
	/**
	 * Sets the extra modifier icon. (Static/Final/etc..)
	 * @param childItem, the TreeItem corresponding to the OutlineElement's attribute.
	 * @param attribute, model used to populate TreeItem with data.
	 */
	private void setChildrenExtraModifierIcon(TreeItem childItem, OutlineAttribute attribute) {
		Image extraModifierIcon;
		if (Modifier.isAbstract(attribute.getModifiers())) {
			extraModifierIcon = this.imageMap.get(ABSTRACT_IMAGE);
		} else if (Modifier.isStatic(attribute.getModifiers())) {
			if (Modifier.isFinal(attribute.getModifiers())) {
				extraModifierIcon = this.imageMap.get(STATIC_FINAL_IMAGE);
			} else {
				extraModifierIcon = this.imageMap.get(STATIC_IMAGE);
			}
		} else if (Modifier.isFinal(attribute.getModifiers())) {
			extraModifierIcon = this.imageMap.get(FINAL_IMAGE);
		} else {
			extraModifierIcon = this.imageMap.get(NO_MODIFIER_IMAGE);
		}
		childItem.setImage(1, extraModifierIcon);
	}

	/**
	 * Sets icons for methods.
	 * @param childItem, the TreeItem corresponding to the OutlineElement's method.
	 * @param method, model used to populate TreeItem with data.
	 */
	private void setChildrenIcons(TreeItem childItem, OutlineMethod method) {
		this.setChildrenAccessModifierIcon(childItem, method);
		this.setChildrenExtraModifierIcon(childItem, method);
	}
	
	/**
	 * Sets the access modifier icon. (Public/Protected/Private)
	 * @param childItem, the TreeItem corresponding to the OutlineElement's method.
	 * @param method, model used to populate TreeItem with data.
	 */
	private void setChildrenAccessModifierIcon(TreeItem childItem, OutlineMethod method) {
		Image accessModifierIcon;
		if (Modifier.isPublic(method.getModifiers())) {
			accessModifierIcon = this.imageMap.get(METHOD_PUBLIC_IMAGE);
		} else if (Modifier.isProtected(method.getModifiers())) {
			accessModifierIcon = this.imageMap.get(METHOD_PROTECTED_IMAGE);
		} else if (Modifier.isPrivate(method.getModifiers())) {
			accessModifierIcon = this.imageMap.get(METHOD_PRIVATE_IMAGE);
		} else {
			accessModifierIcon = this.imageMap.get(NO_MODIFIER_IMAGE);
		}
		childItem.setImage(0, accessModifierIcon);
	}
	
	/**
	 * Sets the extra modifier icon. (Static/Final/etc..) 
	 * @param childItem, the TreeItem corresponding to the OutlineElement's method.
	 * @param method, model used to populate TreeItem with data.
	 */
	private void setChildrenExtraModifierIcon(TreeItem childItem, OutlineMethod method) {
		Image extraModifierIcon;
		if (Modifier.isAbstract(method.getModifiers())) {
			extraModifierIcon = this.imageMap.get(ABSTRACT_IMAGE);
		} else if (Modifier.isStatic(method.getModifiers())) {
			if (Modifier.isFinal(method.getModifiers())) {
				extraModifierIcon = this.imageMap.get(STATIC_FINAL_IMAGE);
			} else {
				extraModifierIcon = this.imageMap.get(STATIC_IMAGE);
			}
		} else if (Modifier.isFinal(method.getModifiers())) {
			extraModifierIcon = this.imageMap.get(FINAL_IMAGE);
		} else if (method.isConstructor()) {
			extraModifierIcon = this.imageMap.get(CONSTRUCTOR_IMAGE);
		} else {
			extraModifierIcon = this.imageMap.get(NO_MODIFIER_IMAGE);
		}
		childItem.setImage(1, extraModifierIcon);
	}

	/**
	 * Sets root TreeItem icons.
	 * @param treeRoot, the TreeItem corresponding to the OutlineElement.
	 * @param element, model used to populate TreeItem with data.
	 */
	private void setRootTypeAndModifierIcons(TreeItem treeRoot, OutlineElement element) {
		setTypeIcon(treeRoot, element);
		setModifierIcon(treeRoot, element);
	}
	
	/**
	 * Sets Element modifier icon. (Final/Abstract).
	 * @param item, the TreeItem corresponding to the OutlineElement.
	 * @param element, model used to populate TreeItem with data.
	 */
	private void setModifierIcon(TreeItem item, OutlineElement element) {
		Image modifierIcon;
		if (Modifier.isFinal(element.getModifiers())) {
			modifierIcon = this.imageMap.get(FINAL_IMAGE);
		} else if (Modifier.isAbstract(element.getModifiers())) {
			modifierIcon = this.imageMap.get(ABSTRACT_IMAGE);
		} else {
			modifierIcon = this.imageMap.get(NO_MODIFIER_IMAGE);
		}

		item.setImage(1, modifierIcon);

	}

	/**
	 * Sets Element type icon. (Class/Interface/Enum)
	 * @param item, the TreeItem corresponding to the OutlineElement.
	 * @param element, model used to populate TreeItem with data.
	 */
	private void setTypeIcon(TreeItem item, OutlineElement element) {
		Image typeIcon;
		if (Objects.isNull(element.getType())) {
			typeIcon = this.imageMap.get(NO_TYPE_IMAGE);
		} else {
			switch (element.getType()) {
			case CLASS:
				typeIcon = this.imageMap.get(CLASS_IMAGE);
				break;
			case INTERFACE:
				typeIcon = this.imageMap.get(INTERFACE_IMAGE);
				break;
			case ENUM:
				typeIcon = this.imageMap.get(ENUM_IMAGE);
				break;
			default:
				typeIcon = this.imageMap.get(NO_TYPE_IMAGE);
				break;
			}
		}
		item.setImage(0, typeIcon);
	}

	/**
	 * Sets the imageMap to be used by the application.
	 * @param imageMap, the image map to be used.
	 */
	public void setImageMap(Map<String, Image> imageMap) {
		this.imageMap = imageMap;
	}

}
