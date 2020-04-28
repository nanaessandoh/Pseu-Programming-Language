package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class StringConstant extends Exp{
	
	private String str;
	private SourceCoords coords;
	
	public StringConstant(String str,SourceCoords coords) {
		this.coords=coords;
		this.str = str;
	}
	
	@Override
	public String toString() {
		// This method returns the object's string value in string format
		return str;
	}

	String getValue() {
		// get the value of the expression
		return this.str;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr str = factory.pushString(this.str, coords);
		codeStore.emit(str);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}
}
