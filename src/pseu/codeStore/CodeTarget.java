package pseu.codeStore;

public interface CodeTarget<Instr extends InstructionI> {
	
	/** Start emitting to a new segment.
	 * <p> The segmentNumber of the new segment is returned as a result.
	 * The current segment before this call will be returned to after the
	 * the new segment is terminated by endSegment
	 * The very first segment created will be numbered 0.
	 * @see #endSegment() 
	 * @return
	 */
	public int startNewSegment() ;
	
	/** Stop emitting to the current segment.
	 * <p>  Following a call to endSegment, the next instructions will be emitted to
	 * the segment that was current when the current segment was started,
	 * provided such as segment exists.
	 * <p>Once a segment is ended, there is no way to add more instructions to it.
	 * To temporarily emit to another segment use startNewSegment.
	 * <p>Note that if all startSegment calls have been
	 * matched by an endSegment call, another call to endSegment is not allowed.
	 * <p> Precondition: There must be a current segment.
	 */
	public void endSegment() ;
	
	/** Put a new instruction a the end of the current segment. */
	public void emit( Instr instruction ) ;
	
	/** Overwrite an instruction emitted earlier with a new instruction.
	 * <p>All instructions are the same size, so exactly one instruction
	 * will be overwritten.
	 * <p> Precondition: The address must be the address of an instruction previously
	 * emitted.
	 * */
	public void overwrite( Instr instruction, int address ) ;
	
	/** Get the address of the next instruction to be emitted. */
	public int getCurrentAddress( ) ;

}
