package parallelizetest.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class ASTRewriteManipulator {
	private ForStatementDetector fsd;
	private ASTRewrite rewrite;
	
	public ASTRewriteManipulator(ForStatementDetector fsdetector){
		fsd = fsdetector;
	}
	
	protected void beforeManipulate(CompilationUnit unit){
		rewrite = ASTRewrite.create(unit.getAST());
	}
	
	protected void afterManipulate(CompilationUnit unit,IPath path){
		try {
			ManipulatorHelper.saveASTRewriteContents(unit, rewrite,path);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void addLoopParallelInfo(CompilationUnit unit,ForStatementBindingManager manager){
//		Iterator tyIter = unit.types().iterator();
//		manager.addHelpAssist();
		
		
//		while(tyIter.hasNext()){
//			Object obj = tyIter.next();
//			if(obj instanceof ForStatement)
//			{
//				rewrite.replace((ForStatement)obj, manager.getForStatement(), null);
//			}
//		}
//		SimpleName oldname = ((TypeDeclaration)unit.types().get(0)).getName();
//		SimpleName newName = unit.getAST().newSimpleName("test2");
//		rewrite.replace(oldname, newName, null);
		
	}
	
	public void manipulate(final CompilationUnit unit,
			//List<ForStatementBindingManager> managers){
			IPath path){
		try{
			unit.recordModifications();
			
		beforeManipulate(unit);
		fsd.setRewrite(rewrite);
		fsd.setAST(unit.getAST());
		fsd.process();
		//addLoopParallelInfo(unit,manager);
		
		afterManipulate(unit,path);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}
