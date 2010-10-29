package parallelizetest.actions;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.corext.dom.SelectionAnalyzer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class Parallelize1Action implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private ISelection selection;
	/**
	 * The constructor.
	 */
	public Parallelize1Action() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	
	private class MySelectionListner implements ISelectionListener{

		public void selectionChanged(IWorkbenchPart part, ISelection sel) {
			if(sel instanceof ITextSelection)
				selection = sel;
		}
		
	}
	
	public void run(IAction action) {
		
	//	if(selection instanceof ITextSelection){
			//if(selection instanceof IStructuredSelection){
			//ICompilationUnit lwunit = (ICompilationUnit)((IStructuredSelection)selection).getFirstElement();
			//createActionExuecutable(action.getId()).run(lwunit);
			//String source = ((TextSelection)selection).getText();
		//selection = window.getSelectionService().getSelection();
		IEditorPart editor = window.getWorkbench().getActiveWorkbenchWindow()
        .getActivePage().getActiveEditor();
		
		IEditorInput input = editor.getEditorInput();
		IFile file = null;
        if(input instanceof IFileEditorInput){
              file = ((IFileEditorInput)input).getFile();
        }

		IEditorSite site = editor.getEditorSite();
		selection = site.getSelectionProvider().getSelection();
		if (selection instanceof TextSelection) {
			String source = ((TextSelection)selection).getText();
			try {
				createActionExuecutable(action.getId()).run(source,file.getContents(),file.getFullPath());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	//	}
	}
	
	private ASTManipulateForStatement createActionExuecutable(String id) {
		if ("parallelizetest.actions.ParallelizeAction1".equals(id)) {
			return new ASTManipulateForStatement();
		} else {
			throw new IllegalArgumentException(id);
		}
	}


	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection select) {
		if(select instanceof TextSelection)
			selection = (TextSelection)select;
	//	this.selection = selection;
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
		MySelectionListner mylistener = new MySelectionListner();
		window.getSelectionService().addSelectionListener(mylistener);
	}
}