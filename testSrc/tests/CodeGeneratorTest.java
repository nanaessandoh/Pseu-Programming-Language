package test;
import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Test;
import pseu.codeStore.CodeStore;
import pseu.common.CompileTimeError;
import pseu.common.SourceCoords;
import pseu.interpreter.ASTSourceI;
import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.InstructionFactory;
import pseudo.parser.ASTBuilder.ASTBuilder;
import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.AnalizerAndCodegenerator.AnalyzerAndGenerator;

public class CodeGeneratorTest {
	
	ASTSourceI<ASTNode> astBuilder = new ASTBuilder();
	SourceCoords coords = new SourceCoords("test", 0, 0, "test", 0, 0);
	AnalyzerAndGenerator analyze = new AnalyzerAndGenerator();
	InstructionFactory instrFactory = new InstructionFactory();
	StringWriter sw = new StringWriter();
	PrintWriter writer = new PrintWriter(sw);
	CodeStore<Instruction> codeStore = new CodeStore<Instruction>();
	
	@Test
	public void testLookUp() {
		
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.varDeclaration(false, coords);
		
		astBuilder.mkIntConstant("1", coords);
		astBuilder.mkLookup("binary(+)", coords);
		
		astBuilder.mkIntConstant("1", coords);
		astBuilder.mkTuple(1, coords);
		
		astBuilder.mkApplication(coords);
		astBuilder.mkExpressionCommand();
		astBuilder.endBlock(2, coords);
		
		codeStore.startNewSegment();
		
		try {
			analyze.analyzeAndGenerate(astBuilder.getAST(), instrFactory, codeStore);
		} catch (CompileTimeError e) {
			e.printStackTrace();
		}
		codeStore.endSegment();
		
		codeStore.dumpTheCode(writer);
		
		assertEquals("The code store\r\n" + 
				"    Start of Segment 0\r\n" + 
				"          0 : pushString(x)\r\n" + 
				"          1 : constructType(Int, 0)\r\n" + 
				"          2 : newFrame(1)\r\n" + 
				"          3 : pushInt(1)\r\n" + 
				"          4 : pushString(binary(+))\r\n" + 
				"          5 : lookup()\r\n" + 
				"          6 : pushInt(1)\r\n" + 
				"          7 : makeClosure(1)\r\n" + 
				"          8 : apply()\r\n" + 
				"          9 : popFrame()\r\n" + 
				"         10 : returnNow()\r\n" + 
				"    End of Segment 0" + "\r\n"
				,  codeStore.toString());
	}
	
	@Test
	public void testValDecleration() {
		
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("2", coords);
		astBuilder.valDeclaration(true, coords);
		
		astBuilder.endBlock(1, coords);
		
		codeStore.startNewSegment();
		
		try {
			analyze.analyzeAndGenerate(astBuilder.getAST(), instrFactory, codeStore);
		} catch (CompileTimeError e) {
			e.printStackTrace();
		}
		
		codeStore.endSegment();
		codeStore.dumpTheCode(writer);
		
		assertEquals("The code store\r\n" + 
				"    Start of Segment 0\r\n" + 
				"          0 : pushString(x)\r\n" + 
				"          1 : constructType(Int, 0)\r\n" + 
				"          2 : newFrame(1)\r\n" + 
				"          3 : pushLocation(0, 0)\r\n" + 
				"          4 : pushInt(2)\r\n" + 
				"          5 : pushLocation(0, 0)\r\n" + 
				"          6 : unlockLocation()\r\n" + 
				"          7 : store(1)\r\n" + 
				"          8 : popFrame()\r\n" + 
				"          9 : returnNow()\r\n" + 
				"    End of Segment 0\r\n"
				,  codeStore.toString());
	}
	
	@Test
	public void testVarDeclerition() {
		
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.varDeclaration(false, coords);
		
		astBuilder.endBlock(1, coords);
		
		codeStore.startNewSegment();
		
		try {
			analyze.analyzeAndGenerate(astBuilder.getAST(), instrFactory, codeStore);
		} catch (CompileTimeError e) {
			e.printStackTrace();
		}
		
		codeStore.endSegment();
		codeStore.dumpTheCode(writer);
		
		assertEquals("The code store\r\n" + 
				"    Start of Segment 0\r\n" + 
				"          0 : pushString(x)\r\n" + 
				"          1 : constructType(Int, 0)\r\n" + 
				"          2 : newFrame(1)\r\n" + 
				"          3 : popFrame()\r\n" + 
				"          4 : returnNow()\r\n" + 
				"    End of Segment 0" + "\r\n"
				,  codeStore.toString());
	}
	
}
