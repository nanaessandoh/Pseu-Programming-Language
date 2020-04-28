package pseu.interpreter;

import java.io.PrintWriter;
import java.io.Reader;

import pseu.codeStore.CodeSource;
import pseu.codeStore.InstructionI;
import pseu.common.RunTimeError;

public interface VirtualMachineI<Instr extends InstructionI> {
	
	/** Reset the state of the machine to the initial state.
	 * <p>This should be called first.
	 */
	void reset() ;
	
	/** Load instructions into the machine.
	 * <p>This should be called after reset
	 * and before any code is executed.
	 * @param codeSource
	 */
	void loadCode( CodeSource<Instr> codeSource ) ;
	
	/** Set a place to get any input.
	 * The default should be standard input.
	 * @param r
	 */
	void setInputSource( Reader r ) ;
	
	/** Set a place to get send any output.
	 * The default should be standard output.
	 * @param pw
	 */
	void setOutputTarget( PrintWriter pw ) ;
	
	/** Make the machine verbose or not.
	 * <p> When verbosity is true, the machine
	 * will print out some information before and
	 * after each step.
	 */
	void setVerbosity( boolean beVerbose ) ;
	
	/** Run until the machine stops
	 * 
	 * @throws RunTimeError
	 */
	void run( ) throws RunTimeError ;
	
	/** Can the machine make another step? */
	boolean canStep() ;
	
	/** Execute one instruction
	 * <p> If the machine is in a stopped state, this
	 * will have no effect on its state.
	 * @throws RunTimeError
	 */
	void singleStep( ) throws RunTimeError ;
	
	/** Return a string representation of the value stack */
	String showValueStack() ;
	
	/** Show the top value on the value stack as a string. */
	String showValueStackTop() ;

	/** How much stuff is on the valueStack */
	int valueStackDepth();
}
