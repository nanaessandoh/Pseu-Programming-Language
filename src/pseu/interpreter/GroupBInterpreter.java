package pseudo.Interpretor;

import pseu.interpreter.Interpreter;
import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.InstructionFactory ;
import pseu.tsn.psam.PSAM;
import pseudo.parser.ASTBuilder.ASTBuilder;
import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.AnalizerAndCodegenerator.AnalyzerAndGenerator;

public class GroupBInterpreter extends Interpreter<ASTNode, Instruction>{
	
	
	public GroupBInterpreter() {
		
		super(new ASTBuilder(), 
			  new InstructionFactory(), 
			  new AnalyzerAndGenerator(), 
			  new PSAM());
	}
}
