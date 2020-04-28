// You can modify and use this file to make a stand-alone
// Java program to implement the Pseu language.
// See modification instructions below.
package tests ;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import pseu.common.CompileTimeError;
import pseu.common.RunTimeError;
import pseu.interpreter.Interpreter;
import pseu.interpreter.InterpreterI;
import pseu.parser.ParseException;
import pseu.parser.TokenMgrError;
import pseu.tsn.psam.Instruction;
import pseu.tsn.psam.InstructionFactory;
import pseu.tsn.psam.PSAM;

public class Main {

	public static void main(String[] args) {
		String fileName ;
		Reader reader ;
		if( args.length > 0 ) {
			fileName = args[0] ;
			File programFile = new File( fileName ) ;
			try {
				reader = new FileReader( programFile ) ;
			} catch (FileNotFoundException e) {
				System.err.println( "File "+fileName+" not found.!") ;
				return ;
			}
		} else {
			fileName = "stdin" ;
			reader = new InputStreamReader( System.in ) ;
		}
		// On the next line, in place of null, put a
		// the construction of your own
		// implementation of the interpreter interface.
		//  E.g. ... = new GroupXInterpreter() ;
		InterpreterI interpreter = new MockInterpreter() ;
		try {
			interpreter.compileAndRun(fileName, reader, false);
		} catch (ParseException e) {
			System.err.println("Parsing error." ) ;
			System.err.println( e.getLocalizedMessage() ) ;
		} catch (TokenMgrError e) {
			System.err.println("Lexing error." ) ;
			System.err.println( e.getLocalizedMessage() ) ;
		} catch (CompileTimeError e) {
			System.err.println("Compilation error at ."+e.getCoords().toString() ) ;
			System.err.println( e.getLocalizedMessage() ) ;
		} catch (RunTimeError e) {
			System.err.println("Execution error at ."+e.getCoords().toString() ) ;
			System.err.println( e.getLocalizedMessage() ) ;
		}
	}
}

class MockInterpreter extends Interpreter<MockAST, Instruction> {
	MockInterpreter() {
        super( new MockBuilder(),
               new InstructionFactory(),
               new MockAnalyser(),
               new PSAM() ) ;
    }
}
