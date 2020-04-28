package pseudo.parser.BuildSymbolTable;

import java.util.ArrayList;

import pseudo.parser.ASTBuilder.VariableExp;

public interface STableI {
	
	public void addToList(Entries entry);
	public boolean checkIfEntryExists(Entries entry);
	public boolean checkInParent(Entries entry);
	public boolean checkIfValExists(VariableExp variable);
	public ArrayList<Entries> getEntries();
	public STableI getParent();
	public int getLocNumber();
}	

