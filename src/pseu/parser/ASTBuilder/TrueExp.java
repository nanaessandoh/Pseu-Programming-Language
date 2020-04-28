package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class TrueExp extends Exp{
	
	private Boolean boolExp;
	private SourceCoords coords;
	
	public TrueExp(SourceCoords coords) {
		this.boolExp = true;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// Return "true" when called, to represent the object is true class
		return "true";
	}

	
	public boolean getValue() {
		// get the value this expression
		return this.boolExp;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr trueInst = factory.pushBool(this.boolExp, this.coords);
		codeStore.emit(trueInst);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
