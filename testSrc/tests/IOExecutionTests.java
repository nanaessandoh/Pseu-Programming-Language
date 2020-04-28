package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;

import pseu.codeStore.CodeStore;
import pseu.codeStore.InstructionFactoryI;
import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.interpreter.VirtualMachineI;
import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.InstructionFactory;
import pseu.tsn.psam.PSAM;

class IOExecutionTests {
	InstructionFactoryI<Instruction> f = new InstructionFactory() ;
	VirtualMachineI<Instruction> machine = new PSAM() ;

	@Test
	void printTest0() throws RunTimeError {
		String expectedResult = "Hello World\n" ;
		SourceCoords coords = new SourceCoords("foo.pseu", 1, 1, "foo.pseu", 1, 1) ;
		CodeStore<Instruction> codeStore = new CodeStore<Instruction>( ) ;
		codeStore.startNewSegment() ;// Segment 0
		codeStore.emit( f.pushLocation(0, 0, coords ) ); //print
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.pushString("Hello World\n", coords ) );
		codeStore.emit( f.apply( coords ) );
		printTest( codeStore, expectedResult ) ;
	}
	
	@Test
	void printTest1() throws RunTimeError {
		String expectedResult = "182" ;
		SourceCoords coords = new SourceCoords("foo.pseu", 1, 1, "foo.pseu", 1, 1) ;
		CodeStore<Instruction> codeStore = new CodeStore<Instruction>( ) ;
		codeStore.startNewSegment() ;// Segment 0
		codeStore.emit( f.pushLocation(0, 0, coords ) ); //print
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.pushInt("123", coords ) );
		codeStore.emit( f.pushString("binary(+)", coords ) );
		codeStore.emit( f.lookup( coords ) );
		codeStore.emit( f.pushInt("59", coords ) );
		codeStore.emit( f.apply( coords ) );
		codeStore.emit( f.apply( coords ) );
		printTest( codeStore, expectedResult ) ;
	}
	
	@Test
	void printTest2() throws RunTimeError {
		String expectedResult = "(123, \"fred\", true)" ;
		SourceCoords coords = new SourceCoords("foo.pseu", 1, 1, "foo.pseu", 1, 1) ;
		CodeStore<Instruction> codeStore = new CodeStore<Instruction>( ) ;
		codeStore.startNewSegment() ;// Segment 0
		codeStore.emit( f.pushLocation(0, 0, coords ) ); //print
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.pushInt("123", coords ) );
		codeStore.emit( f.pushString("fred", coords ) );
		codeStore.emit( f.pushBool(true, coords ) );
		codeStore.emit( f.makeTuple(3, coords ) );
		codeStore.emit( f.apply( coords ) ) ;
		printTest( codeStore, expectedResult ) ;
	}
	
	void printTest(CodeStore<Instruction> codeStore, String expectedResult)
	throws RunTimeError {
		StringWriter w = new StringWriter() ;
		PrintWriter pw = new PrintWriter( w ) ;
		machine.reset();
		machine.setOutputTarget( pw );
		machine.loadCode( codeStore );
		machine.run();
		String actualResult = w.toString() ;
		assertEquals( expectedResult, actualResult ) ;
	}

	
	@Test
	void readLineTest0() throws RunTimeError {
		SourceCoords coords = new SourceCoords("foo.pseu", 1, 1, "foo.pseu", 1, 1) ;
		CodeStore<Instruction> codeStore = new CodeStore<Instruction>( ) ;
		codeStore.startNewSegment() ;// Segment 0
		codeStore.emit( f.pushLocation(0, 0, coords ) ); //print
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.pushLocation(0, 1, coords ) ) ; // readLine
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.makeTuple(0, coords ) );
		codeStore.emit( f.apply( coords ) ) ;
		codeStore.emit( f.apply( coords ) ) ;
		readAndPrintTest( codeStore, "fred and bob\n", "fred and bob" ) ;
	}

	@Test
	void readIntTest0() throws RunTimeError {
		SourceCoords coords = new SourceCoords("foo.pseu", 1, 1, "foo.pseu", 1, 1) ;
		CodeStore<Instruction> codeStore = new CodeStore<Instruction>( ) ;
		// print (readInt() + 1)
		codeStore.startNewSegment() ;// Segment 0
		codeStore.emit( f.pushLocation(0, 0, coords ) ); //print
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.pushLocation(0, 2, coords ) ) ; // readInt
		codeStore.emit( f.fetch( coords ) );
		codeStore.emit( f.makeTuple(0, coords ) );
		codeStore.emit( f.apply( coords ) ) ;
		codeStore.emit( f.pushString( "binary(+)", coords ) ) ;
		codeStore.emit( f.lookup( coords ) ) ;
		codeStore.emit( f.pushInt( "1", coords ) ) ;
		codeStore.emit( f.apply( coords ) ) ;
		codeStore.emit( f.apply( coords ) ) ;
		readAndPrintTest( codeStore, "1234\n", "1235" ) ;
	}
	
	void readAndPrintTest(CodeStore<Instruction> codeStore, String input, String expectedResult)
	throws RunTimeError {
		StringWriter w = new StringWriter() ;
		PrintWriter pw = new PrintWriter( w ) ;
		StringReader sr = new StringReader( input ) ;
		machine.reset();
		machine.setInputSource( sr );
		machine.setOutputTarget( pw );
		machine.loadCode( codeStore );
		machine.run();
		String actualResult = w.toString() ;
		assertEquals( expectedResult, actualResult ) ;
	}
}
