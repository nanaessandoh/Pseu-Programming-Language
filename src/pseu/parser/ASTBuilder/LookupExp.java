package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class LookupExp extends Exp{

	private String name;
	private SourceCoords coords;
	private Exp operand;
	
	public LookupExp(Exp operand, String name,SourceCoords coords) {
		// Constructor
		this.name = name;
		this.coords = coords;
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		// Returns the look up in string format
		return this.operand.toString() + "." + this.name;
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		this.operand.generateCode(factory, codeStore, st);
		
		if (this.operand instanceof VariableExp ) {
			Instr fetch = factory.fetch(this.coords);
			codeStore.emit(fetch);
		}
		
		Instr name = factory.pushString(this.name, this.coords);
		codeStore.emit(name);
		
		Instr lookUpInstr = factory.lookup(this.coords);
		codeStore.emit(lookUpInstr);
	}

}
