package pt.iscte.pidesco.uml;

import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.internal.ProjectBrowserActivator;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;
import pt.iscte.pidesco.uml.service.UmlInterfaceListener;
import pt.iscte.pidesco.uml.service.UmlService;

public class Activator implements BundleActivator {
	private static Activator instance;
	private static BundleContext context;
	private Set<UmlInterfaceListener> listeners;
	private ProjectBrowserServices servicesProjS;
	private CodeGeneratorServices servicesCodeG;
	private JavaEditorServices servicesJavaE;
	private ServiceRegistration<?> service;
	private UmlListener listener;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		servicesProjS = null;
		servicesCodeG = null;
		instance = this;

		listeners = new HashSet<UmlInterfaceListener>();
		service = bundleContext.registerService(UmlService.class.getName(), new UmlServiceImpl(), null);

		ServiceReference<ProjectBrowserServices> projS = context.getServiceReference(ProjectBrowserServices.class);
		servicesProjS = context.getService(projS);

		ServiceReference<CodeGeneratorServices> codeGS = context.getServiceReference(CodeGeneratorServices.class);
		servicesCodeG = context.getService(codeGS);

		ServiceReference<JavaEditorServices> javaE = context.getServiceReference(JavaEditorServices.class);
		servicesJavaE = context.getService(javaE);
		

	}

	public ProjectBrowserServices getServicesProjS() {
		return servicesProjS;
	}

	public CodeGeneratorServices getServicesCodeG() {
		return servicesCodeG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		service.unregister();
		servicesProjS.removeListener(listener);
		listeners.clear();
	}

	public static Activator getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public void addListener(UmlInterfaceListener listener) {
		listeners.add(listener);

	}

	public void addUmlListener(UmlListener listener) {
		this.listener = listener;
	}

	public void removeListener(UmlInterfaceListener listener) {
		listeners.remove(listener);

	}

	public Set<UmlInterfaceListener> getListeners() {
		return listeners;
	}

	public JavaEditorServices getServicesJavaEditor() {
		return servicesJavaE;
	}

}
