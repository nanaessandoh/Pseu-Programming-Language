package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ForCommand extends CommandBlock{
	
	private Exp X;
	private Block B;
	private VariableExp variable;
	private SourceCoords coords;
	
	public ForCommand(Block B,Exp X,VariableExp variable,SourceCoords coords) {
		// Constructor
		this.X = X;
		this.B = B;
		this.variable = variable;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// To be represented like this "for a <- X do B end for" 
		return "for "+this.variable.toString()+ " <- " + this.X.toString()+ " do "+ this.B.toString() + " end for";
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		this.B.buildSymbolTable(table, stStack, entryLoc, tableLoc);
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		this.B.generateCode(factory, codeStore, st);
		
	}

}
