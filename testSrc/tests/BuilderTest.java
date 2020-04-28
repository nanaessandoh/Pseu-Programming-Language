package test;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pseu.common.SourceCoords;
import pseudo.parser.ASTBuilder.ASTBuilder;
import pseudo.parser.ASTBuilder.Block;
import pseudo.parser.BuildSymbolTable.SymbolTable;

public class BuilderTest {
	
	private ASTBuilder astBuilder = new ASTBuilder();
	private SourceCoords coords = new SourceCoords("test", 0, 0, "test", 0, 0);
	
	@Test
	public void testFalse() {
		astBuilder.mkFalse(coords);
		assertEquals("false", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testTrue() {
		astBuilder.mkTrue(coords);
		assertEquals("true", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testIntConst() {
		astBuilder.mkIntConstant("123", coords);
		assertEquals("123", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testStringConst() {
		astBuilder.mkStringConstant("abc", coords);
		assertEquals("abc", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testVariable() {
		astBuilder.mkVariable("a", coords);
		assertEquals("a", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testFetch() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkFetch();
		assertEquals("a", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testSet() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkSet(3, coords);
		assertEquals("{a,b,c}", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testSequence() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkSequence(3, coords);
		assertEquals("[a,b,c]", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testTuple() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkTuple(3, coords);
		assertEquals("(a,b,c)", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testLookup() {
		astBuilder.mkVariable("x", coords);
		astBuilder.mkLookup("/= 1", coords);
		assertEquals("x./= 1", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testApplicationExp() {
		// X((2,3))
		astBuilder.mkVariable("X", coords);
		astBuilder.mkIntConstant("2", coords);
		astBuilder.mkIntConstant("3", coords);
		astBuilder.mkTuple(2, coords);
		astBuilder.mkApplication(coords);
		assertEquals("X(2,3)", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testFunctionExpr() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.startBlock();
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkVariable("E", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("F", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("G", coords);
		astBuilder.mkFetch();
		// a,b,c := E,F,G
		astBuilder.mkTuple(3, coords);
		astBuilder.mkAssignment(3, coords);
		astBuilder.endBlock(1, coords);
		astBuilder.mkFunctionExp(2, true, coords);
		// fun (a:T, b:U) : V do ... end fun
		assertEquals("fun (a:Int, b:Int) : Int do a,b,c := (E,F,G) end fun", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testAssignment() {
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkVariable("E", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("F", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("G", coords);
		astBuilder.mkFetch();
		// a,b,c := E,F,G
		astBuilder.mkTuple(3, coords);
		astBuilder.mkAssignment(3, coords);
		assertEquals("a,b,c := (E,F,G)", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testIf() {
		
		astBuilder.startBlock();
		
		// val X : Int := 1
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.valDeclaration(true, coords);
		
		// If true
		astBuilder.mkTrue(coords);
		// val X : Int := 1
		astBuilder.mkVariable("X", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.valDeclaration(true, coords);
		
		// val Y : Int := 2
		astBuilder.mkVariable("Y", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("2", coords);
		astBuilder.valDeclaration(true, coords);
		
		astBuilder.endBlock(2, coords);
		// else
		
		// val X : Int := 1
		astBuilder.mkVariable("Y", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.valDeclaration(true, coords);
		
		astBuilder.endBlock(1, coords);
		
		astBuilder.mkIf(coords);
		astBuilder.endBlock(2, coords);
		
		SymbolTable st = new SymbolTable((Block) astBuilder.getAST());
		st.generateSymbolTable();
		
		//"if X then B else C end if"
		assertEquals("val x : Int := 1; if true then val X : Int := 1; val Y : Int := 2 else val Y : Int := 1 end if", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testApplicationtype() {
		
		astBuilder.mkVariable("X", coords);
		astBuilder.mkVariable("Y", coords);
		astBuilder.mkVariable("Z", coords);
		astBuilder.mkTypeApplication("Foo", 3, coords);
		assertEquals("Foo[X, Y, Z]", astBuilder.getAST().toString());
	}
	
	@Test
	public void testWhile() {
		
		//x./=1
		astBuilder.mkVariable("x", coords);
		astBuilder.mkFetch();
		astBuilder.mkLookup("/=", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.mkTuple(1, coords);
		astBuilder.mkApplication(coords);
		
		astBuilder.mkVariable("a", coords);
		astBuilder.mkVariable("b", coords);
		astBuilder.mkVariable("c", coords);
		astBuilder.mkVariable("E", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("F", coords);
		astBuilder.mkFetch();
		astBuilder.mkVariable("G", coords);
		astBuilder.mkFetch();
		// a,b,c := E,F,G
		astBuilder.mkTuple(3, coords);
		astBuilder.mkAssignment(3, coords);
		astBuilder.endBlock(1, coords);
		
		// while x./= 1 do a,b,c := E,F,G end while
		astBuilder.mkWhile(coords);
		assertEquals("while x./=(1) do a,b,c := (E,F,G) end while", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testFor() {
		// for a <- X do B end for
		astBuilder.mkVariable( "a", coords );
		
		astBuilder.mkVariable( "X", coords );
		astBuilder.mkFetch();
		
		astBuilder.startBlock();
		astBuilder.mkVariable("a", coords);
		astBuilder.mkIntConstant("2", coords);
		astBuilder.mkFetch();
		astBuilder.mkAssignment(1, coords);
		astBuilder.endBlock(1, coords);
		
		astBuilder.mkFor(coords);
		assertEquals("for a <- X do a := 2 end for", astBuilder.getAST().toString());
		astBuilder.popStack();
	}
	
	@Test
	public void testReturn() {
		// return a
		astBuilder.mkVariable( "a", coords );
		astBuilder.mkFetch();
		astBuilder.mkReturn(coords);
		astBuilder.popStack();
	}
	
	@Test
	public void TestVal() {
		//val x : Int := 1
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.valDeclaration(true, coords);
		astBuilder.popStack();
	}
	
	@Test
	public void TestVar() {
		//var x : Int := 1
		astBuilder.mkVariable("x", coords);
		astBuilder.mkIdentifierType("Int", coords);
		astBuilder.mkIntConstant("1", coords);
		astBuilder.varDeclaration(true, coords);
		astBuilder.popStack();
	}
}
