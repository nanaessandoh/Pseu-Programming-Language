package pseu.tsn.psam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Formatter;
import java.util.Stack;

import pseu.codeStore.CodeSource;
import pseu.codeStore.CodeStore;
import pseu.common.Assert;
import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.interpreter.VirtualMachineI;
import pseu.tsn.psam.locations.Frame;
import pseu.tsn.psam.locations.GlobalFrame;
import pseu.tsn.psam.locations.Location;
import pseu.tsn.psam.values.Value;
import pseu.tsn.psam.values.BuiltInFunctionValue;
import pseu.tsn.psam.values.Type;
import pseu.tsn.psam.values.TypeName;

public class PSAM implements VirtualMachineI<Instruction> {
	
	private class RStackRecord {
		final int seg ;
		final int pc ;
		final int vStackHeight ;
		final Frame framePointer ;
		final boolean running ;
		final Type resultType ;
		
		RStackRecord( int seg, int pc, int vStackHeight, Frame framePointer, boolean running, Type resultType ) {
			this.seg = seg ; this.pc = pc; this.vStackHeight = vStackHeight;
			this.framePointer = framePointer ; this.running = running ; this.resultType = resultType ;
		}
	}
	
	private CodeSource<Instruction> code = new CodeStore<Instruction>() ;
	private int seg = 0 ;
	private int pc = 0 ;
	private Frame framePointer = GlobalFrame.makeGlobalFrame();
	private boolean running = false ;
	private Stack<StackItem> vStack = new Stack<StackItem>() ;
	private Stack<RStackRecord> rStack = new Stack<RStackRecord> () ;
	{ rStack.push( initialRStackRecord() ) ; }
	private PrintWriter printTarget = new PrintWriter( System.out );
	private BufferedReader  inputSource  = new BufferedReader( new InputStreamReader( System.in  ) );
	private boolean verbose = false ;

	@Override
	public void reset() {
		seg = 0 ;
		pc = 0 ;
		vStack.clear() ;
		framePointer = GlobalFrame.makeGlobalFrame();
		running = false ;
		code = new CodeStore() ;
		rStack.clear();
		{ rStack.push( initialRStackRecord() ) ; }
		verbose = false ;
		lastFramePointer = null ;
	}

	@Override
	public void loadCode(CodeSource<Instruction> codeSource) {
		Assert.check( codeSource != null ) ;
		code = codeSource ;
		running = true ;
	}

	public BufferedReader getInputSource() {
		return inputSource ;
	}

	@Override
	public void setInputSource(Reader r) {
		if( r instanceof BufferedReader ) inputSource = (BufferedReader) r ;
		else inputSource = new BufferedReader( r ) ;
	}

	public PrintWriter getOutputTarget() {
		return printTarget ;
	}

	@Override
	public void setOutputTarget(PrintWriter pw) {
		printTarget = pw ;
	}

	@Override
	public void setVerbosity(boolean beVerbose) {
		this.verbose = beVerbose ;
	}

	@Override
	public void run() throws RunTimeError {
		if( verbose ) {
			System.out.println( "===================") ;
			System.out.println( "Start of execution") ;
			System.out.println( code.toString() ) ;
		}
		showState() ;
		while( canStep() ) {
			showInstruction() ;
			singleStep() ;
			showState() ;
		}
		running = false ;

		if( verbose ) {
			System.out.println( "End of execution") ;
			System.out.println( "===================") ;
		}
	}

	@Override
	public boolean canStep() {
		return running && code.hasInstruction( seg, pc );
	}

	@Override
	public void singleStep() throws RunTimeError {
		Assert.check( canStep() );
		Instruction i = code.fetchInstruction(seg, pc) ;
		i.execute( this );
	}
	
	private Frame lastFramePointer = null ;
	
	private void showState() {
		if( this.verbose ) {
			System.out.println( "VStack is:" ) ;
			System.out.println( showValueStack() ) ;
			if( framePointer != null && framePointer!=lastFramePointer  )  {
				System.out.println( framePointer.toString() ) ;
				lastFramePointer = framePointer ;
			}
			System.out.println( "seg = " +seg+ " pc = " +pc+ " call depth = " +rStack.size() ) ;
		}
	}
	
	private void showInstruction() {
		if( this.verbose ) {
			System.out.println( "Next instruction is " + code.fetchInstruction(seg, pc).toString() ) ;
		}
	}

	@Override
	public String showValueStack() {
		Formatter fmt = new Formatter() ;
		for( int i=0, sz=vStack.size() ; i < sz ; ++i ) {
			vStack.elementAt(sz-1-i).show( fmt );
			fmt.format("\n" ) ; }
		return fmt.toString () ;
	}

