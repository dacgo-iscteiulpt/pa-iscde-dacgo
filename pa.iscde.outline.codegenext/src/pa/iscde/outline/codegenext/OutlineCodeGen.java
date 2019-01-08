package pa.iscde.outline.codegenext;

import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import outline.extensibility.OutlineAction;
import outline.extensibility.models.OutlineElement;
import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorServicesImpl;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class OutlineCodeGen implements OutlineAction {

	@Override
	public void run(Composite area, String filePath, List<String> selectedAttributes, List<String> selectedMethods,
			List<String> selectedElements, List<OutlineElement> allElements) {
		CodeGeneratorServicesImpl codeGenService = new CodeGeneratorServicesImpl();
		BundleContext context = OutlineCodeGenActivator.getContext();
		ServiceReference<JavaEditorServices> javaEditorServicesReference = context
				.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServices = context.getService(javaEditorServicesReference);
		codeGenService.addSettersAndGetters(filePath, selectedAttributes, javaEditorServices);	
	}

}
