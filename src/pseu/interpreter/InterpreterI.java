package pseu.interpreter;

import java.io.PrintWriter;
import java.io.Reader;

import pseu.common.CompileTimeError;
import pseu.common.RunTimeError;
import pseu.parser.ParseException;
import pseu.parser.TokenMgrError;

public interface InterpreterI {

	void compileAndRun(String fileName, Reader reader, boolean beVerbose) throws ParseException, TokenMgrError, // First compilation pass
			CompileTimeError, // Second (and subsequent) compilation pass(es)
			RunTimeError // Execution 
	;

	void compileAndRun(String fileName, Reader reader, Reader input, PrintWriter output, boolean beVerbose)
			throws ParseException, TokenMgrError, // First compilation pass
			CompileTimeError, // Second (and subsequent) compilation pass(es)
			RunTimeError // Execution 
	;

}