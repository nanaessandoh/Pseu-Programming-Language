package pseudo.parser.ASTBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class FunctionExpression extends Exp {
	
	private ParametersExp params;
	private Block funBlock;
	private IdentifierType resultType;
	private SourceCoords coords;
	
	public FunctionExpression(Block funBlock,IdentifierType resultType,ParametersExp params,SourceCoords coords) {
		// Constructor
		this.funBlock = funBlock;
		this.params = params;
		this.resultType = resultType;
	}
	
	@Override
	public String toString() {
		// Represent the function in string format, 
		// fun (a:T, b:U) : V do ... end fun
		// fun () do ... end fun
		
		String funStr = "fun " + this.params.toString() + " ";
		
		if(this.resultType != null) {
			funStr += ": "+this.resultType.toString()+" ";
		}
		
		funStr += "do "+ this.funBlock.toString()+ " end fun";
		return funStr;
	}
	
	
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		this.funBlock.buildSymbolTable(table, stStack, entryLoc,tableLoc);
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		
		
	}
	
}
