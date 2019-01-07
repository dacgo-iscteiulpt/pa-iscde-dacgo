package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;

public class UmlListener implements ProjectBrowserListener {
	private UmlView umlview;
	ArrayList<UmlClass> umlClassList = new ArrayList<UmlClass>();
	ArrayList<Dependency> dependencyList = new ArrayList<Dependency>();

	public UmlListener(UmlView umlView) {
		this.umlview = umlView;
	}

	@Override
	public void doubleClick(SourceElement element) {
		umlClassList.clear();
		dependencyList.clear();
		System.out.println(element.getParent() + "." + element.getName().replaceAll(".java", ""));
		System.out.println(element.getFile());
		BundleContext context = Activator.getContext();
		JavaEditorServices servicesJavaE = Activator.getInstance().getServicesJavaEditor();

		if (element.isPackage()) {
			System.out.println(element.getName());
			iterateFrom((PackageElement) element, servicesJavaE);
		} else {
			while (!element.isPackage()) {
				System.out.println(element.getName());
				element = element.getParent();
			}
			iterateFrom((PackageElement) element, servicesJavaE);
		}

	}

	private void iterateFrom(PackageElement packageElement, JavaEditorServices servicesJavaE) {
		for (SourceElement iterable_element : packageElement.getChildren()) {
			if (iterable_element.isPackage()) {
				iterateFrom((PackageElement) iterable_element, servicesJavaE);
			} else {
				System.out.println("ite " + iterable_element.getName());
				AST astvisitor = new AST(umlClassList, dependencyList, iterable_element);
				servicesJavaE.parseFile(iterable_element.getFile(), astvisitor);
			}

		}
		for (UmlClass umlClass : umlClassList) {
			System.out.println(umlClass.getClassName());
			System.out.println(umlClass.getMethods().toString());
			System.out.println(umlClass.getVariables().toString());
		}
		boolean exist = false;
		for (Dependency dependency : dependencyList) {
			exist = false;
			System.out.println(dependency.toString());
			for (UmlClass umlClass : umlClassList) {
				if (umlClass.getClassName().equals(dependency.getParentNameClass())) {
					exist = true;
				}

			}
			if (!exist) {
				UmlClass umlClassParent = new UmlClass(null);
				umlClassParent.setClassName(dependency.getParentNameClass());
				umlClassList.add(umlClassParent);
			}
		}

		umlview.addContent(umlClassList, dependencyList);

	}

	@Override
	public void selectionChanged(Collection<SourceElement> selection) {
		// TODO Auto-generated method stub

	}

}
