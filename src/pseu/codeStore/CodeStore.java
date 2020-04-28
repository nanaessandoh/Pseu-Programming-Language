package pseu.codeStore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Stack;

import pseu.common.Assert;

public class CodeStore< Instr extends InstructionI >
	implements CodeTarget<Instr>, CodeSource<Instr>
{

	private ArrayList<Segment<Instr>> segmentList = new ArrayList<Segment<Instr>>() ;
	private Stack<Integer> segmentStack = new Stack<Integer>() ;
	
	public CodeStore() {
		
	}
	
	public void dumpTheCode( PrintWriter writer ) {
		writer.format("The code store") ;
		writer.println() ;
		for( int i = 0 ; i < segmentList.size() ; ++i ) {
			writer.format( "    Start of Segment %d", i ) ;
			writer.println() ;
			segmentList.get(i).dumpTheSegment( writer ) ;
			writer.format( "    End of Segment %d", i ) ;
			writer.println() ;
		}
	}
	
	@Override public String toString() {
		StringWriter sw = new StringWriter() ;
		PrintWriter pw = new PrintWriter( sw ) ;
		dumpTheCode( pw ) ;
		return sw.toString();
	}
	
	@Override public int startNewSegment() {
		int newSegmentNumber = segmentList.size() ;
		Segment<Instr> newSegment = new Segment<Instr>() ;
		segmentList.add( newSegment ) ;
		segmentStack.push( newSegmentNumber ) ;
		return newSegmentNumber ;
	}
	
	@Override public void endSegment() {
		Assert.check( ! segmentStack.empty() ) ;
		segmentStack.pop();
	}
	
	@Override public void emit( Instr instruction ) {
		currentSegment().emit( instruction ) ;
	}
	
	@Override public void overwrite( Instr instruction, int address ) {
		currentSegment().overwrite( instruction, address ) ;
	}
	
	@Override public int getCurrentAddress( ) {
		return currentSegment().size() ;
	}
	
	@Override public Instr fetchInstruction( int segmentNumber, int address ) {
		Assert.check( hasInstruction( segmentNumber, address) );
		return segmentList.get( segmentNumber ).get( address ) ;
	}
	
	private Segment<Instr> currentSegment() {
		return segmentList.get( segmentStack.peek() ) ;
	}

	@Override
	public boolean hasInstruction(int segmentNumber, int address) {
		if( 0 <= segmentNumber && segmentNumber < segmentList.size() ) {
			Segment<Instr> seg = segmentList.get( segmentNumber ) ;
			return 0 <= address && address < seg.size() ;
		} else {
			return false ;
		}
	}
}