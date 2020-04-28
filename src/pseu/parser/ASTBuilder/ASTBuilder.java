package pseudo.parser.ASTBuilder;

import java.beans.Expression;
import java.util.Stack;
import pseu.common.SourceCoords;
import pseu.interpreter.ASTSourceI;
import pseu.parser.ASTBuilderI;

public class ASTBuilder implements ASTBuilderI, ASTSourceI<ASTNode> {
		
	private Stack<ASTNode> stack = new Stack<ASTNode>();
	
	@Override
	public void mkIntConstant(String i, SourceCoords coords) {
		// Creates and instantiate a new Int constant expression
		
		IntConstant intLit = new IntConstant(i,coords);
		stack.push(intLit);
		
	}

	@Override
	public void mkStringConstant(String str, SourceCoords coords) {
		// Creates and instantiate a new String constant expression
		
		StringConstant stringLit = new StringConstant(str,coords);
		stack.push(stringLit);
	}

	@Override
	public void mkTrue(SourceCoords coords) {
		// Make an expression for true boolean type
		
		TrueExp trueExp = new TrueExp(coords);
		stack.push(trueExp);
	}

	@Override
	public void mkFalse(SourceCoords coords) {
		// Make an expression for false boolean type
		
		FalseExp falseExp = new FalseExp(coords);
		stack.push(falseExp);
	}

	@Override
	public void mkVariable(String name, SourceCoords coords) {
		// Make a variable type
		VariableExp varExp = new VariableExp(name,coords);
		stack.push(varExp);
	}

	@Override
	public void mkFetch() {
		// Pop the first element out of the stack and and make a fetch expression with that
		// then push the fetch expression onto that
		
		ASTNode fetchExp = new FetchExp((Exp)stack.pop());
		stack.push(fetchExp);
		
	}

	@Override
	public void mkSet(int count, SourceCoords coords) {
		// Pop count number of elements out of the stack and make a set out of that 
		
		Stack<Exp> setStack = new Stack<Exp>();
		for (int i = 0; i < count; i++){
			Exp temp_exnode = (Exp)stack.pop();
			setStack.push(temp_exnode);
		}
		
		ASTNode setExp = new SetExp(setStack,coords);
		stack.push(setExp);
	}

	@Override
	public void mkSequence(int count, SourceCoords coords) {
		// Pop count number of elements out of the stack and make a sequence out of that
		
		Stack<Exp> seq = new Stack<Exp>();
		
		for (int i = 0; i < count; i++){
			Exp temp_exnode = (Exp)stack.pop();
			seq.push(temp_exnode);
		}
		
		ASTNode seqExp = new SequenceExp(seq,coords);
		stack.push(seqExp);
	}

	@Override
	public void mkTuple(int count, SourceCoords coords) {
		// Make a tuple data type
		Stack<Exp> tuple = new Stack<Exp>();
		
//		if(count == 1) {
//			assertTrue("Cannot make a tule with size 1",false);
//		}
		
		for (int i = 0; i < count; i++){
			Exp temp_tuple = (Exp) stack.pop();
			tuple.push(temp_tuple);
		}
		
		ASTNode tupleType = new TupleExprestion(tuple,coords);
		stack.push(tupleType);
	}	

	@Override
	public void mkLookup(String name, SourceCoords coords) {
		// Make a lookup expression to store the operation on a variable
		Exp operand = (Exp)stack.pop();
		LookupExp lookUp = new LookupExp(operand,name,coords);
		stack.push(lookUp);

	}

	@Override
	public void mkApplication(SourceCoords coords) {
		// Make an application expression with the tuple 
		TupleExprestion tuple = (TupleExprestion)stack.pop();
		Exp funName = (Exp) stack.pop();
		
		ASTNode application = new Application(tuple, funName, coords);
		stack.push(application);
	}

	@Override
	public void mkFunctionExp(int count, boolean isTyped, SourceCoords coords) {
		// Builds a function expression with the given block
		Stack<ParameterExp> funExprList = new Stack<ParameterExp>(); 
		IdentifierType resultType = null;
		
		Block funBlock = (Block) stack.pop();
		
		if (isTyped) {
			resultType = (IdentifierType) stack.pop();
		}
		
		
		for (int i = 0; i < count; i++) {
			IdentifierType type = (IdentifierType) stack.pop();
			VariableExp id = (VariableExp )stack.pop();
			
			ParameterExp param = new ParameterExp(id, type);
			funExprList.push(param);
			
		}
		
		ParametersExp params = new ParametersExp(funExprList);
		
		ASTNode funExprNode = new FunctionExpression(funBlock,resultType,params,coords);
		stack.push(funExprNode);
	}

