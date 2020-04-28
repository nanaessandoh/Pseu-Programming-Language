package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class IfCommand extends CommandBlock{
	
	// if X then B else C end if
	private Block ifBlock, elseBlock;
	private Exp condition; 
	private SourceCoords coords;
	
	public IfCommand(Exp condition, Block ifBlock, Block elseBlock, SourceCoords coords) {
		// constructor
		this.condition = condition;
		this.ifBlock = ifBlock;
		this.elseBlock = elseBlock;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// to be represented as "if condition then B else C end if"
		
		return  "if " + this.condition.toString() + " then "+ this.ifBlock.toString() + " else " + this.elseBlock.toString() + " end if";
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		// If command has two blocks
		
		this.ifBlock.buildSymbolTable(table, stStack, entryLoc, tableLoc);
		// tableLoc += 1;
		this.elseBlock.buildSymbolTable(table, stStack, entryLoc, tableLoc);
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		this.ifBlock.generateCode(factory, codeStore, st);
		this.elseBlock.generateCode(factory, codeStore, st);
	}

}
