package quickconverter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class command_handler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String cmd_para=event.getParameter("Quick_Converter.Parameter1");
		System.out.println(cmd_para);
		if(cmd_para.startsWith("show_QC")) {
			System.out.println(cmd_para);
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("QuickConverter.view1");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
