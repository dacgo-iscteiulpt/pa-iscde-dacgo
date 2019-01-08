package pa.iscde.outline.testaction;


import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import outline.extensibility.OutlineAction;
import outline.extensibility.models.OutlineElement;

public class OutlineExtraAction implements OutlineAction {

	@Override
	public void run(Composite area, String filePath, List<String> selectedAttributes, List<String> selectedMethods,
			List<String> selectedElements, List<OutlineElement> allElements) {
		new Label(area, SWT.NONE).setText("!!!");
	}

}
