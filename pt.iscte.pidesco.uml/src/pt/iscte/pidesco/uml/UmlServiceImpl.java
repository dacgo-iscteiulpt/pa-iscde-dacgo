package pt.iscte.pidesco.uml;

import pt.iscte.pidesco.uml.service.UmlInterfaceListener;
import pt.iscte.pidesco.uml.service.UmlService;

public class UmlServiceImpl implements UmlService {
	
	@Override
	public void addListener(UmlInterfaceListener listener) {
		Activator.getInstance().addListener(listener);
	}
	@Override
	public void removeListener(UmlInterfaceListener listener) {
		Activator.getInstance().removeListener(listener);
	}
}
