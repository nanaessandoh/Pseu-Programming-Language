package pseudo.parser.ASTBuilder;

import static org.junit.Assert.assertTrue;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class IntConstant extends Exp{
	
	private String intStr;
	private SourceCoords coords;
	
	public IntConstant(String intStr,SourceCoords coords) {
		
		this.coords = coords;
		String[] IntSeq = intStr.split("");
		
		for(String digits : IntSeq) 
		{
			if (digits.matches("[0-9]+")) {
				
				this.intStr = intStr;
				
			}else {
				
				assertTrue("The provided i is not a integer",false);
				
			}
		}
		
	}
	
	@Override
	public String toString() {
		// This method returns the object's integer value in string format
		return this.intStr;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Instr intInst = factory.pushInt(this.intStr, coords);
		codeStore.emit(intInst);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		
		
	}

	
}
