package outline.extensibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import outline.model.OutlineAttribute;
import outline.model.OutlineElement;
import outline.model.OutlineElementType;
import outline.model.OutlineMethod;

public class OutlineAST extends ASTVisitor implements IOutlineAST {
	private String currentPackage;
	private OutlineElement currentOutlineElement;
	private List<OutlineElement> outlineElementList;

	private static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}

	public OutlineAST() {
		super();
		this.outlineElementList = new ArrayList<OutlineElement>();
	}

	// visits package declaration
	@Override
	public boolean visit(PackageDeclaration node) {
		this.currentPackage = node.getName().toString();
		return true;			
	}

	// visits enum declaration
	@Override
	public boolean visit(EnumDeclaration node) {
		System.out.println("Started Examining: " + node.getName());
		this.currentOutlineElement = new OutlineElement();
		this.currentOutlineElement.setPackageName(this.currentPackage);
		String name = Objects.isNull(node.getName()) ? "" : node.getName().toString();
		this.currentOutlineElement.setName(name);
		this.currentOutlineElement.setType(OutlineElementType.ENUM);

		return true;
	}

	// visits class/interface declaration, sets OutlineElement type (Interface/Class).
	@Override
	public boolean visit(TypeDeclaration node) {
		System.out.println("Started Examining: " + node.getName());
		this.currentOutlineElement = new OutlineElement();
		this.currentOutlineElement.setPackageName(this.currentPackage);
		String name = Objects.isNull(node.getName()) ? "" : node.getName().toString();
		this.currentOutlineElement.setName(name);

		if (node.isInterface()) {
			this.currentOutlineElement.setType(OutlineElementType.INTERFACE);
		} else {
			this.currentOutlineElement.setType(OutlineElementType.CLASS);
			this.currentOutlineElement.setModifiers(node.getModifiers());
		}
		return true;
	}

	// when it stops visiting a class/interface element, it adds the populated model to the list of elements.
	@Override
	public void endVisit(TypeDeclaration node) {
		this.outlineElementList.add(this.currentOutlineElement);
		super.endVisit(node);
	}

	// when it stops visiting an enum element, it adds the populated model to the list of elements.
	@Override
	public void endVisit(EnumDeclaration node) {
		this.outlineElementList.add(this.currentOutlineElement);
		super.endVisit(node);
	}

	// visits attributes
	@Override
	public boolean visit(FieldDeclaration node) {

		// loop for several variables in the same declaration
		for (Object o : node.fragments()) {
			OutlineAttribute attribute = new OutlineAttribute();

			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			String attributeName = Objects.isNull(var.getName()) ? "" : var.getName().toString();
			String attributeType = Objects.isNull(node.getType()) ? "" : node.getType().toString();

			attribute.setName(attributeName);
			attribute.setType(attributeType);
			attribute.setModifiers(node.getModifiers());

			this.currentOutlineElement.getAttributeList().add(attribute);
		}
		return false;
	}

	// visits methods
	@Override
	public boolean visit(MethodDeclaration node) {
		
		OutlineMethod outlineMethod = new OutlineMethod();
		String methodName = Objects.isNull(node.getName()) ? "" : node.getName().toString();
		String returnType = Objects.isNull(node.getReturnType2()) ? "" : node.getReturnType2().toString();

		outlineMethod.setName(methodName);
		outlineMethod.setReturnType(returnType);
		outlineMethod.setModifiers(node.getModifiers());
		outlineMethod.setConstructor(node.isConstructor());
		
		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> parameters = node.parameters();
		if (parameters.size() > 0) {
			for (SingleVariableDeclaration parameter : parameters) {
				String parameterType = Objects.isNull(parameter.getType()) ? "" : parameter.getType().toString();
				outlineMethod.getParameterTypes().add(parameterType);
			}
		}

		this.currentOutlineElement.getMethodList().add(outlineMethod);
		return true;
	}

	//Resets the element list stored by the AST
	public void resetOutlineElementList() {
		this.outlineElementList.clear();
	}
	
	//Returns all OutlineElements.
	public List<OutlineElement> getOutlineElementList() {
		return outlineElementList;
	}

	//Get the current package's name. Example: "outline.extensibility".
	public String getCurrentPackage() {
		return currentPackage;
	}

}
