package pseudo.parser.ASTBuilder;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class SequenceExp extends Exp {
	
	private Stack<Exp> seq = new Stack<Exp>();
	private SourceCoords coords;
	
	public SequenceExp(Stack<Exp> seq,SourceCoords coords) {
		this.seq = seq;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		// return the representation of stored sequence in string format
		String returnStr = "[";

		int size = this.seq.size();
		
		returnStr += this.seq.get(size -1).toString(); 
		
		for(int i = size - 2; i >= 0 ; i--)
		{
			returnStr += ",";
			returnStr += this.seq.get(i).toString();
		}
		
		returnStr += "]";
		
		return returnStr;
	}

	@Override
	public <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory,
			CodeTarget<Instr> codeStore, SymbolTable st) {
		
		int size = this.seq.size() - 1;
		
		for(int i = size; i >= 0 ; i-- )
		{
			this.seq.get(i).generateCode(factory, codeStore, st);
		}
		
		Instr seqInstr = factory.makeSeq(this.seq.size(), this.coords);
		codeStore.emit(seqInstr);
		
	}

	@Override
	public void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc)
			throws CompileTimeError {
		// TODO Auto-generated method stub
		
	}

}
