package pseudo.parser.ASTBuilder;

import java.util.Stack;
import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ApplicationType extends TypeNode {
	
	private Stack<ASTNode> argStack;
	private String name;
	private SourceCoords coords;
	private int n;
	
	public ApplicationType(String name, Stack<ASTNode> argStack, SourceCoords coords) {
		// constructor
		this.coords = coords;
		this.name = name;
		this.argStack = argStack;
		
		this.n = argStack.size() - 1;
		
		if (name.equals("Set") || name.equals("Seq")) {
			this.n = 1;
		}else if (name.equals("Fun")) {
			this.n = 2;
		}
	}
	
	@Override
	public String toString() {
		// Represent type application in string such as Foo[T, U]
		int size = this.argStack.size() - 1;
		
		String appStr = this.name + "["; 
		
		appStr += this.argStack.get(size).toString(); 
		
		for(int i = size - 1; i >= 0 ; i-- )
		{
			appStr += ", ";
			appStr += this.argStack.get(i).toString();
		}
		
		appStr += "]";
		
		return appStr;
	}


	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		int size = argStack.size() - 1;
		
		for (int i = size; i >= 0; i--) {
			this.argStack.get(i).generateCode(factory, codeStore, st);
		}
		
		Instr appTypeInstr = factory.constructType(this.name, this.n, this.coords);
		codeStore.emit(appTypeInstr);
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
