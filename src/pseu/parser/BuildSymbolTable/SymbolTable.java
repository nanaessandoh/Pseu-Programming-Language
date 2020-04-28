package pseudo.parser.BuildSymbolTable;

import java.util.ArrayList;
import java.util.Stack;
import pseu.common.CompileTimeError;
import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.ASTBuilder.Block;

public class SymbolTable{
	private Stack<STableI> stStack = new Stack<STableI>();
	private ASTNode astNode;
	
	public SymbolTable(ASTNode astNode) {
		// Constructor which takes ASTBuilder as an object
		this.astNode = astNode;
	}
	
	public void generateSymbolTable() {
		
		// Initialize the global frame
		GlobalSTable gf = new GlobalSTable(); 
		
		// Add the value of the frame pointer to the stack
		this.stStack.push(gf);
		
		int entryLoc = 0;
		int tableLoc = 0;
		try {
			astNode.buildSymbolTable(gf, stStack, entryLoc, tableLoc);
		} catch (CompileTimeError e) {
			e.printStackTrace();
		}
	}
	
	public Stack<STableI> getSTStack(){
		return this.stStack;
	}
	
	public String toString() {
		
		String st = "";
		int tableNo = 0;
		for(STableI tables : this.stStack) {
			st += "Table Location : " + tableNo +"\n";
			ArrayList<Entries> entries = tables.getEntries();
			for(Entries entry : entries) {
				st += "\t" + entry.toString() +"\n" ;
			}
			st += "\n";
			tableNo += 1;
		}
		return st;
		
	}
}

