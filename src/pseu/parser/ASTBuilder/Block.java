package pseudo.parser.ASTBuilder;

import java.util.ArrayList;
import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.Entries;
import pseudo.parser.BuildSymbolTable.LocalSTable;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class Block extends ASTNode{
	
	private Stack<BlockItems> blocElekList = new Stack<BlockItems>();
	private SourceCoords coords;
	private STableI locST;
	
	public Block() {
		// Constructor without paramenters
	}
	
	public Block(Stack<BlockItems> blocElekList,SourceCoords coords) {
		// Constructor with paramenters
		this.blocElekList = blocElekList;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// represent block in string
		int size = this.blocElekList.size() - 1;
		
		String blkStr = this.blocElekList.get(size).toString(); 
		
		for(int i = size - 1; i >= 0 ; i-- )
		{	
			blkStr += "; ";
			blkStr += this.blocElekList.get(i).toString();
		}
		
		return blkStr;
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError {
		
		tableLoc++;
		LocalSTable locST = new LocalSTable(table, tableLoc);
		int size = this.blocElekList.size();
		
		entryLoc = 0;
		
		for(int i = size - 1; i >= 0 ; i-- )
		{	
			this.blocElekList.get(i).buildSymbolTable(locST, stStack, entryLoc, tableLoc);
			
			if(this.blocElekList.get(i) instanceof DeclerationBlock){
				entryLoc++;
			}
			
		}
		this.locST = locST;
		stStack.push(locST);
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		ArrayList<Entries> entryArray = this.locST.getEntries();
		
		for(Entries ent : entryArray) {
			ent.getName().generateCode(factory, codeStore, st);
			ent.getType().generateCode(factory, codeStore, st);
		}
		
		Instr blockI = factory.newFrame(entryArray.size(), coords);
		codeStore.emit(blockI);
		
		int size = this.blocElekList.size();
		
		for( int i = size - 1; i >= 0 ; i-- )
		{	
			this.blocElekList.get(i).generateCode(factory, codeStore, st);
			
		}
		
		Instr endblock = factory.popFrame(this.coords);
		codeStore.emit(endblock);
		
		Instr returnInstr = factory.returnNow(this.coords);
		codeStore.emit(returnInstr);
	}

	

	
}
