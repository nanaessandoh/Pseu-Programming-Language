package pseudo.parser.AnalizerAndCodegenerator;

import java.io.PrintWriter;
import java.io.StringWriter;

import pseu.codeStore.CodeSource;
import pseu.codeStore.CodeTarget;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.RunTimeError;
import pseu.interpreter.AnalyzerAndGeneratorI;
import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.PSAM;
import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class AnalyzerAndGenerator implements AnalyzerAndGeneratorI<ASTNode> {

	@Override
	public <Instr extends InstructionI> 
	void analyzeAndGenerate(ASTNode block,
			InstructionFactoryI<Instr> instrFactory, CodeTarget<Instr> codeStore) throws CompileTimeError {
		
		// Build symbol table and check for compiler errors
		SymbolTable st = new SymbolTable(block);
		st.generateSymbolTable();
		
		codeStore.startNewSegment();
		
		block.generateCode(instrFactory, codeStore, st);
		
		codeStore.endSegment();
	}

}
