package tests;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import pseu.parser.PseuParser;

public class ParserTests {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEmptyFile() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "" ;
		String expected = 
				"startBlock()\n" + 
				"endBlock(0, unknown.pseu (line: 1 column: 0))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testAlmostEmptyFile() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "\n" ;
		String expected = 
				"startBlock()\n" + 
				"endBlock(0, unknown.pseu (line: 1 column: 1))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void testSendCommand() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "print.apply(\"hello\\nworld\\n\")" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(print, unknown.pseu (line: 1 columns: 1--5))\n" + 
				"mkFetch()\n" + 
				"mkLookup(apply, unknown.pseu (line: 1 columns: 1--11))\n" + 
				"mkStringConstant(\"hello\n" + 
				"world\n" + 
				"\", unknown.pseu (line: 1 columns: 13--28))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 1--29))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 1--29))\n" ;
		runParserTest(verbose, input, expected);
	}
	
	@Test
	public void testApplyCommand() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "print(x)(\"hello\\nworld\\n\")" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(print, unknown.pseu (line: 1 columns: 1--5))\n" + 
				"mkFetch()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 7))\n" + 
				"mkFetch()\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 1--8))\n" + 
				"mkStringConstant(\"hello\n" + 
				"world\n" + 
				"\", unknown.pseu (line: 1 columns: 10--25))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 1--26))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 1--26))\n" ;
		runParserTest(verbose, input, expected);
	}
	
	@Test
	public void testMultipleSendCommand() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "x.foo().bar()" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 1))\n" + 
				"mkFetch()\n" + 
				"mkLookup(foo, unknown.pseu (line: 1 columns: 1--5))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 columns: 6--7))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 1--7))\n" + 
				"mkLookup(bar, unknown.pseu (line: 1 columns: 1--11))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 columns: 12--13))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 1--13))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 1--13))\n" ;
		runParserTest(verbose, input, expected);
	}
	
	@Test
	public void testBinaryLookup() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "x.binary +" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 1))\n" + 
				"mkFetch()\n" + 
				"mkLookup(binary(+), unknown.pseu (line: 1 columns: 1--10))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 1--10))\n" ;
		runParserTest(verbose, input, expected);
	}
	
	@Test
	public void testUnaryLookup() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "x.unary -" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 1))\n" + 
				"mkFetch()\n" + 
				"mkLookup(unary(-), unknown.pseu (line: 1 columns: 1--9))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 1--9))\n" ;
		runParserTest(verbose, input, expected);
	}


	@Test
	public void testAssignment() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := 3\n";
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 6))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--6))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--6))\n" ;
		runParserTest(verbose, input, expected);		
	}
	@Test
	public void testAssignments() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := 3 ;\n" +
		               "b, c := 123_456, a\n";
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 6))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--6))\n" + 
				"mkVariable(b, unknown.pseu (line: 2 column: 1))\n" + 
				"mkVariable(c, unknown.pseu (line: 2 column: 4))\n" + 
				"mkIntConstant(123456, unknown.pseu (line: 2 columns: 9--15))\n" + 
				"mkVariable(a, unknown.pseu (line: 2 column: 18))\n" + 
				"mkFetch()\n" + 
				"mkTuple(2, unknown.pseu (line: 2 columns: 9--18))\n" + 
				"mkAssignment(2, unknown.pseu (line: 2 columns: 1--18))\n" + 
				"endBlock(2, unknown.pseu (line: 1 column: 1)--(line: 2 column: 18))\n" ;
		runParserTest(verbose, input, expected);		
	}
	@Test
	public void testMultipleAssignment0() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := (b,c,d)";
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 7))\n" + 
				"mkFetch()\n" + 
				"mkVariable(c, unknown.pseu (line: 1 column: 9))\n" + 
				"mkFetch()\n" + 
				"mkVariable(d, unknown.pseu (line: 1 column: 11))\n" + 
				"mkFetch()\n" + 
				"mkTuple(3, unknown.pseu (line: 1 columns: 6--12))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--12))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--12))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void testMultipleAssignment1() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := b,c,d";
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 6))\n" + 
				"mkFetch()\n" + 
				"mkVariable(c, unknown.pseu (line: 1 column: 8))\n" + 
				"mkFetch()\n" + 
				"mkVariable(d, unknown.pseu (line: 1 column: 10))\n" + 
				"mkFetch()\n" + 
				"mkTuple(3, unknown.pseu (line: 1 columns: 6--10))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--10))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--10))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	
	@Test
	public void testIf() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "if a then a := b else b := a end if" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 4))\n" + 
				"mkFetch()\n" + 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 11))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 16))\n" + 
				"mkFetch()\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 11--16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 11--16))\n" + 
				"startBlock()\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 23))\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 28))\n" + 
				"mkFetch()\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 23--28))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 23--28))\n" + 
				"mkIf(unknown.pseu (line: 1 columns: 1--35))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--35))\n" ;
		runParserTest(verbose, input, expected);
	}
	
	@Test
	public void testIfWithEmptyBlock() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "if a then else\n"
				     + "end if" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 4))\n" + 
				"mkFetch()\n" + 
				"startBlock()\n" + 
				"endBlock(0, unknown.pseu (line: 1 column: 11))\n" + 
				"startBlock()\n" + 
				"endBlock(0, unknown.pseu (line: 2 column: 1))\n" + 
				"mkIf(unknown.pseu (line: 1 column: 1)--(line: 2 column: 6))\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 1)--(line: 2 column: 6))\n" ;
		runParserTest(verbose, input, expected);
	}

	@Test
	public void testWhile() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "while x do a := b ; c := d end while" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 7))\n" + 
				"mkFetch()\n" + 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 12))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 17))\n" + 
				"mkFetch()\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 12--17))\n" + 
				"mkVariable(c, unknown.pseu (line: 1 column: 21))\n" + 
				"mkVariable(d, unknown.pseu (line: 1 column: 26))\n" + 
				"mkFetch()\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 21--26))\n" + 
				"endBlock(2, unknown.pseu (line: 1 columns: 12--26))\n" + 
				"mkWhile(unknown.pseu (line: 1 columns: 1--36))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--36))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFor() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "for a <- x do a := b end for" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 10))\n" + 
				"mkFetch()\n" + 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 15))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 20))\n" + 
				"mkFetch()\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 15--20))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 15--20))\n" + 
				"mkFor(unknown.pseu (line: 1 columns: 1--28))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--28))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testReturn() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "return a" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 8))\n" + 
				"mkFetch()\n" + 
				"mkReturn(unknown.pseu (line: 1 columns: 1--8))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--8))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testSendExp() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := (b).foo().bar()" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 7))\n" + 
				"mkFetch()\n" + 
				"mkLookup(foo, unknown.pseu (line: 1 columns: 6--12))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 columns: 13--14))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 6--14))\n" + 
				"mkLookup(bar, unknown.pseu (line: 1 columns: 6--18))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 columns: 19--20))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 6--20))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--20))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--20))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testLookupExp() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := b.foo.bar" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 6))\n" + 
				"mkFetch()\n" + 
				"mkLookup(foo, unknown.pseu (line: 1 columns: 6--10))\n" + 
				"mkLookup(bar, unknown.pseu (line: 1 columns: 6--14))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--14))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--14))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testApplyExp() {
		boolean verbose = false ;
		//              1234567890123456789012345678901234567890
		String input = "a := b(1)(2,3)" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 6))\n" + 
				"mkFetch()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 8))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 6--9))\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 11))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 13))\n" + 
				"mkTuple(2, unknown.pseu (line: 1 columns: 10--14))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 6--14))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--14))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--14))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFiniteSequenceExp() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := [1, 2, 3] ; b := [] ; c := [\"abc\"]" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 7))\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 10))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 13))\n" + 
				"mkSequence(3, unknown.pseu (line: 1 columns: 6--14))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--14))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 18))\n" + 
				"mkSequence(0, unknown.pseu (line: 1 columns: 23--24))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 18--24))\n" + 
				"mkVariable(c, unknown.pseu (line: 1 column: 28))\n" + 
				"mkStringConstant(\"abc\", unknown.pseu (line: 1 columns: 34--38))\n" + 
				"mkSequence(1, unknown.pseu (line: 1 columns: 33--39))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 28--39))\n" + 
				"endBlock(3, unknown.pseu (line: 1 columns: 1--39))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFiniteSetExp() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := {1, 2, 3} ; b := {} ; c := {\"abc\"}" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 7))\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 10))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 13))\n" + 
				"mkSet(3, unknown.pseu (line: 1 columns: 6--14))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--14))\n" + 
				"mkVariable(b, unknown.pseu (line: 1 column: 18))\n" + 
				"mkSet(0, unknown.pseu (line: 1 columns: 23--24))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 18--24))\n" + 
				"mkVariable(c, unknown.pseu (line: 1 column: 28))\n" + 
				"mkStringConstant(\"abc\", unknown.pseu (line: 1 columns: 34--38))\n" + 
				"mkSet(1, unknown.pseu (line: 1 columns: 33--39))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 28--39))\n" + 
				"endBlock(3, unknown.pseu (line: 1 columns: 1--39))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunExp0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := fun () : Int do return 1 ; end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 15--17))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 29))\n" + 
				"mkReturn(unknown.pseu (line: 1 columns: 22--29))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 22--31))\n" + 
				"mkFunctionExp(0, true, unknown.pseu (line: 1 columns: 6--39))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--39))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--39))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void testProcExp1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := proc (x : String) do 1 end proc" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 12))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 16--21))\n" + 
				"mkIdentifierType(Unit, unknown.pseu (line: 1 columns: 6--9))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 27))\n" + 
				"mkExpressionCommand()\n" + 
				"mkTuple(0, unknown.pseu (line: 1 column: 27))\n" +
				"mkExpressionCommand()\n" + 
				"endBlock(2, unknown.pseu (line: 1 column: 27))\n" + 
				"mkFunctionExp(1, true, unknown.pseu (line: 1 columns: 6--36))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--36))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--36))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunExp1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := fun (x : String) : Int do return 1 ; end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 11))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 15--20))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 25--27))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 39))\n" + 
				"mkReturn(unknown.pseu (line: 1 columns: 32--39))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 32--41))\n" + 
				"mkFunctionExp(1, true, unknown.pseu (line: 1 columns: 6--49))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--49))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--49))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunExp2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := fun (x : String, y : Set[Integer]) : Int do\n"
				     + "         return 1 ;\n"
				     + "     end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 11))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 15--20))\n" + 
				"mkVariable(y, unknown.pseu (line: 1 column: 23))\n" + 
				"mkIdentifierType(Integer, unknown.pseu (line: 1 columns: 31--37))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 27--38))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 43--45))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 2 column: 17))\n" + 
				"mkReturn(unknown.pseu (line: 2 columns: 10--17))\n" + 
				"endBlock(1, unknown.pseu (line: 2 columns: 10--19))\n" + 
				"mkFunctionExp(2, true, unknown.pseu (line: 1 column: 6)--(line: 3 column: 12))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 column: 1)--(line: 3 column: 12))\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 1)--(line: 3 column: 12))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunExp3() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := fun () do end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"startBlock()\n" +  
				"endBlock(0, unknown.pseu (line: 1 column: 16))\n" + 
				"mkFunctionExp(0, false, unknown.pseu (line: 1 columns: 6--22))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--22))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--22))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void testShortFunExp0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := () -> 1" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 12))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 12))\n" + 
				"mkFunctionExp(0, false, unknown.pseu (line: 1 columns: 6--12))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--12))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--12))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testShortFunExp1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := (x : String) -> - 2" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 7))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 11--16))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 24))\n" + 
				"mkLookup(unary(-), unknown.pseu (line: 1 column: 22))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 column: 22))\n" +
				"mkApplication(unknown.pseu (line: 1 column: 22))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 22--24))\n" + 
				"mkFunctionExp(1, false, unknown.pseu (line: 1 columns: 6--24))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--24))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--24))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testShortFunExp2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := (x : String, y : Set[Integer]) -> 1" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 7))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 11--16))\n" + 
				"mkVariable(y, unknown.pseu (line: 1 column: 19))\n" + 
				"mkIdentifierType(Integer, unknown.pseu (line: 1 columns: 27--33))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 23--34))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 40))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 40))\n" + 
				"mkFunctionExp(2, false, unknown.pseu (line: 1 columns: 6--40))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--40))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--40))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testShortFunExp4() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "a := () -> ()" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 1))\n" + 
				"startBlock()\n" +  
				"mkTuple(0, unknown.pseu (line: 1 columns: 12--13))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 12--13))\n" + 
				"mkFunctionExp(0, false, unknown.pseu (line: 1 columns: 6--13))\n" + 
				"mkAssignment(1, unknown.pseu (line: 1 columns: 1--13))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--13))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclUnsugared0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val a := fun () : Int do 1  end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 19--21))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 26))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 column: 26))\n" + 
				"mkFunctionExp(0, true, unknown.pseu (line: 1 columns: 10--35))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--35))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--35))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclUnsugared1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val a := fun (x : String) : Int do 1 ; end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 15))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 19--24))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 29--31))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 36))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 36--38))\n" + 
				"mkFunctionExp(1, true, unknown.pseu (line: 1 columns: 10--46))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--46))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--46))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclUnsugared2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val a := fun (x : String, y : Set[Integer]) : Int do\n"
				     + "             return 1 ;\n"
				     + "         end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 15))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 19--24))\n" + 
				"mkVariable(y, unknown.pseu (line: 1 column: 27))\n" + 
				"mkIdentifierType(Integer, unknown.pseu (line: 1 columns: 35--41))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 31--42))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 47--49))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 2 column: 21))\n" + 
				"mkReturn(unknown.pseu (line: 2 columns: 14--21))\n" + 
				"endBlock(1, unknown.pseu (line: 2 columns: 14--23))\n" + 
				"mkFunctionExp(2, true, unknown.pseu (line: 1 column: 10)--(line: 3 column: 16))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 column: 1)--(line: 3 column: 16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 1)--(line: 3 column: 16))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclUnsugared4() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val a := fun () do end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"startBlock()\n" +  
				"endBlock(0, unknown.pseu (line: 1 column: 20))\n" + 
				"mkFunctionExp(0, false, unknown.pseu (line: 1 columns: 10--26))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--26))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--26))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclSugared0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "fun a () : Int do 1  end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 12--14))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 19))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 column: 19))\n" + 
				"mkFunctionExp(0, true, unknown.pseu (line: 1 columns: 1--28))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--28))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--28))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclSugared1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "fun a (x : String) : Int do 1 ; end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 8))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 12--17))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 22--24))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 29))\n" + 
				"mkExpressionCommand()\n" +
				"endBlock(1, unknown.pseu (line: 1 columns: 29--31))\n" + 
				"mkFunctionExp(1, true, unknown.pseu (line: 1 columns: 1--39))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--39))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--39))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testProcDeclSugared1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "proc a (x : String) do 1 ; end proc" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 6))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 9))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 13--18))\n" + 
				"mkIdentifierType(Unit, unknown.pseu (line: 1 columns: 1--4))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 24))\n" + 
				"mkExpressionCommand()\n" +
				"mkTuple(0, unknown.pseu (line: 1 column: 26))\n" +
				"mkExpressionCommand()\n" +
				"endBlock(2, unknown.pseu (line: 1 columns: 24--26))\n" + 
				"mkFunctionExp(1, true, unknown.pseu (line: 1 columns: 1--35))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--35))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--35))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclSugared2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "fun a (x : String, y : Set[Integer]) : Int do\n"
				     + "             return 1 ;\n"
				     + "         end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 8))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 12--17))\n" + 
				"mkVariable(y, unknown.pseu (line: 1 column: 20))\n" + 
				"mkIdentifierType(Integer, unknown.pseu (line: 1 columns: 28--34))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 24--35))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 40--42))\n" + 
				"startBlock()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 2 column: 21))\n" + 
				"mkReturn(unknown.pseu (line: 2 columns: 14--21))\n" + 
				"endBlock(1, unknown.pseu (line: 2 columns: 14--23))\n" + 
				"mkFunctionExp(2, true, unknown.pseu (line: 1 column: 1)--(line: 3 column: 16))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 column: 1)--(line: 3 column: 16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 column: 1)--(line: 3 column: 16))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunDeclSugared3() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "fun a () do end fun" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(a, unknown.pseu (line: 1 column: 5))\n" + 
				"startBlock()\n" +  
				"endBlock(0, unknown.pseu (line: 1 column: 13))\n" + 
				"mkFunctionExp(0, false, unknown.pseu (line: 1 columns: 1--19))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--19))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--19))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testValDeclaration0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val x : Int := 1" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 9--11))\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 16))\n" + 
				"valDeclaration(true, unknown.pseu (line: 1 columns: 1--16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--16))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testValDeclaration1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "val x := 1" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 10))\n" + 
				"valDeclaration(false, unknown.pseu (line: 1 columns: 1--10))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--10))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testVarDeclaration0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Int := 1" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 9--11))\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 16))\n" + 
				"varDeclaration(true, unknown.pseu (line: 1 columns: 1--16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--16))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testVarDeclaration1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Int" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 9--11))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--11))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--11))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testTypeApplication1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Set[Int]" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 13--15))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 9--16))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--16))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--16))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testTypeApplication2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Map[Int, String]" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 13--15))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 18--23))\n" + 
				"mkTypeApplication(Map, 2, unknown.pseu (line: 1 columns: 9--24))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--24))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--24))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testTypeApplication11() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Set[Seq[String]]" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 17--22))\n" + 
				"mkTypeApplication(Seq, 1, unknown.pseu (line: 1 columns: 13--23))\n" + 
				"mkTypeApplication(Set, 1, unknown.pseu (line: 1 columns: 9--24))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--24))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--24))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunctionType1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Int * String -> Foo * Bar" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 9--11))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 15--20))\n" + 
				"mkTypeApplication(Product, 2, unknown.pseu (line: 1 columns: 9--20))\n" + 
				"mkIdentifierType(Foo, unknown.pseu (line: 1 columns: 25--27))\n" + 
				"mkIdentifierType(Bar, unknown.pseu (line: 1 columns: 31--33))\n" + 
				"mkTypeApplication(Product, 2, unknown.pseu (line: 1 columns: 25--33))\n" + 
				"mkTypeApplication(Fun, 2, unknown.pseu (line: 1 columns: 9--33))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--33))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--33))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunctionType2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : Int -> String -> Unit" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 9--11))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 16--21))\n" + 
				"mkIdentifierType(Unit, unknown.pseu (line: 1 columns: 26--29))\n" + 
				"mkTypeApplication(Fun, 2, unknown.pseu (line: 1 columns: 16--29))\n" + 
				"mkTypeApplication(Fun, 2, unknown.pseu (line: 1 columns: 9--29))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--29))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--29))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void testFunctionType3() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "var x : (Int -> String) -> Unit" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(x, unknown.pseu (line: 1 column: 5))\n" + 
				"mkIdentifierType(Int, unknown.pseu (line: 1 columns: 10--12))\n" + 
				"mkIdentifierType(String, unknown.pseu (line: 1 columns: 17--22))\n" + 
				"mkTypeApplication(Fun, 2, unknown.pseu (line: 1 columns: 10--22))\n" + 
				"mkIdentifierType(Unit, unknown.pseu (line: 1 columns: 28--31))\n" + 
				"mkTypeApplication(Fun, 2, unknown.pseu (line: 1 columns: 9--31))\n" + 
				"varDeclaration(false, unknown.pseu (line: 1 columns: 1--31))\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--31))\n" ;
		runParserTest(verbose, input, expected);		
	}

	@Test
	public void prefixOperator0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "- 42" ;
		String expected = 
				"startBlock()\n" + 
				"mkIntConstant(42, unknown.pseu (line: 1 columns: 3--4))\n" + 
				"mkLookup(unary(-), unknown.pseu (line: 1 column: 1))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 column: 1))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 1))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--4))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void prefixOperator1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "* - f(1)" ;
		String expected = 
				"startBlock()\n" + 
				"mkVariable(f, unknown.pseu (line: 1 column: 5))\n" + 
				"mkFetch()\n" + 
				"mkIntConstant(1, unknown.pseu (line: 1 column: 7))\n" + 
				"mkApplication(unknown.pseu (line: 1 columns: 5--8))\n" + 
				"mkLookup(unary(-), unknown.pseu (line: 1 column: 3))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 column: 3))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 3))\n" + 
				"mkLookup(unary(*), unknown.pseu (line: 1 column: 1))\n" + 
				"mkTuple(0, unknown.pseu (line: 1 column: 1))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 1))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--8))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void infixOperator0() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "2 + 3" ;
		String expected = 
				"startBlock()\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 1))\n" + 
				"mkLookup(binary(+), unknown.pseu (line: 1 column: 3))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 5))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 3))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--5))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void infixOperator1() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "2 * 3 + 4 * 5" ;
		String expected = 
				"startBlock()\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 1))\n" + 
				"mkLookup(binary(*), unknown.pseu (line: 1 column: 3))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 5))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 3))\n" + 
				"mkLookup(binary(+), unknown.pseu (line: 1 column: 7))\n" + 
				"mkIntConstant(4, unknown.pseu (line: 1 column: 9))\n" + 
				"mkLookup(binary(*), unknown.pseu (line: 1 column: 11))\n" + 
				"mkIntConstant(5, unknown.pseu (line: 1 column: 13))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 11))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 7))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--13))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	@Test
	public void infixOperator2() {
		boolean verbose = false ;
		//              12345678901234567890123456789012345678901234567
		String input = "2 * (3 + 4) * 5" ;
		String expected = 
				"startBlock()\n" + 
				"mkIntConstant(2, unknown.pseu (line: 1 column: 1))\n" + 
				"mkLookup(binary(*), unknown.pseu (line: 1 column: 3))\n" + 
				"mkIntConstant(3, unknown.pseu (line: 1 column: 6))\n" + 
				"mkLookup(binary(+), unknown.pseu (line: 1 column: 8))\n" + 
				"mkIntConstant(4, unknown.pseu (line: 1 column: 10))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 8))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 3))\n" + 
				"mkLookup(binary(*), unknown.pseu (line: 1 column: 13))\n" + 
				"mkIntConstant(5, unknown.pseu (line: 1 column: 15))\n" + 
				"mkApplication(unknown.pseu (line: 1 column: 13))\n" + 
				"mkExpressionCommand()\n" + 
				"endBlock(1, unknown.pseu (line: 1 columns: 1--15))\n" ;
		runParserTest(verbose, input, expected);		
	}
	
	

	private void runParserTest(boolean verbose, String input, String expected) {
		putLine( "Input <<<" + input + ">>>", verbose ) ;
		String output = runParser( input ) ;
		putLine( "Output <<<" + output + ">>>", verbose) ;
		assertEquals(expected, output);
	}

	private String runParser( String input ) {
		Reader reader = new StringReader( input ) ;
		PseuParser parser = new PseuParser( reader ) ;
		MockBuilder builder = new MockBuilder() ;
		parser.setBuilder( builder ) ;
		try { 
			parser.File();
		} catch( Throwable t) {
			System.out.println( t.getMessage() ) ;
			throw new AssertionError(t) ;
		}
		return builder.toString() ;
	}
	
	private void putLine( String str, boolean verbose ) {
		if( verbose ) System.out.println( str ) ;
	}
	
	private void put( String str, boolean verbose ) {
		if( verbose ) System.out.print( str ) ;
	}
}
