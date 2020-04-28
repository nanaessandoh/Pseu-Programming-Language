package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class WhileCommand extends CommandBlock{
	
	private Application condition;
	private Block B;
	private SourceCoords coords;
	
	public WhileCommand(Application condition, Block B,SourceCoords coords) {
		// constructor
		this.condition = condition;
		this.B = B;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// To be represented like, "while X do B end while" 
		return "while "+this.condition.toString()+" do "+this.B.toString() + " end while";
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		B.buildSymbolTable(table, stStack, entryLoc, tableLoc);
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		this.B.generateCode(factory, codeStore, st);
		
	}

}
