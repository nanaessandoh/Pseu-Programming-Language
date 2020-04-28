package tests;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable; 


import pseu.codeStore.CodeStore;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.SourceCoords;
import pseu.interpreter.VirtualMachineI;

public abstract class SimpleExecutionTests<Instr extends InstructionI>{
	
	InstructionFactoryI<Instr> f ;
	VirtualMachineI<Instr> machine ;
	
	class TestData {
		String name ;
		CodeStore<Instr> codeStore ;
		String expectedResult ;
		
		TestData( String name, InstructionI[][] instructions, String expectedResult) {
			this.name = name ;
			this.expectedResult = expectedResult ;
			codeStore = new CodeStore<Instr>() ;
			for( int s = 0 ; s < instructions.length ; ++s ) {
				InstructionI[] segmentInstructions = (InstructionI[]) instructions[s] ;
				codeStore.startNewSegment() ;
				for( int i = 0 ; i < segmentInstructions.length ; ++i ) {
					// This is safe because the instruction comes from this.f
					Instr instr = (Instr) segmentInstructions[i] ;
					codeStore.emit( instr  ); }
			}
			for( int s = 0 ; s < instructions.length ; ++s ) {
				codeStore.endSegment();
			}	
		}
	}
	
	private Stream<TestData> testDataStream() {
		SourceCoords sc = new SourceCoords("a.pseu", 1, 2, "a.pseu", 3, 4) ;
		return Stream.of(
			new TestData(
					"intConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "123",sc)
						}
					},
					"123\n"
			),
			new TestData(
					"intConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "123",sc),
								f.pushInt( "456",sc)
						}
					},
					"456\n123\n"
			),
			new TestData(
					"stringConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "abc",sc)
						}
					},
					"\"abc\"\n"
			),
			new TestData(
					"stringConstant_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "abc",sc),
								f.pushString( "\n\t\r",sc)
						}
					},
					"\"\\n\\t\\r\"\n\"abc\"\n"
			),
			new TestData(
					"boolConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( true,sc),
								f.pushBool( false,sc)
						}
					},
					"false\ntrue\n"
			),
			new TestData(
					"tupleConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeTuple(0, sc)
						}
					},
					"()\n"
			),
			new TestData(
					"tupleConstant_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeTuple( 0, sc ),
								f.makeTuple( 0, sc ),
								f.makeTuple( 2, sc )
						}
					},
					"((), ())\n"
			),
			new TestData(
					"tupleConstant_2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( true,sc),
								f.pushInt( "1234", sc),
								f.pushString( "abc", sc),
								f.makeTuple(3, sc)
						}
					},
					"(true, 1234, \"abc\")\n"
			),
			new TestData(
					"setConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeSet(0, sc)
						}
					},
					"{}\n"
			),
			new TestData(
					"setConstant_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeTuple( 0, sc ),
								f.makeSet( 1, sc )
						}
					},
					"{()}\n"
			),
			new TestData(
					"setConstant_2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( true,sc),
								f.pushInt( "1234", sc),
								f.pushString( "abc", sc),
								f.makeSet(3, sc)
						}
					},
					// This test assumes a particular order
					"{true, 1234, \"abc\"}\n"
			),
			new TestData(
					"seqConstant_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeSeq(0, sc)
						}
					},
					"[]\n"
			),
			new TestData(
					"seqConstant_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeTuple( 0, sc ),
								f.makeSeq( 1, sc )
						}
					},
					"[()]\n"
			),
			new TestData(
					"seqConstant_2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( true,sc),
								f.pushInt( "1234", sc),
								f.pushString( "abc", sc),
								f.makeSeq(3, sc)
						}
					},
					"[true, 1234, \"abc\"]\n"
			),
			new TestData(
					"constructTypes",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType( "Int", 0, sc),
								f.constructType( "String", 0, sc),
								f.constructType( "Bool", 0, sc),
								f.constructType( "Unit", 0, sc),
								f.constructType( "Any", 0, sc),
								f.constructType( "None", 0, sc),

								f.constructType( "Int", 0, sc),
								f.constructType( "Bool", 0, sc),
								f.constructType( "Product", 2, sc),

								f.constructType( "String", 0, sc),
								f.constructType( "Bool", 0, sc),
								f.constructType( "Unit", 0, sc),
								f.constructType( "Product", 3, sc),

								f.constructType( "String", 0, sc),
								f.constructType( "Set", 1, sc),

								f.constructType( "Bool", 0, sc),
								f.constructType( "Iterator", 1, sc),

								f.constructType( "String", 0, sc),
								f.constructType( "Set", 1, sc),
								f.constructType( "Seq", 1, sc),
								

								f.constructType( "Bool", 0, sc),
								f.constructType( "Unit", 0, sc),
								f.constructType( "Fun", 2, sc)
						}
					},
					"Fun[Bool, Unit]\n" +
					"Seq[Set[String]]\n" +
					"Iterator[Bool]\n" +
					"Set[String]\n" +
					"Product[String, Bool, Unit]\n" +
					"Product[Int, Bool]\n" +
					"None\n" +
					"Any\n" +
					"Unit\n" +
					"Bool\n" +
					"String\n" +
					"Int\n"
			),
			new TestData(
					"locations_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.newFrame(0, sc),
								f.popFrame( sc ) 
						}
					},
					""
			),
			new TestData(
					"locations_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x", sc ),
								f.constructType("Int", 0, sc),
								f.pushString( "y", sc ),
								f.constructType("String", 0, sc),
								f.newFrame(2, sc),
								f.pushLocation( 0, 0, sc ),
								f.pushLocation( 0, 1, sc ),
								f.popFrame( sc ) 
						}
					},
					"Loc[name=y, writable=false, readable=false, value=none]\n" +
					"Loc[name=x, writable=false, readable=false, value=none]\n"
			),
			new TestData(
					"locations_2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x", sc ),
								f.constructType("Int", 0, sc),
								f.pushString( "y", sc ),
								f.constructType("Bool", 0, sc),
								f.newFrame(2, sc),
								// Unlock both locations
								f.pushLocation( 0, 0, sc ),
								f.unlockLocation( sc ),
								f.pushLocation( 0, 1, sc ),
								f.unlockLocation( sc ),
								// Push both locations
								f.pushLocation( 0, 0, sc ),
								f.pushLocation( 0, 1, sc ),
								// Push a 2 tuple
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.makeTuple(2, sc),
								f.store( 2, sc ),
								f.pop( 1, sc ),
								// Fetch x and then y
								f.pushLocation( 0, 0, sc ),
								f.fetch( sc ),
								f.pushLocation( 0, 1, sc ),
								f.fetch( sc ),
								f.popFrame( sc ) 
						}
					},
					"true\n" +
					"666\n"
			),
			new TestData(
					"locations_3",
					new InstructionI[][] {
						new InstructionI[] {
								// Make a frame with x and y
								f.pushString( "x", sc ),
								f.constructType("Int", 0, sc),
								f.pushString( "y", sc ),
								f.constructType("Bool", 0, sc),
								f.newFrame(2, sc),
								// Make a nested  frame with z.
								f.pushString( "z", sc ),
								f.constructType("String", 0, sc),
								f.newFrame(1, sc),
								// Make an empty frame
								f.newFrame(0, sc),	
								// Unlock all tree locations
								f.pushLocation( 2, 0, sc ),
								f.unlockLocation( sc ),
								f.pushLocation( 2, 1, sc ),
								f.unlockLocation( sc ),
								f.pushLocation( 1, 0, sc ),
								f.unlockLocation( sc ),
								// Push locations x, y, and z
								f.pushLocation( 2, 0, sc ),
								f.pushLocation( 2, 1, sc ),
								f.pushLocation( 1, 0, sc ),
								// Push a 3 tuple and store.
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.pushString( "hello", sc ),
								f.makeTuple(3, sc),
								f.store( 3, sc ),
								f.pop( 1, sc ),
								// Fetch x, y and z
								f.pushLocation( 2, 0, sc ),
								f.fetch( sc ),
								f.pushLocation( 2, 1, sc ),
								f.fetch( sc ),
								f.pushLocation( 1, 0, sc ),
								f.fetch( sc ),
								// pop 2 frames
								f.popFrame( sc ),
								f.popFrame( sc ),
								// Fetch x and y again 
								f.pushLocation( 0, 0, sc ),
								f.fetch( sc ),
								f.pushLocation( 0, 1, sc ),
								f.fetch( sc ),
								// The stack should be (bot to top)
								// x, y, z, x, y
								f.popFrame( sc )
						}
					},
					"true\n" +
					"666\n" +
					"\"hello\"\n" +
					"true\n" +
					"666\n"
			),
			new TestData(
					"stack manipluation_0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.pushString( "hello", sc ),
								f.pop(2, sc)
						}
					},
					"666\n" 
			),
			new TestData(
					"stack manipluation_1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.pushString( "hello", sc ),
								f.duplicate(sc)
						}
					},
					"\"hello\"\n" +
					"\"hello\"\n" +
					"true\n" +
					"666\n" 
			),
			new TestData(
					"stack manipluation_2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.pushString( "hello", sc ),
								f.rotateUp(3, sc)
						}
					},
					"true\n" +
					"666\n" +
					"\"hello\"\n"
			),
			new TestData(
					"stack manipluation_3",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "666", sc),
								f.pushBool( true, sc ),
								f.pushString( "hello", sc ),
								f.rotateDown(3, sc)
						}
					},
					"666\n" +
					"\"hello\"\n" +
					"true\n"
			),
			new TestData(
					"apply sequence",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "0", sc),
								f.pushInt( "10", sc),
								f.pushInt( "20", sc),
								f.pushInt( "30", sc),
								f.pushInt( "40", sc),
								f.makeSeq( 5, sc ),
								f.duplicate( sc ),
								f.duplicate( sc ),
								f.pushInt( "0", sc),
								f.apply( sc ),
								f.rotateDown( 3, sc ),
								f.pushInt( "1", sc),
								f.apply( sc ),
								f.rotateDown( 3, sc ),
								f.pushInt( "4", sc),
								f.apply( sc ),
						}
					},
					"40\n" +
					"10\n" +
					"0\n"
			),
			new TestData(
					"lookup size of a set",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "0", sc),
								f.pushInt( "10", sc),
								f.pushInt( "20", sc),
								f.pushInt( "30", sc),
								f.pushInt( "40", sc),
								f.makeSet( 5, sc ),
								f.pushString("size", sc),
								f.lookup( sc )
						}
					},
					"5\n"
			),
			new TestData(
					"lookup length of sequence",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "0", sc),
								f.pushInt( "10", sc),
								f.pushInt( "20", sc),
								f.pushInt( "30", sc),
								f.makeSeq( 4, sc ),
								f.pushString("length", sc),
								f.lookup( sc )
						}
					},
					"4\n"
			),
			new TestData(
					"binary addition 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(+)", sc),
								f.lookup( sc )
						}
					},
					"23.binary +\n"
			),
			new TestData(
					"binary addition 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(+)", sc),
								f.lookup( sc ),
								f.pushInt( "1000000000000000000", sc ),
								f.apply( sc ) 
						}
					},
					"1000000000000000023\n"
			),
			new TestData(
					"int subtraction",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(-)", sc),
								f.lookup( sc ),
								f.pushInt( "42", sc ),
								f.apply( sc ) 
						}
					},
					"-19\n"
			),
			new TestData(
					"int multiplications",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(*)", sc),
								f.lookup( sc ),
								f.pushInt( "2", sc ),
								f.apply( sc ) 
						}
					},
					"46\n"
			),
			new TestData(
					"int division 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(/)", sc),
								f.lookup( sc ),
								f.pushInt( "3", sc ),
								f.apply( sc ) 
						}
					},
					"7\n"
			),
			new TestData(
					"int division 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(div)", sc),
								f.lookup( sc ),
								f.pushInt( "3", sc ),
								f.apply( sc ) 
						}
					},
					"7\n"
			),
			new TestData(
					"int mod",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(mod)", sc),
								f.lookup( sc ),
								f.pushInt( "3", sc ),
								f.apply( sc ) 
						}
					},
					"2\n"
			),
			new TestData(
					"int negations",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("unary(-)", sc),
								f.lookup( sc ),
								f.makeTuple(0, sc ),
								f.apply( sc ) 
						}
					},
					"-23\n"
			),
			new TestData(
					"int = 0 ",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushInt( "3", sc ),
								f.apply( sc ) 
						}
					},
					"false\n"
			),
			new TestData(
					"int = 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ) 
						}
					},
					"true\n"
			),
			new TestData(
					"int != 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(!=)", sc),
								f.lookup( sc ),
								f.pushInt( "2", sc ),
								f.apply( sc ) 
						}
					},
					"true\n"
			),
			new TestData(
					"int != 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(!=)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ) 
						}
					},
					"false\n"
			),
			new TestData(
					"int comparison",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "23", sc),
								f.pushString("binary(<)", sc),
								f.lookup( sc ),
								f.pushInt( "22", sc ),
								f.apply( sc ), 
								
								f.pushInt( "23", sc),
								f.pushString("binary(<)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ), 

								f.pushInt( "23", sc),
								f.pushString("binary(<)", sc),
								f.lookup( sc ),
								f.pushInt( "24", sc ),
								f.apply( sc ), 

								f.pushInt( "23", sc),
								f.pushString("binary(<=)", sc),
								f.lookup( sc ),
								f.pushInt( "22", sc ),
								f.apply( sc ), 
								
								f.pushInt( "23", sc),
								f.pushString("binary(<=)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ), 
								
								f.pushInt( "23", sc),
								f.pushString("binary(<=)", sc),
								f.lookup( sc ),
								f.pushInt( "24", sc ),
								f.apply( sc ), 

								f.pushInt( "23", sc),
								f.pushString("binary(>)", sc),
								f.lookup( sc ),
								f.pushInt( "22", sc ),
								f.apply( sc ), 
								
								f.pushInt( "23", sc),
								f.pushString("binary(>)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ), 

								f.pushInt( "23", sc),
								f.pushString("binary(>)", sc),
								f.lookup( sc ),
								f.pushInt( "24", sc ),
								f.apply( sc ),  

								f.pushInt( "23", sc),
								f.pushString("binary(>=)", sc),
								f.lookup( sc ),
								f.pushInt( "22", sc ),
								f.apply( sc ),
								
								f.pushInt( "23", sc),
								f.pushString("binary(>=)", sc),
								f.lookup( sc ),
								f.pushInt( "23", sc ),
								f.apply( sc ), 

								f.pushInt( "23", sc),
								f.pushString("binary(>=)", sc),
								f.lookup( sc ),
								f.pushInt( "24", sc ),
								f.apply( sc ), 
						}
					},
					"false\n" +
					"true\n"+
					"true\n" +
					
					"false\n" +
					"false\n"+
					"true\n" +
					
					"true\n" +
					"true\n"+
					"false\n" +
					
					"true\n" +
					"false\n"+
					"false\n"
			),
			new TestData(
					"implication",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("binary(implies)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( false, sc),
								f.pushString("binary(implies)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("binary(implies)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( true, sc),
								f.pushString("binary(implies)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
						}
					},
					"true\n" +
					"false\n" +
					"true\n"+
					"true\n"
			),
			new TestData(
					"implication",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("binary(and)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( false, sc),
								f.pushString("binary(and)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("binary(and)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( true, sc),
								f.pushString("binary(and)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
						}
					},
					"true\n" +
					"false\n" +
					"false\n"+
					"false\n"
			),
			new TestData(
					"or",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("binary(or)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( false, sc),
								f.pushString("binary(or)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("binary(or)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( true, sc),
								f.pushString("binary(or)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
						}
					},
					"true\n" +
					"true\n" +
					"true\n"+
					"false\n"
			),
			new TestData(
					"boolean equal",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( false, sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( true, sc),
								f.pushString("binary(=)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
						}
					},
					"true\n" +
					"false\n" +
					"false\n"+
					"true\n"
			),
			new TestData(
					"boolean not equal",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("binary(/=)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( false, sc),
								f.pushString("binary(/=)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("binary(/=)", sc),
								f.lookup( sc ),
								f.pushBool( false, sc),
								f.apply( sc ), 

								f.pushBool( true, sc),
								f.pushString("binary(/=)", sc),
								f.lookup( sc ),
								f.pushBool( true, sc),
								f.apply( sc ), 
						}
					},
					"false\n" +
					"true\n" +
					"true\n"+
					"false\n"
			),
			new TestData(
					"boolean not",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushBool( false, sc),
								f.pushString("unary(not)", sc),
								f.lookup( sc ),
								f.makeTuple(0, sc),
								f.apply( sc ), 
								
								f.pushBool( true, sc),
								f.pushString("unary(not)", sc),
								f.lookup( sc ),
								f.makeTuple(0, sc),
								f.apply( sc ) 
						}
					},
					"false\n" +
					"true\n"
			),
			new TestData(
					"store a function 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x" , sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.constructType("Fun", 2, sc),
								f.newFrame(1, sc),
								
								f.pushLocation(0, 0, sc),
								f.unlockLocation( sc ),
								
								f.pushLocation(0, 0, sc),
								f.pushInt( "23", sc),
								f.pushString("binary(+)", sc),
								f.lookup( sc ),
								f.store( 1, sc ),
								
								f.popFrame( sc )
						}
					},
					"23.binary +\n"
			),
			new TestData(
					"store a function 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x" , sc),
								f.constructType("None", 0, sc),
								f.constructType("Any", 0, sc),
								f.constructType("Fun", 2, sc),
								f.newFrame(1, sc),
								
								f.pushLocation(0, 0, sc),
								f.unlockLocation( sc ),
								
								f.pushLocation(0, 0, sc),
								f.pushInt( "23", sc),
								f.pushString("binary(+)", sc),
								f.lookup( sc ),
								f.store( 1, sc ),
								
								f.popFrame( sc )
						}
					},
					"23.binary +\n"
			),
			new TestData(
					"return 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "7", sc ),
								f.returnNow( sc ),
								f.pushInt( "77", sc )
						}
					},
					"7\n"
			),
			new TestData(
					"jump 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "1", sc ),
								f.jump( 3, sc ),
								f.pushInt( "3", sc ),
								f.jump( 3, sc ),
								f.pushInt( "2", sc ),
								f.jump( -3, sc)
						}
					},
					"3\n2\n1\n"
			),
			new TestData(
					"jumpOnFalse 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "1", sc ),
								f.pushBool( true, sc),
								f.jumpOnFalse(0, sc),
								f.pushBool( false, sc),
								f.jumpOnFalse( 6, sc ),
								f.pushInt( "3", sc ),
								f.pushBool( true, sc),
								f.jumpOnFalse(99, sc),
								f.pushBool( false, sc),
								f.jumpOnFalse( 6, sc ),
								f.pushInt( "2", sc ),
								f.pushBool( true, sc),
								f.jumpOnFalse(99, sc),
								f.pushBool( false, sc),
								f.jumpOnFalse( -9, sc)
						}
					},
					"3\n2\n1\n"
			),
			new TestData(
					"jumpOnTrue 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "1", sc ),
								f.pushBool( false, sc),
								f.jumpOnTrue(0, sc),
								f.pushBool( true, sc),
								f.jumpOnTrue( 6, sc ),
								f.pushInt( "3", sc ),
								f.pushBool( false, sc),
								f.jumpOnTrue(99, sc),
								f.pushBool( true, sc),
								f.jumpOnTrue( 6, sc ),
								f.pushInt( "2", sc ),
								f.pushBool( false, sc),
								f.jumpOnTrue(99, sc),
								f.pushBool( true, sc),
								f.jumpOnTrue( -9, sc)
						}
					},
					"3\n2\n1\n"
			),
			new TestData(
					"makeClosure 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Int", 0, sc),
								f.pushInt("0", sc),
								f.makeClosure(0, sc)
						}
					},
					"function defined at a.pseu (line: 1 column: 2)--(line: 3 column: 4) : Unit -> Int\n"
			),
			new TestData(
					"makeClosure 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString("abc", sc),
								f.constructType("Bool", 0, sc),
								f.constructType("Int", 0, sc),
								f.pushInt("0", sc),
								f.makeClosure(1, sc)
						}
					},
					"function defined at a.pseu (line: 1 column: 2)--(line: 3 column: 4) : Bool -> Int\n"
			),
			new TestData(
					"makeClosure 2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString("abc", sc),
								f.constructType("Bool", 0, sc),
								f.pushString("xyz", sc),
								f.constructType("String", 0, sc),
								f.constructType("Int", 0, sc),
								f.pushInt("0", sc),
								f.makeClosure(2, sc)
						}
					},
					"function defined at a.pseu (line: 1 column: 2)--(line: 3 column: 4) : Product[Bool, String] -> Int\n"
			),
			new TestData(
					"function call 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt("99",  sc),
								f.constructType("Int", 0, sc),
								f.pushInt("1", sc),
								f.makeClosure(0, sc),
								f.makeTuple(0, sc),
								f.apply(sc)
						},
						new InstructionI[] {
								f.pushInt("42", sc),
								f.returnNow(sc)
						}
						
					},
					"42\n99\n"
			),
			new TestData(
					"function call 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt("99",  sc),
								
								// fun (abc : Int) : Bool do abc < 100 end fun
								f.pushString("abc", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Bool", 0, sc),
								f.pushInt("1", sc),
								f.makeClosure(1, sc),
								
								f.pushInt("88", sc),
								f.apply(sc)
						},
						new InstructionI[] {
								// fun (abc : Int) : Bool do abc < 100 end fun
								f.pushLocation(0, 0, sc),
								f.fetch(sc),
								f.pushString("binary(<)", sc),
								f.lookup( sc ),
								f.pushInt("100", sc),
								f.apply( sc ),
								f.returnNow(sc)
						}
						
					},
					"true\n99\n"
			),
			new TestData(
					"function call 2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt("99",  sc),
								
								// fun (a : Int, b : Int) : Int do a+b end fun
								f.pushString("a", sc),
								f.constructType("Int", 0, sc),
								f.pushString("b", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.pushInt("1", sc),
								f.makeClosure(2, sc),
								
								f.pushInt("88", sc),
								f.pushInt("100", sc),
								f.makeTuple(2, sc),
								f.apply(sc)
						},
						new InstructionI[] {
								f.newFrame(0, sc),
								f.pushLocation(1, 0, sc),
								f.fetch(sc),
								f.pushString("binary(+)", sc),
								f.lookup( sc ),
								f.pushLocation(1, 1, sc),
								f.fetch(sc),
								f.apply( sc ),
								f.returnNow(sc),
								f.popFrame(sc)
						}
						
					},
					"188\n99\n"
			),
			new TestData(
					"function returning a function",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString("f", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Any", 0, sc),
								f.constructType("Fun", 2, sc),
								f.pushString("g", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.constructType("Fun", 2, sc),
								f.pushString("h", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.constructType("Fun", 2, sc),
								f.newFrame( 3, sc ),
								// fun f( x : Int ) do 
								//     return fun( y : Int ) : Int do x+y end
								// end
								f.pushLocation( 0, 0, sc ),
								

								f.pushString("x", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.pushInt("1", sc),
								f.makeClosure(1, sc),
								f.pushLocation( 0, 0, sc ),
								f.unlockLocation( sc ),
								f.store(1, sc),
								
								f.pushLocation( 0, 0, sc ),
								f.lockLocation( sc ),
								
								f.pop(1	, sc),
								
								// val g : Int->Int := f(2) ;
								f.pushLocation( 0, 1, sc ),
								

								f.pushLocation( 0, 0, sc ),
								f.fetch( sc ),
								f.pushInt( "2", sc),
								f.apply( sc ),
								
								f.pushLocation( 0, 1, sc ),
								f.unlockLocation( sc ),
								f.store(1, sc),
								
								f.pushLocation( 0, 1, sc ),
								f.lockLocation( sc ),
								
								f.pop(1	, sc),
								
								// val h : Int->Int := f(3) ;
								f.pushLocation( 0, 2, sc ),
								

								f.pushLocation( 0, 0, sc ),
								f.fetch( sc ),
								f.pushInt( "3", sc),
								f.apply( sc ),
								
								f.pushLocation( 0, 2, sc ),
								f.unlockLocation( sc ),
								f.store(1, sc),
								
								f.pushLocation( 0, 2, sc ),
								f.lockLocation( sc ),
								
								f.pop(1	, sc),
								
								// (g(5), h(5))
								f.pushLocation( 0, 1, sc ),
								f.fetch( sc ),
								f.pushInt( "5", sc),
								f.apply( sc ),
								
								f.pushLocation( 0, 2, sc ),
								f.fetch( sc ),
								f.pushInt( "5", sc),
								f.apply( sc ),
								
								f.makeTuple(2, sc),
								
								f.returnNow( sc ),
								f.popFrame( sc )
						},
						new InstructionI[] {
								// return fun( y : Int ) : Int do x+y end
								f.newFrame(0, sc),
								
								f.pushString("y", sc),
								f.constructType("Int", 0, sc),
								f.constructType("Int", 0, sc),
								f.pushInt("2", sc),
								f.makeClosure(1, sc),
								f.returnNow(sc),
								f.popFrame(sc)
						},
						new InstructionI[] {
								//  x+y 
								f.newFrame(0, sc),
								
								f.pushLocation( 3, 0, sc ),
								f.fetch( sc ),
								f.pushString( "binary(+)", sc),
								f.lookup(sc),
								f.pushLocation( 1, 0, sc ),
								f.fetch( sc ),
								f.apply( sc ),
								
								f.returnNow(sc),
								f.popFrame(sc)
						}
						
					},
					"(7, 8)\n"
			)
		) ;
	}
	
	public class Exec implements Executable {

		private TestData data ;
		
		Exec( TestData data ) { this.data = data ; }
		
		@Override
		public void execute() throws Throwable {
			//System.out.println( data.name ) ;
			//System.out.println( data.codeStore.toString() ) ;
			machine.reset();
			machine.loadCode( data.codeStore );
			machine.run();
			String actual = machine.showValueStack() ;
			//System.out.println( "Expected:  <<"+data.expectedResult+">>" ) ;
			//System.out.println( "Result is: <<"+actual+">>" ) ;
			assertEquals( data.expectedResult, actual  ) ;
		}
	}
	
	@TestFactory
	public Stream<DynamicTest> allSimpleExecutionTest() {
		Stream<DynamicTest> stream =  testDataStream().map(
			(testData) -> DynamicTest.dynamicTest( testData.name,
					new Exec( testData )
			)
		) ;
		return stream ;
	}
}
