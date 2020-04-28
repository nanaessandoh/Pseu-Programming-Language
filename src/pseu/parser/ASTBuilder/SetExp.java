package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class SetExp extends Exp {
	
	private Stack<Exp> setStack;
	private SourceCoords coords;
	
	public SetExp(Stack<Exp> setStack,SourceCoords coords) {
		
		this.setStack = setStack;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// return the representation of stored set in string format
		
		String returnStr = "{";
		
		int size = this.setStack.size();
		
		returnStr += this.setStack.get(size -1).toString(); 
		
		for(int i = size - 2; i >= 0 ; i-- )
		{
			returnStr += ",";
			returnStr += this.setStack.get(i).toString();
		}
		
		returnStr += "}";
		
		return returnStr;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		int size = this.setStack.size() - 1;
		
		for(int i = size; i >= 0 ; i-- )
		{
			this.setStack.get(i).generateCode(factory, codeStore, st);
		}
		
		Instr setInstr = factory.makeSet(this.setStack.size(), this.coords);
		codeStore.emit(setInstr);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
