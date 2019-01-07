package pa.iscde.outline.codegenext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class OutlineCodeGenActivator implements BundleActivator {

	public static OutlineCodeGenActivator instance;
	private static BundleContext context;
	private JavaEditorServices javaEditorServices;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		OutlineCodeGenActivator.context = bundleContext;
		ServiceReference<JavaEditorServices> editorReference = context.getServiceReference(JavaEditorServices.class);
		javaEditorServices = context.getService(editorReference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		OutlineCodeGenActivator.context = null;
	}
	
	public static OutlineCodeGenActivator getInstance() {
		return instance;
	}
	
	public JavaEditorServices getJavaEditorServices() {
		return javaEditorServices;
	}
	

}
