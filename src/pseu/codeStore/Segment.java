package pseu.codeStore;

import pseu.common.Assert;

import java.io.PrintWriter;
import java.util.ArrayList;

class Segment< Instr extends InstructionI > {
	
	private ArrayList<Instr> instructionList = new ArrayList<Instr>() ;
	
	void dumpTheSegment( PrintWriter writer ) {
		for( int i = 0 ; i < instructionList.size(); ++i ) {
			writer.format( "        %3d : %s", i, instructionList.get(i).toString() ) ;
			writer.println();
		}
	}

	void emit(Instr instruction) {
		instructionList.add( instruction ) ;
	}

	void overwrite(Instr instruction, int address) {
		Assert.check( 0 <= address ); 
		Assert.check( address < instructionList.size() );
		instructionList.set( address, instruction ) ;
	}

	Instr get(int address) {
		return instructionList.get( address ) ;
	}

	int size() {
		return instructionList.size() ;
	}

}
