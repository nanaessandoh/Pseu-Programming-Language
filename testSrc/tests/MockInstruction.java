package tests;

import pseu.codeStore.InstructionI;
import pseu.common.SourceCoords;

public class MockInstruction implements InstructionI {
	final SourceCoords coords ;
	String info ;
	
	MockInstruction( String info, SourceCoords coords) {
		this.info = info ;
		this.coords = coords ;
	}
	
	@Override
	public SourceCoords getSourceCoords() {
		return coords ;
	}
	
	@Override
	public String toString() {
		return info + " " + coords.toString() ;
	}
}
