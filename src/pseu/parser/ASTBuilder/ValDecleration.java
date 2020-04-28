package pseudo.parser.ASTBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.Entries;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class ValDecleration extends DeclerationBlock {
	
//	private ASTNode rightHandSide,type,variable;
	private TypeNode type;
	private Exp rightHandSide;
	private VariableExp variable;
	private SourceCoords coords;
	
	public ValDecleration(TypeNode type,Exp rightHandSide, VariableExp variable,SourceCoords coords) {
		// Constructor
		this.rightHandSide = rightHandSide;
		this.type = type;
		this.variable = variable;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// String representation of a val declaration such as val a : T := X
		
		String valStr = "val " + this.variable.toString() + " ";
		
		if(!this.type.toString().equals("Any")) {
			
			valStr += ": " + type.toString();
		}
		valStr += " := " + this.rightHandSide.toString();
		
		return valStr;
	}
	

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		Entries valEntry = new Entries(this.variable, this.type, "val", entryLoc);
		boolean alreadyExists = table.checkIfEntryExists(valEntry);
		
		if (alreadyExists) {
			throw new CompileTimeError("Decleration " + this.toString() + " already exists", this.coords);
		}else {
			table.addToList(valEntry);
		}
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		Stack<STableI> stStack = st.getSTStack();
		int size = stStack.size() - 1;
		int tableLoc = 0;
		int  entryLoc = 0;
		boolean isFound = false;
		ArrayList<Integer> arrayInt = new ArrayList<Integer>();
		
		for(STableI symbolTable : st.getSTStack()) {
			for(Entries arrayEnt : symbolTable.getEntries()) {
				if (this.variable.equals(arrayEnt.getName())) {
					isFound = true;
					entryLoc = arrayEnt.getEntryLoc();
					tableLoc = symbolTable.getLocNumber();
				}
			}
			arrayInt.add(symbolTable.getLocNumber());
			
		}
			
		Collections.sort(arrayInt);
		
		int max = arrayInt.get(arrayInt.size() - 1);
		tableLoc = max - tableLoc;
			
		if (isFound) {
			Instr assignVal = factory.pushLocation(tableLoc, entryLoc, this.coords);
			codeStore.emit(assignVal);
			
			Instr val = factory.pushInt(this.rightHandSide.toString(), this.coords);
			codeStore.emit(val);
			
			Instr assignValAgain = factory.pushLocation(tableLoc, entryLoc, this.coords);
			codeStore.emit(assignValAgain);
			
			Instr unLock = factory.unlockLocation(this.coords);
			codeStore.emit(unLock);
			
			Instr store = factory.store(1, this.coords);
			codeStore.emit(store);
		}
		
	}
}
