package pseudo.parser.ASTBuilder;

import java.util.Stack;
import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class TupleExprestion extends Exp {
	
//	private static final int InstructionFactoryI = 0;
	private Stack<Exp> tuple;
	private SourceCoords coords;
	
	public TupleExprestion(Stack<Exp> tuple, SourceCoords coords) {
		// Make a tuple type object with passed expression nodes
		this.tuple = tuple;
		this.coords = coords;
		
	}
	
	@Override
	public String toString() {
		// represent the stored tuple object in string format
		int size = this.tuple.size() - 1;
		String tupleStr = "(";
		
		tupleStr += this.tuple.get(size).toString();
		
		for(int i = size - 1; i >= 0; i-- )
		{
			tupleStr += ",";
			tupleStr += this.tuple.get(i).toString();
		}
		
		tupleStr += ")";
		
		return tupleStr;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		int size = this.tuple.size() - 1;
		 
		for(int i = size; i >= 0; i-- )
		{
			this.tuple.get(i).generateCode(factory, codeStore, st);	
		}
		
		Instr tupleInstr = factory.makeClosure(this.tuple.size(), this.coords);
		codeStore.emit(tupleInstr);
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
