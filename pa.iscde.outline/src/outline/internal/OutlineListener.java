package outline.internal;

import java.io.File;
import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import outline.extensibility.OutlineService;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.uml.service.UmlInterfaceListener;

public class OutlineListener implements ProjectBrowserListener, JavaEditorListener,UmlInterfaceListener{

	private OutlineService outlineService;
	private Composite viewArea;
	private Tree tree;
	private JavaEditorServices editorServices;
	private String activeFile;

	public OutlineListener(OutlineService outlineService, Composite viewArea, Tree tree) {
		BundleContext context = OutlineActivator.getContext();
		ServiceReference<JavaEditorServices> javaE = context.getServiceReference(JavaEditorServices.class);

		this.editorServices = context.getService(javaE);
		this.viewArea = viewArea;
		this.outlineService = outlineService;
		this.tree = tree;
		this.activeFile = "";
	}

	@Override
	public void doubleClick(SourceElement element) {
		if(!this.activeFile.equals(element.getFile().getAbsolutePath())) {
			this.outlineService.parseSelectedSourceElement(this.editorServices, element);
			this.outlineService.addTreeItems(this.outlineService.getOutlineElementList(), this.tree);
			this.activeFile = element.getFile().getAbsolutePath();
			
			viewArea.layout();
		}

	}

	@Override
	public void selectionChanged(Collection<SourceElement> selection) {
	}

	@Override
	public void fileOpened(File file) {
	}

	@Override
	public void fileSaved(File file) {
	}

	@Override
	public void fileClosed(File file) {
	}

	@Override
	public void selectionChanged(File file, String text, int offset, int length) {
		if(!this.activeFile.equals(file.getAbsolutePath())){
			this.outlineService.parseSelectedFile(editorServices, file);
			this.outlineService.addTreeItems(this.outlineService.getOutlineElementList(), this.tree);
			this.activeFile = file.getAbsolutePath();			
			viewArea.layout();
		}		
		System.out.println(file.getName() + " selected.");
	}

}