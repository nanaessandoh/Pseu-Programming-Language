package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class FalseExp extends Exp{
	
	private Boolean boolExp;
	private SourceCoords coords;
	
	public FalseExp(SourceCoords coords) {
		this.boolExp = false;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// Return "false" when called, to represent the object is false class
		return "false";
	}

	
	boolean getValue() {
		// return boolean value
		return this.boolExp;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr falseInst = factory.pushBool(this.boolExp, this.coords);
		codeStore.emit(falseInst);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
