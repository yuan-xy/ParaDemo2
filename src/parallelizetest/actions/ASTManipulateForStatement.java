package parallelizetest.actions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;

public class ASTManipulateForStatement {
	
	public void run(String src,InputStream in,IPath path) {
		Statement selectunit = parse(src);
		ForStatementDetector fsd = new ForStatementDetector();
		byte[] buff = new byte[1024];
		String str = "";
		int len = 0;
			try {
				while((len=in.read(buff))!= -1){
					str = str.concat((new String(buff)).substring(0, len));
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		fsd.setSelectUnit(selectunit);
	//	fsd.process(src,selectunit);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(str.toCharArray());
		parser.setResolveBindings(true);
		
		CompilationUnit cu =  (CompilationUnit)parser.createAST(null);
		IProblem[] pro = cu.getProblems();
		for(int i=0; i < pro.length; i++)
			System.out.println(pro[i].getMessage());
		rewrite(cu,path,fsd);
	}
	
//	protected CompilationUnit parse(ICompilationUnit lwunit)  {
//		ASTParser parser = ASTParser.newParser(AST.JLS3);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setSource(lwunit);
//		parser.setResolveBindings(true);
//		
//		return (CompilationUnit)parser.createAST(null);
//	}

	protected Statement parse(String src)  {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(src.toCharArray());
		parser.setResolveBindings(true);
		
		return (Statement)parser.createAST(null);
	}
	
	private void rewrite(CompilationUnit unit,IPath path,
							ForStatementDetector fsd){
		new ASTRewriteManipulator(fsd).manipulate(unit,path);
	}
}
