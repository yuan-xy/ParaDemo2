package parallelizetest.actions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.PostfixExpression.Operator;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;


public class ForStatementBindingManager {

	private final ForStatement forStatement;
	private Map<SimpleName,int[]> iterationVars;
	private List<Assignment> initializer;
	//private List<VariableDeclarationExpression> initializer;
	//such as int i=0,j=0;
	//private InfixExpression expression;
	private Expression condition;
	//private List<Expression> condition;
	private List<PostfixExpression> updater;
	//private List<Expression> updater;  
	//private List<Assignment> updater;
	//such as i += 2;
	private Block body;
	private int[] forStatementPosition;
	private Javadoc directives;
	
	public ForStatementBindingManager(ForStatement forstatement){
		forStatement = forstatement;
		initializer = new ArrayList<Assignment>();
		updater = new ArrayList<PostfixExpression>();
		iterationVars = new LinkedHashMap<SimpleName,int[]>();
	}
	
	public int getIterationLowerBound(Assignment node){
		int[] arr = new int[3];
		if(node.getLeftHandSide() instanceof SimpleName){
			
			arr[0] = ((Integer)node.getRightHandSide().resolveConstantExpressionValue());
			iterationVars.put((SimpleName)node.getLeftHandSide(), 
					arr);
		}
		return arr[0];
	}
	
	public int getIterationUpperBound(Expression cond){
		int arr[] = new int[3];
		if(cond instanceof InfixExpression){
			if(((InfixExpression)cond).getLeftOperand()
					instanceof SimpleName)
			{
				 arr = iterationVars.get((SimpleName)((InfixExpression)cond)
						.getLeftOperand());
				arr[1] = (Integer)((InfixExpression)cond).getRightOperand()
						.resolveConstantExpressionValue();
			}
		}
		return arr[1];
	}
	
	public int getStep(PostfixExpression node){
		int arr[] = new int[3];
		if(node.getOperand() instanceof SimpleName){
//			if(node.getOperator().equals(Operator.INCREMENT))
//			{
//				arr = iterationVars.get((SimpleName)(node.getOperand()));
//				arr[2] = 1;
//			}
//			else if(node.getOperator().equals(Operator.DECREMENT))
//			{
//				arr = iterationVars.get((SimpleName)(node.getOperand()));
//				arr[2] = -1;
//			}
			arr = iterationVars.get((SimpleName)(node.getOperand()));
			arr[2] = 1;
		}
//		return arr[2];
		return 1;
	}
	
	//analyze the dependence type,array var or scalar var
	
	public List<Assignment> getAllAssignments(Block b){
		List<Statement> statements = b.statements();
		List<Assignment> assignments = new ArrayList<Assignment>();
		Iterator ite = statements.iterator();
		while(ite.hasNext()){
			ExpressionStatement es = (ExpressionStatement)ite.next();
			if(es.getExpression() instanceof Assignment)
				assignments.add((Assignment)es.getExpression());
		}
		return assignments;
		//return null;
	}
	
	private void analyze(){
		
	}
	
	public void transform(ASTRewrite rewrt,AST tree){
		analyze();
		addHelpAssist(rewrt,tree);
		
	}
	
	public void addHelpAssist(ASTRewrite rewrt,AST tree){
//		AST node = forStatement.getAST();
//		directives = node.newJavadoc();
//		TagElement tag = node.newTagElement();
//		TextElement te = node.newTextElement();
//		te.setText("#pragma omp parallel for");
//		tag.fragments().add(te);
//		directives.tags().add(tag);
//		directives.setAlternateRoot(forStatement);
		ForStatement old = forStatement;
		ForStatement newfor = tree.newForStatement();
		newfor.setBody(body);
		newfor.setExpression(condition);
		newfor.initializers().addAll(initializer);
		newfor.updaters().addAll(updater);
		newfor.setLeadingComment("/*#pragma omp parallel for*/");
		rewrt.replace(old, newfor, null);
	}
	
	public Javadoc getHelpAssist(){
		return directives;
	}
	
//	public void transform(AST node){
//		Javadoc jc = node.newJavadoc();
//		TagElement tag = node.newTagElement();
//		TextElement te = node.newTextElement();
//		te.setText("#pragma omp parallel for");
//		tag.fragments().add(te);
//		jc.tags().add(tag);
//		jc.setAlternateRoot(forStatement);
//	}
	
	public void setInitializer(List<Assignment> init){
		if(!initializer.isEmpty())
			initializer.clear();
		initializer.addAll(init);
	}
	
	public void setItarationVariable(SimpleName var){
		
	}
	
	public void setExpression(Expression exp){
		condition = exp;
	}
	
	public void setUpdater(List<PostfixExpression> upd){
		if(!updater.isEmpty())
			updater.clear();
		updater.addAll(upd);
	}
	
	public void setBody(Block b){
		body = b;
	}
	
	public ForStatement getForStatement(){
		return forStatement;
	}
	
	public void setPosition(int[] pos){
		forStatementPosition = pos;
	}
	
	public int[] getPosition(){
		return forStatementPosition;
	}
}
