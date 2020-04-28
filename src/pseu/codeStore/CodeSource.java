package pseu.codeStore;

public interface CodeSource<Instr extends InstructionI> {
	
	/** Get the instruction at given (segmentNumber, address) pair.
	 * <p>Precondition: There must be an instruction at the given address.
	 * @param segmentNumber
	 * @param address
	 * @return
	 */
	public Instr fetchInstruction( int segmentNumber, int address ) ;
	
	public boolean hasInstruction( int segmentNumber, int address ) ;
}
