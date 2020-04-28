package pseudo.parser.BuildSymbolTable;

import pseudo.parser.ASTBuilder.TypeNode;
import pseudo.parser.ASTBuilder.VariableExp;

public class Entries{
	
	private VariableExp name;
	private TypeNode type;
	private String functionality;
	private int entryLoc;
	
	public Entries(VariableExp name, TypeNode type, String functionality, int entryLoc) {
		// Constructor
		this.name = name;
		this.type = type;
		this.functionality = functionality;
		this.entryLoc = entryLoc;
	}
	
	public VariableExp getName() {
		return this.name;
	}
	
	public TypeNode getType() {
		return this.type;
	}
	
	public String getFunctionality() {
		return this.functionality;
	}
	
	public int getEntryLoc() {
		return this.entryLoc;
	}
	
	public String toString() {
		return this.name.toString() + " " + this.type.toString() + " " + this.functionality + " " + this.entryLoc;
	}
	
}