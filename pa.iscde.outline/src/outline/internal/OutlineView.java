package outline.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import outline.extensibility.CustomImageMap;
import outline.extensibility.IOutlineService;
import outline.extensibility.OutlineAction;
import outline.extensibility.OutlineService;
import outline.extensibility.models.OutlineAttribute;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;
import pt.iscte.pidesco.uml.service.UmlService;

public class OutlineView implements PidescoView {
	private static final String OUTLINE_ACTIONS_CLASS = "class";
	private static final String OUTLINE_ACTIONS_EXTENSION_PATH = "pa.iscde.outline.actions";
	private static final String CUSTOM_IMAGE_MAP_CLASS = "customImageMap";
	private static final String CUSTOM_IMAGEMAP_EXTENSION_PATH = "pa.iscde.outline.imagemap";
	private static final int INFO_ICON_WIDTH = 70;
	private static final int EXTRA_INFO_ICON_WIDTH = 20;
	private static final int INFO_TEXT_WIDTH = 450;
	private IOutlineService outlineService;
	private Tree outlineTree;
	private Composite baseArea;
	private Group buttonRow;

	public OutlineView() {
		this.outlineService = new OutlineService();
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		baseArea = viewArea;
		GridLayout gridLayout = new GridLayout(1, false);
		viewArea.setLayout(gridLayout);

		ScrolledComposite scrolledComposite = new ScrolledComposite(viewArea, SWT.H_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite innerComposite = new Composite(scrolledComposite, SWT.NONE);
		innerComposite.setLayout(new GridLayout());

		buttonRow = new Group(innerComposite, SWT.NONE);
		buttonRow.setText("Outline Actions");
		buttonRow.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.outlineTree = new Tree(viewArea,
				SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		outlineTree.setHeaderVisible(false);
		outlineTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		TreeColumn iconInfo = new TreeColumn(this.outlineTree, SWT.LEFT);
		iconInfo.setWidth(INFO_ICON_WIDTH);
		TreeColumn iconExtraInfo = new TreeColumn(this.outlineTree, SWT.LEFT);
		iconExtraInfo.setWidth(EXTRA_INFO_ICON_WIDTH);

		TreeColumn textInfo = new TreeColumn(this.outlineTree, SWT.LEFT);
		textInfo.setWidth(INFO_TEXT_WIDTH);

		OutlineListener outlineListener = new OutlineListener(this.outlineService, viewArea, this.outlineTree);

		this.addRefreshButton(buttonRow);
		this.addExtensionsToView(buttonRow, viewArea, this.outlineService, this.outlineTree);

		scrolledComposite.setContent(innerComposite);
		scrolledComposite.setMinSize(innerComposite.computeSize(SWT.DEFAULT, 30));
		buttonRow.pack();
		innerComposite.pack();
		scrolledComposite.pack();

		this.checkForCustomImageMap(imageMap);
		this.addListenerToServices(outlineListener);

	}

	private void checkForCustomImageMap(Map<String, Image> imageMap) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(CUSTOM_IMAGEMAP_EXTENSION_PATH);
		if (elements.length > 0) {
			try {
				IConfigurationElement element = elements[0];
				CustomImageMap map = (CustomImageMap) element.createExecutableExtension(CUSTOM_IMAGE_MAP_CLASS);
				this.outlineService.setImageMap(map.getImageMap());
			} catch (CoreException e) {
				this.outlineService.setImageMap(imageMap);
				e.printStackTrace();
			}
		} else {
			this.outlineService.setImageMap(imageMap);
		}
	}

	private void addListenerToServices(OutlineListener outlineListener) {
		BundleContext context = OutlineActivator.getContext();

		ServiceReference<ProjectBrowserServices> projectBrowserServiceReference = context
				.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projectBrowserService = context.getService(projectBrowserServiceReference);

		ServiceReference<JavaEditorServices> javaEditorServicesReference = context
				.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServices = context.getService(javaEditorServicesReference);

		@SuppressWarnings("unchecked")
		ServiceReference<UmlService> umlServiceReference = (ServiceReference<UmlService>) context
				.getServiceReference(UmlService.class.getName());
		UmlService umlService = (UmlService) context.getService(umlServiceReference);

		projectBrowserService.addListener(outlineListener);
		javaEditorServices.addListener(outlineListener);
		umlService.addListener(outlineListener);
	}

	private void addExtensionsToView(Composite buttonArea, Composite parent, IOutlineService outlineService,
			Tree outlineTree) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(OUTLINE_ACTIONS_EXTENSION_PATH);

		for (IConfigurationElement e : elements) {
			String name = e.getAttribute("name");
			Button b = new Button(buttonArea, SWT.PUSH);
			b.setText(name);
			try {
				OutlineAction action = (OutlineAction) e.createExecutableExtension(OUTLINE_ACTIONS_CLASS);
				b.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						action.run(parent, outlineService.getCurrentFile(), getCurrentlySelectedAttributes(),
								getCurrentlySelectedMethods(), getCurrentlySelectedElements(),
								outlineService.getOutlineElementList());
						parent.layout();
					}

					private List<String> getCurrentlySelectedAttributes() {
						List<String> outputStringList = new ArrayList<String>();
						TreeItem[] selection = outlineTree.getSelection();

						for (TreeItem item : selection) {
							String itemName = item.getText(2).split(":")[0];
							itemName = itemName.substring(0, itemName.length() - 1);

							if (!itemName.endsWith(")")) {
								String itemType = item.getText(2).split(":")[1];
								itemType = itemType.substring(1, itemType.length());

								outputStringList.add(itemType + " " + itemName);
							}
						}
						return outputStringList;
					}

					private List<String> getCurrentlySelectedMethods() {
						List<String> outputStringList = new ArrayList<String>();
						TreeItem[] selection = outlineTree.getSelection();

						for (TreeItem item : selection) {
							String itemName = item.getText(2).split(":")[0];
							itemName = itemName.substring(0, itemName.length() - 1);

							if (itemName.endsWith(")")) {
								String itemType = item.getText(2).split(":")[1];
								itemType = itemType.substring(1, itemType.length());

								outputStringList.add(itemType + " " + itemName);
							}
						}
						return outputStringList;
					}

					private List<String> getCurrentlySelectedElements() {
						List<String> outputStringList = new ArrayList<String>();
						TreeItem[] selection = outlineTree.getSelection();
						for (TreeItem item : selection) {
							String itemName = item.getText(2).split(":")[0];
							itemName = itemName.substring(0, itemName.length() - 1);

							String itemType = item.getText(2).split(":")[1];
							itemType = itemType.substring(1, itemType.length());

							outputStringList.add(itemType + " " + itemName);
						}
						return outputStringList;
					}

				});
			} catch (CoreException e1) {
				e1.printStackTrace();
			}

		}
	}

	private void addRefreshButton(Group buttonRow) {
		Button b = new Button(this.buttonRow, SWT.PUSH);
		b.setText("Refresh Actions");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
	}

	public void refresh() {
		for (Control control : this.buttonRow.getChildren()) {
			control.dispose();
		}
		this.addRefreshButton(this.buttonRow);
		this.addExtensionsToView(this.buttonRow, baseArea, outlineService, outlineTree);
		this.buttonRow.layout();

	}

}