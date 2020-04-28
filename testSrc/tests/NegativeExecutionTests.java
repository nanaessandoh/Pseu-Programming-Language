package tests;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable; 


import pseu.codeStore.CodeStore;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.interpreter.VirtualMachineI;

public abstract class NegativeExecutionTests<Instr extends InstructionI>{
	
	InstructionFactoryI<Instr> f ;
	VirtualMachineI<Instr> machine ;
	
	class TestData {
		String name ;
		CodeStore<Instr> codeStore ;
		SourceCoords expectedCoords ;
		Pattern expectedMessage ;
		
		TestData( String name, InstructionI[][] instructions, SourceCoords expectedCoords, Pattern expectedMessage) {
			this.name = name ;
			this.expectedCoords = expectedCoords ;
			this.expectedMessage = expectedMessage ;
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
		SourceCoords good = new SourceCoords("a.pseu", 1, 2, "a.pseu", 3, 4) ;
		SourceCoords bad = new SourceCoords("a.pseu", 13, 13, "a.pseu", 13, 13) ;
		return Stream.of(
			new TestData(
					"bad type 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("foo", 0, bad) 
						}
					},
					bad,
					Pattern.compile("No type named foo[.]")
			),
			new TestData(
					"bad type 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Bool", 1, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Bool can not have 1 argument\\(s\\)[.]")
			),
			new TestData(
					"bad type 2",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Fun", 1, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Fun can not have 1 argument\\(s\\)[.]")
			),
			new TestData(
					"bad type 3",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Product", 0, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Product can not have 0 argument\\(s\\)[.]")
			),
			new TestData(
					"bad type 3",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Product", 1, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Product can not have 1 argument\\(s\\)[.]")
			),
			new TestData(
					"bad type 4",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Set", 0, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Set can not have 0 argument\\(s\\)[.]")
			),
			new TestData(
					"bad type 5",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType("Set", 2, bad) 
						}
					},
					bad,
					Pattern.compile("Type constructor Set can not have 2 argument\\(s\\)[.]")
			),
			new TestData(
					"Bad Int 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "abc", bad)
						}
					},
					bad,
					Pattern.compile("Bad integer constant.")
			),
			new TestData(
					"Underflow 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "123", good),
								f.makeTuple(2, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow")
			),
			new TestData(
					"Underflow 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "123", good),
								f.makeSeq(2, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow")
			),
			new TestData(
					"Underflow 2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "123", good),
								f.makeSet(2, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow")
			),
			new TestData(
					"Bad makeClosure 0",
					new InstructionI[][] {
						new InstructionI[] {
								f.makeClosure(0, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow")
			),
			new TestData(
					"Bad makeClosure 1",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "7", good),
								f.makeClosure(1, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow")
			),
			new TestData(
					"Bad makeClosure 2",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "7", good),
								f.pushInt( "7", good),
								f.makeClosure(0, bad)
						}
					},
					bad,
					Pattern.compile("Expected a type on the stack, but found something else[.]")
			),
			new TestData(
					"Bad makeClosure 3",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType( "Bool", 0, good),
								f.pushString( "abc", good),
								f.makeClosure(0, bad)
						}
					},
					bad,
					Pattern.compile("Expected an integer on the stack, but found something else[.]")
			),
			new TestData(
					"Bad makeClosure 4",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType( "Bool", 0, good),
								f.pushInt( "12345678901234567890", good),
								f.makeClosure(0, bad)
						}
					},
					bad,
					Pattern.compile("Expected a small integer on the stack, but found a large one[.]")
			),
			new TestData(
					"Bad makeClosure 5",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType( "Bool", 0, good),
								f.pushInt( "1", good),
								f.makeClosure(1, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow.")
			),
			new TestData(
					"Bad makeClosure 6",
					new InstructionI[][] {
						new InstructionI[] {
								f.constructType( "Bool", 0, good),
								f.constructType( "Bool", 0, good),
								f.pushInt( "1", good),
								f.makeClosure(1, bad)
						}
					},
					bad,
					Pattern.compile("Stack underflow.")
			),
			new TestData(
					"Bad makeClosure 7",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushInt( "0", good),
								f.constructType( "Bool", 0, good),
								f.constructType( "Bool", 0, good),
								f.pushInt( "1", good),
								f.makeClosure(1, bad)
						}
					},
					bad,
					Pattern.compile("Parameter name is not a String[.]")
			),
			new TestData(
					"Bad makeClosure 8",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x", good),
								f.pushInt( "0", good),
								f.constructType( "Bool", 0, good),
								f.pushInt( "1", good),
								f.makeClosure(1, bad)
						}
					},
					bad,
					Pattern.compile("Expected a type on the stack, but found something else[.]")
			),
			new TestData(
					"Bad makeClosure 9",
					new InstructionI[][] {
						new InstructionI[] {
								f.pushString( "x", good),
								f.constructType( "Int", 0, good),
								f.pushString( "x", good),
								f.constructType( "Int", 0, good),
								f.constructType( "Bool", 0, good),
								f.pushInt( "1", good),
								f.makeClosure(2, bad)
						}
					},
					bad,
					Pattern.compile("abc")
			),
			new TestData(
					"Bad pushLocation 0",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushLocation(0, 999, bad)
						}
					},
					bad,
					Pattern.compile("Bad location address [(]0, 999[)][.]")
			),
			new TestData(
					"Bad pushLocation 1",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushLocation(1, 0, bad)
						}
					},
					bad,
					Pattern.compile("Bad location address [(]1, 0[)][.]")
			),
			new TestData(
					"Bad pushLocation 2",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushLocation(-1, 0, bad)
						}
					},
					bad,
					Pattern.compile("Bad location address [(]-1, 0[)][.]")
			),
			new TestData(
					"Bad pushLocation 3",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushLocation(0, -1, bad)
						}
					},
					bad,
					Pattern.compile("Bad location address [(]0, -1[)][.]")
			),
			new TestData(
					"Bad fetch 0",
					new InstructionI[][] {
						new InstructionI[] {
							f.fetch( bad )
						}
					},
					bad,
					Pattern.compile("Stack underflow[.]")
			),
			new TestData(
					"Bad fetch 1",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushInt("1", good),
							f.fetch( bad )
						}
					},
					bad,
					Pattern.compile("Expected a location on the stack, but found something else[.]")
			),
			new TestData(
					"Bad fetch 2",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.newFrame( 1, good ),
							f.pushLocation(0, 0, good),
							f.fetch( bad )
						}
					},
					bad,
					Pattern.compile("Tried to read a location before it was written to[.]")
			),
			new TestData(
					"Bad store 0",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.newFrame( 1, good ),
							f.pushLocation(0, 0, good),
							f.pushBool(true, good),
							f.store(1, bad )
						}
					},
					bad,
					Pattern.compile("Tried to write to a location that is not writable[.]")
			),
			new TestData(
					"Bad store 1",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.newFrame( 1, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.pushLocation(0, 0, good),
							f.pushInt("123", good),
							f.store(1, bad )
						}
					},
					bad,
					Pattern.compile("Tried to write value 123 to a location of type Bool[.]")
			),
			new TestData(
					"Bad store 2",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.newFrame( 1, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.store(0, bad )
						}
					},
					bad,
					Pattern.compile("Stack underflow[.]")
			),
			new TestData(
					"Bad store 3",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.pushString("y", good),
							f.constructType("Int", 0, good),
							f.newFrame( 2, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.pushLocation(0, 1, good),
							f.unlockLocation(good),
							f.pushLocation(0, 0, good),
							f.pushLocation(0, 1, good),
							f.pushInt("1", good),
							f.store(2, bad )
						}
					},
					bad,
					Pattern.compile("Tried to store to multiple locations, but the value is not a tuple.")
			),
			new TestData(
					"Bad store 4",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.pushString("y", good),
							f.constructType("Int", 0, good),
							f.newFrame( 2, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.pushLocation(0, 1, good),
							f.unlockLocation(good),
							f.pushLocation(0, 0, good),
							f.pushLocation(0, 1, good),
							f.pushInt("1", good),
							f.pushInt("1", good),
							f.pushInt("1", good),
							f.makeTuple(3, good),
							f.store(2, bad )
						}
					},
					bad,
					Pattern.compile("Tried to store to 2 locations, but the value is a tuple of length 3.")
			),
			new TestData(
					"Bad store 5",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.pushString("y", good),
							f.constructType("Int", 0, good),
							f.pushString("z", good),
							f.constructType("String", 0, good),
							f.newFrame( 3, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.pushLocation(0, 1, good),
							f.unlockLocation(good),
							f.pushLocation(0, 2, good),
							f.unlockLocation(good),
							f.pushLocation(0, 0, good),
							f.pushLocation(0, 1, good),
							f.pushLocation(0, 2, good),
							f.pushInt("1", good),
							f.pushInt("1", good),
							f.makeTuple(2, good),
							f.store(3, bad )
						}
					},
					bad,
					Pattern.compile("Tried to store to 3 locations, but the value is a tuple of length 2.")
			),
			new TestData(
					"Bad store 6",
					new InstructionI[][] {
						new InstructionI[] {
							f.pushString("x", good),
							f.constructType("Bool", 0, good),
							f.pushString("y", good),
							f.constructType("Int", 0, good),
							f.pushString("z", good),
							f.constructType("String", 0, good),
							f.newFrame( 3, good ),
							f.pushLocation(0, 0, good),
							f.unlockLocation(good),
							f.pushLocation(0, 1, good),
							f.unlockLocation(good),
							f.pushLocation(0, 2, good),
							f.unlockLocation(good),
							f.pushLocation(0, 0, good),
							f.pushLocation(0, 1, good),
							f.pushLocation(0, 2, good),
							f.pushInt("1", good),
							f.pushInt("1", good),
							f.makeTuple(2, good),
							f.store(4, bad )
						}
					},
					bad,
					Pattern.compile("Stack underflow[.]")
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
			boolean ok = false ;
			try {
				machine.run(); }
			catch( RunTimeError rte ) {
				assertEquals( data.expectedCoords, rte.getCoords() ) ;
				assertTrue(
					"Message " +rte.getMessage()+ " did not match " + data.expectedMessage.pattern(),
					data.expectedMessage.asPredicate().test( rte.getMessage() ) ) ;
					ok = true ; }
			if( !ok ) assertTrue( "No exception thrown!", false ) ;
		}
	}
	
	@TestFactory
	public Stream<DynamicTest> allNegativeExecutionTest() {
		Stream<DynamicTest> stream =  testDataStream().map(
			(testData) -> DynamicTest.dynamicTest( testData.name,
					new Exec( testData )
			)
		) ;
		return stream ;
	}
}
