package outline.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class OutlineActivator implements BundleActivator {
	private static OutlineActivator instance;
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	public void start(BundleContext bundleContext) throws Exception {
		OutlineActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		OutlineActivator.context = null;
	}
	
	public static OutlineActivator getInstance() {
		return instance;
	}

}
