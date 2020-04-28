package pseudo.parser.BuildSymbolTable;

import java.util.ArrayList;
import java.util.Stack;

import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.ASTBuilder.ApplicationType;
import pseudo.parser.ASTBuilder.IdentifierType;
import pseudo.parser.ASTBuilder.TypeNode;
import pseudo.parser.ASTBuilder.VariableExp;

public class GlobalSTable implements STableI{
	
	// List of locations
	private ArrayList<Entries> globalEntry = new ArrayList<Entries>();
	private int tableLoc = 0;
	public GlobalSTable() {
		IdentifierType any = new IdentifierType("Any", null);
		IdentifierType unit = new IdentifierType("Unit", null);
		IdentifierType int_I = new IdentifierType("Int", null);
		IdentifierType string = new IdentifierType("String", null);
		
		// print   Fun[Any, Unit]       val   0
		Stack<ASTNode> argStackPrint = new Stack<ASTNode>();
		argStackPrint.push(unit);
		argStackPrint.push(any);
		ApplicationType funPrint = new ApplicationType("Fun",argStackPrint,null);
		Entries print = new Entries(new VariableExp("print", null), funPrint, "val", 0);
		this.addToList(print);
		
		// readInt Fun[Unit, Int]       val   1
		Stack<ASTNode> argStackInt = new Stack<ASTNode>();
		argStackInt.push(int_I);
		argStackInt.push(unit);
		ApplicationType funInt = new ApplicationType("Fun",argStackInt,null);
		Entries readInt = new Entries(new VariableExp("readInt", null), funInt , "val",1);
		this.addToList(readInt);
		
		// readString Fun[Unit, String] val   2
		Stack<ASTNode> argStackStr = new Stack<ASTNode>();
		argStackStr.push(string);
		argStackStr.push(unit);
		ApplicationType funStr = new ApplicationType("Fun",argStackStr,null);
		Entries readString = new Entries(new VariableExp("readString", null), funStr , "val",2);
		this.addToList(readString);
		
	}
	
	@Override
	public void addToList(Entries entry) {
		// This function to put the key location pair into the frame
		this.globalEntry.add(entry);
	}

	@Override
	public boolean checkIfEntryExists(Entries entry) {
		boolean doesExist = false;
		
		for (Entries globalArrEnt : this.globalEntry) {
			if (entry.getName().equals(globalArrEnt.getName()) && entry.getType().equals(globalArrEnt.getType())) {
				doesExist = true;
			}
		}
		
		return doesExist;
	}

	@Override
	public boolean checkIfValExists(VariableExp variable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkInParent(Entries entry) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getTableLoc() {
		return this.tableLoc;
	}

	@Override
	public ArrayList<Entries> getEntries() {
		return this.globalEntry;
	}
	
	@Override
	public int getLocNumber() {
		return this.tableLoc;
	}

	@Override
	public STableI getParent() {
		
		return null;
	}
	
}
