package pseudo.parser.ASTBuilder;

import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ParametersExp extends Exp{
	
	private Stack<ParameterExp> params;
	
	public ParametersExp(Stack<ParameterExp> params) {
		// Constructor
		this.params = params;
	}
	
	@Override
	public String toString() {
		// represent the parameters in string format
		int size = this.params.size();
		
		String paramsStr = "("; 
		paramsStr += this.params.get(size -1).toString(); 
		
		for(int i = size - 2; i >= 0 ; i-- )
		{
			paramsStr += ", ";
			paramsStr += this.params.get(i).toString();
		}
		
		paramsStr += ")";
		
		return paramsStr;
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		// TODO Auto-generated method stub
		
	}


}
