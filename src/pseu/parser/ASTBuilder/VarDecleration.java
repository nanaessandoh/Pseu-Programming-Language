package pseudo.parser.ASTBuilder;

import java.util.ArrayList;
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

public class VarDecleration extends DeclerationBlock {
	
	private TypeNode type;
	private Exp rightHandSide;
	private VariableExp variable;
	private SourceCoords coords;
	
	public VarDecleration(IdentifierType type,Exp rightHandSide, VariableExp variable,SourceCoords coords) {
		// Constructor
		this.rightHandSide = rightHandSide;
		this.type = type;
		this.variable = variable;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// String representation of a var declaration such as var a : T := X
		
		String varStr = "val " + this.variable.toString() + " ";
		varStr += ": " + type.toString();
		
		if(rightHandSide != null) {	
			varStr += " := " + this.rightHandSide.toString();
		}
	
		return varStr;
	}
	
	
	public ASTNode getType() {
		return this.type;
	}
	
	public ASTNode getVariable() {
		return this.variable;
	}
	
	public ASTNode getInitializedValue() {
		return this.rightHandSide;
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		
		Entries varEntry = new Entries(this.variable, this.type, "var",entryLoc);
		entryLoc++;
		boolean alreadyExists = table.checkIfEntryExists(varEntry);
		
		if (alreadyExists) {
			throw new CompileTimeError("Decleration " + this.toString() + " already exists", this.coords);
		}else {
			table.addToList(varEntry);
		}

	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		if(this.rightHandSide != null) {	
			// Stack<STableI> stStack = st.getSTStack();
			int tableLoc = 0;
			int entryLoc = 0;
			boolean isFound = false;
			ArrayList<Integer> nums = null;
 			
			for(STableI symbolTable : st.getSTStack()) {
				for(Entries arrayEnt : symbolTable.getEntries()) {
					if (this.variable.equals(arrayEnt.getName())) {
						isFound = true;
						entryLoc = arrayEnt.getEntryLoc();
						tableLoc = symbolTable.getLocNumber();
					}
				}
				nums.add(symbolTable.getLocNumber());
			}
				
			Collections.sort(nums);
			
			int max = nums.get(nums.size() - 1);
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
		
}


