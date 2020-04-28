package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class IdentifierType extends TypeNode{
	
	private String name;
	private SourceCoords coords;
	private int n = 0;
	
	public IdentifierType(String name,SourceCoords coords) {
		// Constructor
		this.name = name;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// represent the type in string
		return this.name;
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr typeInstr = factory.constructType(this.name, this.n, this.coords);
		codeStore.emit(typeInstr);
		
	}

}
