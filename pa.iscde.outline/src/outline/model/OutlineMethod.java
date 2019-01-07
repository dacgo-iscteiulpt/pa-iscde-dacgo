package outline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OutlineMethod {
	private int modifiers;
	private String returnType;
	private String name;
	private List<String> parameterTypes;
	private boolean isConstructor; 

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParameterTypes() {
		if (Objects.isNull(parameterTypes)) {
			this.parameterTypes = new ArrayList<String>();
		}
		return parameterTypes;
	}

	public void setParameterTypes(List<String> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

}