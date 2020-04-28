package pseudo.parser.ASTBuilder;

import java.util.Stack;
import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseudo.parser.BuildSymbolTable.STableI;
import pseudo.parser.BuildSymbolTable.SymbolTable;


public abstract class ASTNode {
	
	public abstract String toString();
	public abstract void buildSymbolTable(STableI table, Stack<STableI> stStack, int entryLoc, int tableLoc) throws CompileTimeError;
	public abstract <Instr extends InstructionI> void generateCode(InstructionFactoryI<Instr> factory, CodeTarget<Instr> codeStore, SymbolTable st);
}