	@Override
	public void mkAssignment(int count, SourceCoords coords) {
		// Make an assignment expression
		
		Exp rhs = (Exp) stack.pop();
		
		Stack<VariableExp> assignmentList = new Stack<VariableExp>(); 
		
		for (int i = 0; i < count; i++) {
			assignmentList.push((VariableExp)stack.pop());
		}
		
		ASTNode assi = new AssignmentCommand(rhs, assignmentList, count, coords);
		stack.push(assi);
	}

	@Override
	public void mkIf(SourceCoords coords) {
		// Make an if statement popping out three elements from the stack
		
		Block C = (Block) stack.pop();
		Block B = (Block) stack.pop();
		Exp X = (Exp) stack.pop();
		
		IfCommand ifInstance = new IfCommand(X, B, C,coords);
		
		stack.push(ifInstance);
	}

	@Override
	public void mkWhile(SourceCoords coords) {
		// This function makes while loop into a command
		
		Block B = (Block) stack.pop();
		Application X = (Application) stack.pop();
		ASTNode whileInstance = new WhileCommand(X, B,coords);
		
		stack.push(whileInstance);
	}

	@Override
	public void mkFor(SourceCoords coords) {
		// This function makes for loop into a command
		Block B = (Block) stack.pop();
		Exp X =  (Exp) stack.pop();
		VariableExp variable = (VariableExp) stack.pop();
		
		ASTNode forExp = new ForCommand(B, X, variable,coords);
		
		stack.push(forExp);
	}

	@Override
	public void mkReturn(SourceCoords coords) {
		// This returns from the function makes expression into command
		
		Exp C = (Exp) stack.pop();
		ASTNode returnExp = new ReturnCommand(C,coords);
		
		stack.push(returnExp);	
	}

	@Override
	public void mkExpressionCommand() {
		// This function makes expression into command
		
		Application app = (Application) stack.pop();
		ASTNode expCommand = new ExpCommand(app);
		
		stack.push(expCommand);
	}

	@Override
	public void valDeclaration(boolean isTyped, SourceCoords coords) {
		// Makes value deceleration is the variable is initialized then set that as well
		TypeNode type = new IdentifierType("Any", coords);
		
		Exp rightHandSide = (Exp) stack.pop();
	
		if (isTyped) {
			
			type = (TypeNode) stack.pop();
		}
		
		VariableExp variable = (VariableExp) stack.pop();
		ASTNode valDecleration = new ValDecleration(type, rightHandSide, variable,coords);
		
		stack.push(valDecleration);
	}

	@Override
	public void varDeclaration(boolean isInitialized, SourceCoords coords) {
		// Makes variable deceleration is the variable is initialized then set that as well
		Exp rightHandSide = null;
		
		if (isInitialized) {
			rightHandSide = (Exp) stack.pop();
		}
		
		IdentifierType type = (IdentifierType) stack.pop();
		
		VariableExp variable = (VariableExp) stack.pop();
		ASTNode valDecleration = new VarDecleration(type, rightHandSide, variable, coords);
		
		stack.push(valDecleration);
	}

	@Override
	public void mkIdentifierType(String name, SourceCoords coords) {
		// Make an identifier type
		
		IdentifierType idType = new IdentifierType(name,coords);
		stack.push(idType);

	}

	@Override
	public void mkTypeApplication(String name, int count, SourceCoords coords) {
		// Makes an application type with the given name and the set of arguments in it 
		
		Stack<ASTNode> argList = new Stack<ASTNode>();
		
		for (int i=0; i < count; i++) {
			argList.push(stack.pop());
		}
		
		ASTNode appType = new ApplicationType(name, argList, coords);
		stack.push(appType);
	}

	@Override
	public void startBlock() {
		// To be decided
		
	}

	@Override
	public void endBlock(int count, SourceCoords coords) {
		// Builds one expression node object out of all the number of 
		// elements given in the count
		
		Stack<BlockItems> endBlockList = new Stack<BlockItems>();
		
		for (int i=0; i < count; i++) {
			endBlockList.push((BlockItems) stack.pop());
		}
		
		Block block = new Block(endBlockList,coords);
		stack.push(block);
	}
	
	public void popStack() {
		stack.pop();
	}

	@Override
	public ASTNode getAST() {
		return (ASTNode) this.stack.peek();
	}
}