	@Override
	public String showValueStackTop() {
		int sz = vStack.size() ;
		Assert.check( sz > 0 ) ;
		Formatter fmt = new Formatter() ;
		vStack.get(sz-1).show( fmt ) ; 
		return fmt.toString() ;
	}

	@Override
	public int valueStackDepth() {
		return vStack.size() ;
	}
	
	public void tick() { tick(1) ; }
	
	void tick(int i) { pc += i ; }
	
	void setPC( int i ) { pc = i ; }
	
	void setSegNumber( int segNum )  throws RunTimeError {
		if( code.hasInstruction(segNum, 0) )
			this.seg = segNum ;
		else
			error( "Invalid Segment number") ;
	}
	
	SourceCoords currentSourceCoords() {
		if( code.hasInstruction( seg, pc ) ) {
			return code.fetchInstruction(seg, pc).sourceCoords;
		} else {
			throw new AssertionError("Internal machine error") ; }
	}
	
	public void push( StackItem item ) {
		vStack.push( item ) ;
	}
	
	StackItem pop() throws RunTimeError {
		if( vStack.empty() ) 
			throw new RunTimeError( "Stack underflow.",
					currentSourceCoords(), seg, pc) ;
		return vStack.pop() ;
	}
	
	Value popValue() throws RunTimeError {
		StackItem item = pop() ;
		if( item instanceof Value) return (Value)item ;
		else throw new RunTimeError("Expected a value on the stack, but found something else.",
				currentSourceCoords(), seg, pc) ;
	}
	
	Location popLocation() throws RunTimeError {
		StackItem item = pop() ;
		if( item instanceof Location) return (Location)item ;
		else throw new RunTimeError("Expected a location on the stack, but found something else.",
				currentSourceCoords(), seg, pc) ;
	}
	
	Type popType() throws RunTimeError {
		StackItem item = pop() ;
		if( item instanceof Type) return (Type)item ;
		else throw new RunTimeError("Expected a type on the stack, but found something else.",
				currentSourceCoords(), seg, pc) ;
	}

	
	StackItem[] pop( int count ) throws RunTimeError {
		StackItem[] values = new StackItem[count] ;
		for( int i = count-1 ; i >= 0 ; --i)
			values[i] = pop() ; 
		return values ;
	}
	
	Value[] popValues( int count ) throws RunTimeError {
		Value[] values = new Value[count] ;
		for( int i = count-1 ; i >= 0 ; --i)
			values[i] = popValue() ; 
		return values ;
	}
	
	Type[] popTypes( int count ) throws RunTimeError {
		Type[] types = new Type[count] ;
		for( int i = count-1 ; i >= 0 ; --i)
			types[i] = popType() ; 
		return types ;
	}

	
	Frame getFrame( ) {
		return framePointer;
	}
	
	public void setFrame( Frame frame ) {
		this.framePointer = frame ;
	}
	
	void popFrame() throws RunTimeError {
		if( framePointer.hasParent() ) 
			framePointer = framePointer.getParent() ;
		else 
			throw new RunTimeError("Frame can not be popped.",
					currentSourceCoords(), seg, pc) ;
	}
	
	public void error(String message) throws RunTimeError {
		throw new RunTimeError(message, currentSourceCoords(), seg, pc) ;
	}
	
	public void call(int newSegment, Frame newFrame, Type resultType )  throws RunTimeError {
		RStackRecord returnRecord = new RStackRecord(seg, pc+1, valueStackDepth(), framePointer, running, resultType) ;
		rStack.push( returnRecord ) ;
		setFrame( newFrame ) ;
		setSegNumber( newSegment ) ;
		setPC( 0 ) ;
	}
	
	public void doReturn() throws RunTimeError {
		Assert.check( ! rStack.isEmpty() );
		RStackRecord rRecord = rStack.pop() ;
		Value val = popValue() ;
		if( ! rRecord.resultType.containsValue( val ) ) {
			error( String.format("Result %s of function is not of the expected type %s",
				   val.toString(),
				   rRecord.resultType ) ) ; }
		if( vStack.size() < rRecord.vStackHeight ) {
			error( String.format("vStack is only %d items, but should be at least %d items",
				   vStack.size(),
				   rRecord.vStackHeight ) ) ; }
		pop( vStack.size() - rRecord.vStackHeight ) ;
		seg = rRecord.seg ;
		pc = rRecord.pc ;
		running = rRecord.running ;
		framePointer = rRecord.framePointer ;
		push(val) ;
	}
	
	private RStackRecord initialRStackRecord() {
		return new RStackRecord(0, 0, 0, null, false, TypeName.anyType ) ;
	}
}
