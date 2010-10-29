package parallelizetest.actions;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class ForStatementDetector extends ASTVisitor{

	private ForStatementBindingManager forStatementManagers;
	private String source;
	private Statement selectunit;
	private int[] position;
	private ASTRewrite rewrite;
	private AST ast;
	//private Map<IBinding,ForStatementBindingManager> forStatementManagers;
	
	public ForStatementDetector(){
		source = "";
	}
	
	public boolean visit(ForStatement node){
		forStatementManagers = new ForStatementBindingManager(node);
		forStatementManagers.setInitializer(node.initializers());
		forStatementManagers.setUpdater(node.updaters());
		forStatementManagers.setExpression(node.getExpression());
		forStatementManagers.setBody((Block)node.getBody());
		forStatementManagers.transform(rewrite,ast);
		
		if(!source.equals(""))
		{
			position = genForPosition(node);
			forStatementManagers.setPosition(position);
		}
		return true;
	}
	
	private int[] genForPosition(ForStatement node){
		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(source.toCharArray());
		int start = node.getStartPosition();
		int end = start + node.getLength();
		scanner.resetTo(start, end);

		int token;
		try {
			while((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF)
			{
				switch(token){
				case ITerminalSymbols.TokenNamefor:
					return new int[]{scanner.getCurrentTokenStartPosition(),scanner.getCurrentTokenEndPosition()};
				}
			}
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void process(String src){
		selectunit.accept(this);
		source = src; 
	}
	
	public void process(){
		selectunit.accept(this);
		source = "";
	}
	
	public int[] getPosition(){
		return position;
	}
	
	public void setSelectUnit(Statement unit){
		selectunit = unit;
	}
	
	public void setRewrite(ASTRewrite rewrt){
		rewrite = rewrt;
	}
	
	public void setAST(AST tree){
		ast = tree;
	}
	
	public ForStatementBindingManager getForStatementBindingManager(){
		return forStatementManagers;
	}
}
