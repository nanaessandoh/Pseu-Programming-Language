package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class VariableExp extends Exp{
	
	private String name;
	private SourceCoords coords;
	
	public VariableExp(String name,SourceCoords coords) {
		this.name = name;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// return the variable in the form of string
		return this.name;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr variableInst = factory.pushString(this.name, coords);
		codeStore.emit(variableInst);
	
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
