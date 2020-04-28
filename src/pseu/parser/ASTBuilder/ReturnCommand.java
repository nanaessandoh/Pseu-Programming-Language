package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ReturnCommand extends CommandBlock{
	
	private Exp X;
	private SourceCoords coords;
	
	public ReturnCommand(Exp X,SourceCoords coords) {
		// constructor
		this.X = X;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// To represent in string like , "return X" 
		
		return "return "+this.X.toString();
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr returnInstr = factory.returnNow(this.coords);
		codeStore.emit(returnInstr);
	}

}
