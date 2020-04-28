package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class Application extends Exp{
	
	private TupleExprestion tuple;
	private Exp funName;
	private SourceCoords coords;
	
	public Application(TupleExprestion tuple,Exp funName,SourceCoords coords) {
		// Constructor
		this.coords = coords;
		this.tuple = tuple;
		this.funName = funName;
	}

	@Override
	public String toString() {
		// return the tuple in string format : X(()), X((Y,Z))
		return this.funName.toString() + this.tuple.toString();
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
			
		this.funName.generateCode(factory, codeStore, st);
		this.tuple.generateCode(factory, codeStore, st);
		
		Instr apply = factory.apply(this.coords);
		codeStore.emit(apply);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		
		
		
	}

}
