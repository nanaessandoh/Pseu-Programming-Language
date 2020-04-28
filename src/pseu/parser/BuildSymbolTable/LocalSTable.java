package pseudo.parser.BuildSymbolTable;

import java.awt.List;
import java.util.ArrayList;

import pseudo.parser.ASTBuilder.TypeNode;
import pseudo.parser.ASTBuilder.VariableExp;

public class LocalSTable implements STableI{
	
	// Reference to parent table
	private STableI parentPtr;
	private int tableLoc;
	// List of entries
	private ArrayList<Entries> entryArray = new ArrayList<Entries>();
	
	public LocalSTable(STableI parentPtr, int tableLoc) {
		this.parentPtr = parentPtr;
		this.tableLoc = tableLoc;
	}
	
	@Override
	public void addToList(Entries entry) {
		// This function to put entry into the array list of entries
		this.entryArray.add(entry);
	}
	
	@Override
	public STableI getParent() {
		return this.parentPtr;
	}

	@Override
	public boolean checkIfEntryExists(Entries entry) {
		
		boolean doesExist = false;
		
		for (Entries arrayEnt : this.entryArray) {
			if (entry.getName().equals(arrayEnt.getName()) && entry.getFunctionality().equals(arrayEnt.getFunctionality())) {
				doesExist = true;
			}
		}
		
		if (!doesExist) {
			doesExist = this.parentPtr.checkInParent(entry);
		}
		
		return doesExist;
	}
	
	@Override
	public boolean checkInParent(Entries entry) {
		
		boolean doesExist = false;
		
		for (Entries arrayEnt : this.entryArray) {
			if (entry.getName().equals(arrayEnt.getName()) && entry.getType().equals(arrayEnt.getType())) {
				doesExist = true;
			}
		}
		
		if (!doesExist) {
			doesExist = this.parentPtr.checkInParent(entry);
		}
		
		return doesExist;
	}

	@Override
	public boolean checkIfValExists(VariableExp variable) {
		
		boolean doesExist = false;
		
		for (Entries arrayEnt : this.entryArray) {
			if (variable.equals(arrayEnt.getName()) & arrayEnt.getFunctionality().equals("val") & !arrayEnt.getFunctionality().equals("Any")) {
				doesExist = true;
			}
		}
		
		return doesExist;
	}

	@Override
	public ArrayList<Entries> getEntries() {
		
		return this.entryArray;
	}

	@Override
	public int getLocNumber() {
		
		return this.tableLoc;
	}
}
