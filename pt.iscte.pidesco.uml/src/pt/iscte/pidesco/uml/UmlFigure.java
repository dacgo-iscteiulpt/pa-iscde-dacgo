package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.internal.ProjectBrowserActivator;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.uml.service.UmlInterfaceListener;

public class UmlFigure extends Figure {

	public static Color classColor = new Color(null, 255, 255, 206);
	private CompartmentFigure attributeFigure = new CompartmentFigure();
	private CompartmentFigure methodFigure = new CompartmentFigure();
	private int size;

	public UmlFigure(UmlClass umlClass) {

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);
		size = 100;
		MouseListenerUml mouseListener = new MouseListenerUml(umlClass);
		Label name = new Label(umlClass.getClassName());
		name.addMouseListener(mouseListener);
		attributeFigure.addComponents(umlClass.getVariables());
		attributeFigure.addMouseListener(mouseListener);
		methodFigure.addComponents(umlClass.getMethods());
		methodFigure.addMouseListener(mouseListener);
		setSize(200, size);
		add(name);
		add(attributeFigure);
		add(methodFigure);

	}

	public class MouseListenerUml implements MouseListener {
		private UmlClass umlClass;

		public MouseListenerUml(UmlClass umlClass) {
			this.umlClass = umlClass;
		}

		@Override
		public void mousePressed(MouseEvent me) {

		}

		@Override
		public void mouseReleased(MouseEvent me) {

		}

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			for(UmlInterfaceListener l : Activator.getInstance().getListeners()) {
				l.doubleClick(umlClass.getSourceElement());
			}
			Activator.getInstance().getServicesJavaEditor().openFile(umlClass.getSourceElement().getFile());

			System.out.println(umlClass.getSourceElement());

		}
	}

	public class CompartmentFigure extends Figure {

		public CompartmentFigure() {
			ToolbarLayout layout = new ToolbarLayout();
			layout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
			layout.setStretchMinorAxis(true);
			layout.setSpacing(2);
			setLayoutManager(layout);
			setBorder(new CompartmentFigureBorder());
		}

		public void addComponents(List list) {
			Label label = new Label();
			String labelString = "";
			;
			for (Object object : list) {

				labelString += (object.toString() + "\n");
				size += 20;
			}
			setSize(200, size);
			label.setText(labelString);
			add(label);

		}

		public class CompartmentFigureBorder extends AbstractBorder {
			public Insets getInsets(IFigure figure) {
				return new Insets(1, 0, 0, 0);
			}

			public void paint(IFigure figure, Graphics graphics, Insets insets) {
				graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
			}
		}
	}
}
