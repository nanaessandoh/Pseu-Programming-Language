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

public class AssignmentCommand extends CommandBlock{
	
	private int count; 
	private Exp rhs;
	private SourceCoords coords;
	private Stack<VariableExp> assignmentList; 
	
	public AssignmentCommand(Exp rhs, Stack<VariableExp> assignmentList, int count, SourceCoords coords) {
		// Constructor
		this.rhs = rhs;
		this.assignmentList = assignmentList;
		this.coords = coords;
		this.count = count;
	}
	
	@Override
	public String toString() {
		// return the assignment expression in the form of a string
		// a,b,c := E,F,G
		
		int size = this.assignmentList.size() - 1;
		
		String assiStr = this.assignmentList.get(size).toString(); 
		
		for(int i = size-1; i >= 0; i-- )
		{
			assiStr += ",";
			assiStr += this.assignmentList.get(i).toString();
			
		}
		
		assiStr += " := ";
		assiStr += this.rhs.toString();
		return assiStr;
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		boolean doesExists = true;
		int size = this.assignmentList.size() - 1;
		
		for(int i = size; i >= 0; i-- )
		{
			doesExists = table.checkIfValExists(this.assignmentList.get(i));
			if (doesExists == false) {
				throw new CompileTimeError("No decleration for "+ this.assignmentList.get(i).toString() + " variable ", this.coords);
			}
		}
		
		this.rhs.buildSymbolTable(table, stStack, entryLoc, tableLoc);
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		int tableLoc = 0;
		int entryLoc = 0;
		boolean isFound = false;
		ArrayList<Integer> nums = null;
		
		int size = this.assignmentList.size() - 1;
		
		for (int i = size ; i >= 0; i--) {
			
			for(STableI symbolTable : st.getSTStack()) {
				for(Entries arrayEnt : symbolTable.getEntries()) {
					if (this.assignmentList.get(i).equals(arrayEnt.getName())) {
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
				
				this.rhs.generateCode(factory, codeStore, st);
				
				Instr pop = factory.pop(1, this.coords);
				codeStore.emit(pop);
			}
		}
	}
	
}
