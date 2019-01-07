package pa.iscde.outline.testaction;


import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import outline.extensibility.OutlineAction;

public class OutlineExtraAction implements OutlineAction {

	@Override
	public void run(Composite area, String filePath, List<String> selectedAttributes, List<String> selectedMethods,
			List<String> selectedElements) {
		new Label(area, SWT.NONE).setText("!!!");
		
	}

}
