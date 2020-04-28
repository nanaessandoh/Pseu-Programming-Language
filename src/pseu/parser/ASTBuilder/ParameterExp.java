package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ParameterExp extends Exp{
	
	VariableExp id;
	IdentifierType Type;
	
	public ParameterExp(VariableExp id,IdentifierType Type) {
		// Constructor
		this.id = id;
		this.Type = Type;
	}
	
	@Override
	public String toString() {
		// Convert paramenter into string format 
		return this.id.toString() + ":" + this.Type.toString();
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		// TODO Auto-generated method stub
		
	}
	
}
