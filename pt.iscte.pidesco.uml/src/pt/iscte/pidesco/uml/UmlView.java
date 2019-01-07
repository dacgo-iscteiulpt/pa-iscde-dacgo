package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.projectbrowser.internal.ProjectBrowserServicesImpl;

public class UmlView implements PidescoView {
	private List<UmlClass> umlClassList;
	private ArrayList<Dependency> dependencyList;
	private GraphViewer graphViewArea;

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {

		graphViewArea = new GraphViewer(viewArea, SWT.BORDER);
		graphViewArea.setContentProvider(new ZestNodeContentProvider());
		graphViewArea.setLabelProvider(new ZestFigureProvider());

		graphViewArea.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		graphViewArea.applyLayout();

		UmlListener listener = new UmlListener(this);
		Activator.getInstance().addUmlListener(listener);
		Activator.getInstance().getServicesProjS().addListener(listener);

	}

	void addContent(List<UmlClass> umlClassList, ArrayList<Dependency> dependencyList) {
		this.umlClassList = umlClassList;
		this.dependencyList = dependencyList;
		graphViewArea.setInput(umlClassList);
		graphViewArea.applyLayout();
	}

	class ZestNodeContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {
		@Override
		public Object[] getConnectedTo(Object entity) {
			UmlClass parentClass = (UmlClass) entity;
			ArrayList<Object> umlDependencyList = new ArrayList<Object>();
			for (Dependency dependency : dependencyList) {
				if (dependency.getChildClass().equals(entity)) {
					for (UmlClass parent : umlClassList) {
						if (dependency.getParentNameClass().equals(parent.getClassName())) {
							umlDependencyList.add(parent);
						}
					}

				}
			}
			return umlDependencyList.toArray();
		}
	}

	class ZestFigureProvider extends LabelProvider
			implements IFigureProvider, IEntityConnectionStyleProvider, ISelfStyleProvider {
		private UmlClass umlClass;

		@Override
		public IFigure getFigure(Object element) {
			umlClass = (UmlClass) element;
			return new UmlFigure((UmlClass) element);
		}

		@Override
		public String getText(Object element) {
			return "";
		}

		@Override
		public IFigure getTooltip(Object entity) {
			return null;
		}

		@Override
		public void selfStyleConnection(Object element, GraphConnection connection) {
			PolylineConnection connectionFig = (PolylineConnection) connection.getConnectionFigure();
			PolygonDecoration decoration = new PolygonDecoration();
			decoration.setScale(20, 10);
			decoration.setLineWidth(2);
			decoration.setOpaque(true);
			decoration.setBackgroundColor(ColorConstants.white);
			connectionFig.setTargetDecoration(decoration);
		}

		@Override
		public void selfStyleNode(Object element, GraphNode node) {

		}

		@Override
		public int getConnectionStyle(Object src, Object dest) {
			UmlClass parent = (UmlClass) dest;
			if (parent.getClassType().equals("interface")) {
				return ZestStyles.CONNECTIONS_DASH;
			} else {
				return ZestStyles.CONNECTIONS_DIRECTED;
			}

		}

		@Override
		public Color getColor(Object src, Object dest) {
			// TODO Auto-generated method stub
			return ColorConstants.red;
		}

		@Override
		public Color getHighlightColor(Object src, Object dest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLineWidth(Object src, Object dest) {
			// TODO Auto-generated method stub
			return 1;
		}
	}

}
