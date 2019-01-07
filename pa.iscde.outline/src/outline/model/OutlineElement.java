package outline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OutlineElement {
	private List<OutlineAttribute> attributeList;
	private List<OutlineMethod> methodList;
	private String name;
	private String packageName;
	private OutlineElementType type;
	private int modifiers;

	public List<OutlineAttribute> getAttributeList() {
		if(Objects.isNull(this.attributeList)){
			this.attributeList = new ArrayList<OutlineAttribute>();
		}
		return attributeList;
	}

	public void setAttributeList(List<OutlineAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public List<OutlineMethod> getMethodList() {
		if(Objects.isNull(this.methodList)){
			this.methodList = new ArrayList<OutlineMethod>();
		}
		return methodList;
	}

	public void setMethodList(List<OutlineMethod> methodList) {
		this.methodList = methodList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OutlineElementType getType() {
		return type;
	}

	public void setType(OutlineElementType type) {
		this.type = type;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
}
