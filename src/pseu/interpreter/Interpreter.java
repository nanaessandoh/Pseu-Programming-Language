package pseu.interpreter;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import pseu.codeStore.CodeStore;
import pseu.codeStore.InstructionFactoryI;
import pseu.codeStore.InstructionI;
import pseu.common.CompileTimeError;
import pseu.common.RunTimeError;
import pseu.parser.ParseException;
import pseu.parser.PseuParser;
import pseu.parser.TokenMgrError;

public abstract class Interpreter<AST, Instr extends InstructionI>
implements InterpreterI
{
	// The following are the dependences of this object.
	final protected ASTSourceI<AST> builder ;
	final protected InstructionFactoryI< Instr > instructionFactory ;
	final protected AnalyzerAndGeneratorI<AST> analyser ;
	final protected VirtualMachineI<Instr> psam ;

	public Interpreter( 
			    ASTSourceI<AST> builder,
				InstructionFactoryI< Instr > instructionFactory,
				AnalyzerAndGeneratorI<AST> analyser,
				VirtualMachineI<Instr> psam ) {
		// Inject the dependences.
		this.builder = builder ;
		this.instructionFactory = instructionFactory ;
		this.analyser = analyser ;
		this.psam = psam ;
	}

	/* (non-Javadoc)
	 * @see pseu.interpreter.InterpreterI#compileAndRun(java.lang.String, java.io.Reader, boolean)
	 */
	@Override
	public void compileAndRun(
			String fileName,
			Reader reader,
			boolean beVerbose )
	throws ParseException, TokenMgrError, // First compilation pass
	       CompileTimeError,  // Second (and subsequent) compilation pass(es)
	       RunTimeError // Execution 
	{
		Reader input = new InputStreamReader( System.in ) ;
		PrintWriter output = new PrintWriter( System.out ) ;
		compileAndRun( fileName, reader, input, output, beVerbose ) ;
	}

	/* (non-Javadoc)
	 * @see pseu.interpreter.InterpreterI#compileAndRun(java.lang.String, java.io.Reader, java.io.Reader, java.io.PrintWriter, boolean)
	 */
	@Override
	public void compileAndRun(
			String fileName,
			Reader reader,
			Reader input,
			PrintWriter output,
			boolean beVerbose )
	throws ParseException, TokenMgrError, // First compilation pass
	       CompileTimeError,  // Second (and subsequent) compilation pass(es)
	       RunTimeError // Execution 
	{
		// First compilation pass: Lexing, Parsing, Tree Building.
			PseuParser parser = new PseuParser( reader ) ;
			parser.setBuilder( builder ) ;
			parser.setFileName( fileName ) ;
			parser.File() ;
			AST ast = builder.getAST() ;
		// Second (and subsequent) compilation pass(es): Analysis and code
		// generation.
			CodeStore<Instr> codeStore = new CodeStore<Instr>() ;
			analyser.<Instr>analyzeAndGenerate(ast, instructionFactory, codeStore) ;
		// Execution on the virtual machine
			psam.reset();
			psam.setVerbosity( beVerbose );
			psam.loadCode( codeStore ) ;
			psam.setInputSource( input ); 
			psam.setOutputTarget( output );
			psam.run( ) ;
			output.println( psam.showValueStackTop() ) ;
			output.flush();
	}
}
