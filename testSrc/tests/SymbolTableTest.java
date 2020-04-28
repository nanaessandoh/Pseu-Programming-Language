package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pseu.common.SourceCoords;
import pseu.interpreter.ASTSourceI;
import pseudo.parser.ASTBuilder.ASTBuilder;
import pseudo.parser.ASTBuilder.ASTNode;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class SymbolTableTest {
	
	ASTSourceI<ASTNode> builder = new ASTBuilder();
	SourceCoords coords = new SourceCoords("test", 0, 0, "test", 0, 0);
	
	@Test
	public void testTrue() {
		
		builder.mkVariable("a", coords);
		builder.mkIdentifierType("Int", coords);
		builder.varDeclaration(false, coords);
		
		// If true
		builder.mkTrue(coords);
		// a := 1
		builder.mkVariable("x", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("2", coords);
		builder.valDeclaration(true, coords);
		
		builder.endBlock(1, coords);
		
		// val y : Int := 1
		builder.mkVariable("y", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("1", coords);
		builder.valDeclaration(true, coords);
		
		//
		builder.mkTrue(coords);
		
		builder.mkVariable("b", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("1", coords);
		builder.valDeclaration(true, coords);
		builder.endBlock(1, coords);
		
		builder.mkVariable("c", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("1", coords);
		builder.valDeclaration(true, coords);
		builder.endBlock(1, coords);
		
		builder.mkIf(coords);
		
		// val X : Int := 1
		builder.mkVariable("x", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("1", coords);
		builder.valDeclaration(true, coords);
		
		// val z : Int := 1
		builder.mkVariable("z", coords);
		builder.mkIdentifierType("Int", coords);
		builder.mkIntConstant("1", coords);
		builder.valDeclaration(true, coords);
		
		builder.endBlock(4, coords);
		
		builder.startBlock();
		builder.mkIf(coords);
		builder.endBlock(2, coords);
		
		SymbolTable st = new SymbolTable(builder.getAST());
		st.generateSymbolTable();
		
		assertEquals("Table Location : 0\n" + 
				"\tprint Fun[Any, Unit] val 0\n" + 
				"\treadInt Fun[Unit, Int] val 1\n" + 
				"\treadString Fun[Unit, String] val 2\n" + 
				"\n" + 
				"Table Location : 1\n" + 
				"\tx Int val 0\n" + 
				"\n" + 
				"Table Location : 2\n" + 
				"\tb Int val 0\n" + 
				"\n" + 
				"Table Location : 3\n" + 
				"\tc Int val 0\n" + 
				"\n" + 
				"Table Location : 4\n" + 
				"\ty Int val 0\n" + 
				"\tx Int val 1\n" + 
				"\tz Int val 2\n" + 
				"\n" + 
				"Table Location : 5\n" + 
				"\ta Int var 0" + "\n" + "\n"
				,  st.toString());
	}
}
