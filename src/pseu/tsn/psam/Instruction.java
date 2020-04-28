package pseu.tsn.psam;

import java.util.Formatter;

import pseu.codeStore.InstructionI;
import pseu.common.Assert;
import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.locations.Frame;
import pseu.tsn.psam.locations.Location;
import pseu.tsn.psam.values.*;

public abstract class Instruction implements InstructionI
{
	final protected SourceCoords sourceCoords ;
	
	protected Instruction(SourceCoords sourceCoords) {
		this.sourceCoords = sourceCoords ;
	}

	@Override
	public SourceCoords getSourceCoords() {
		return sourceCoords ;
	}
	
	abstract void execute( PSAM vm ) throws RunTimeError ;
	
	abstract void show( Formatter fmt ) ;
	
	@Override public String toString() {
		Formatter fmt = new Formatter() ;
		show( fmt ) ;
		return fmt.toString() ;
	}

}

class PushInt extends Instruction {
	private String str ;
	
	PushInt(String str, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.str = str ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.push( IntValue.make( str, vm ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "pushInt(%s)", str) ;
	}

}

class PushString extends Instruction {
	private String str ;
	
	PushString(String str, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.str = str ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.push( StringValue.make( str ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "pushString(%s)", str) ;
	}
}


class PushBool extends Instruction {
	private boolean value ;
	
	PushBool(boolean value, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.value = value ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.push( BoolValue.make( value ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "pushBool(%b)", value) ;
	}
}

class MakeTuple extends Instruction {
	private int count ;
	
	MakeTuple(int count, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value values[] = vm.popValues( count ) ;
		vm.push( TupleValue.make( values ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "makeTuple(%d)", count) ;
	}
}

class MakeSet extends Instruction {
	private int count ;
	
	MakeSet(int count, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value values[] = vm.popValues( count ) ;
		vm.push( SetValue.make( values ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "makeSet(%d)", count) ;
	}
}

class MakeSeq extends Instruction {
	private int count ;
	
	MakeSeq(int count, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value values[] = vm.popValues( count ) ;
		vm.push( SequenceValue.make( values ) ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "makeSeq(%d)", count) ;
	}
}

class MakeClosure extends Instruction {
	private int count ;
	
	MakeClosure(int count, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value segNumValue = vm.popValue() ;
		if( ! segNumValue.isInt() )
			vm.error( "Expected an integer on the stack, but found something else.");
		IntValue segNumIntValue = (IntValue) segNumValue ;
		if( ! segNumIntValue.canConvertToInt() )
			vm.error( "Expected a small integer on the stack, but found a large one.");
		int seg = segNumIntValue.toInt()  ;
		Type resultType = vm.popType() ;
		String[] paramNames = new String[ count ] ;
		Type[] paramTypes = new Type[ count ] ;
		for( int i=count-1 ; i >= 0 ; --i ) {
			paramTypes[i] = vm.popType() ;
			Value paramNameVal = vm.popValue() ;
			if( ! paramNameVal.isString() ) 
				vm.error( "Parameter name is not a String." ) ;
			StringValue temp = (StringValue) paramNameVal ;
			paramNames[i] = temp.getString() ; }
		Type argType ;
		if( count == 0 ) argType = TypeName.unitType ;
		else if( count == 1 ) argType = paramTypes[0] ;
		else argType = Type.make( TypeName.ProductTypeName, paramTypes) ;
		Value closure = new ClosureValue(argType,
				                         resultType,
				                         paramNames,
				                         paramTypes,
				                         seg,
				                         vm.getFrame(),
				                         this.sourceCoords ) ;
		vm.push( closure ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "makeClosure(%d)", count) ;
	}
}

class PushLocation extends Instruction {
	
	private int depth;
	private int address;

	PushLocation(int depth, int address, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.depth = depth ;
		this.address = address ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Location loc = vm.getFrame().getLocation(depth, address) ;
		if( loc == null)
			vm.error("Bad location address (" +depth+ ", " +address+ ")." ) ;
		vm.push( loc );
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "pushLocation(%d, %d)", depth, address) ;
	}
}

class Fetch extends Instruction {
	Fetch(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Location loc = vm.popLocation() ;
		if( ! loc.isReadable() ) 
			vm.error("Tried to read a location before it was written to.") ;
		Value val = loc.getValue() ;
		Assert.check( val != null ) ;
		vm.push( val ); 
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "fetch()") ;
	}
}

class Store extends Instruction {
	private int count ;
	
	Store(int count, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError { 
		// Pop a value off the stack.
		Value val = vm.popValue() ;
		
		// Construct an array of locations
		Location locs[] = new Location[count] ;
		for( int i=count-1 ; i >= 0 ; --i ) {
			locs[i] = vm.popLocation() ;
		}
		
		// Construct an array of values
		Value[] values = new Value[count] ;
		if( count == 1 ) values[0] = val ;
		else if( ! val.isTuple() ) 
			vm.error("Tried to store to multiple locations, but the value is not a tuple.") ;
		else {
			TupleValue tuple = (TupleValue) val ;
			if( tuple.size() != count ) 
				vm.error( "Tried to store to " +count+ " locations, " +
				          "but the value is a tuple of length " +tuple.size()+ "." ) ;
			else {
				for( int i=0 ; i < count ; ++i )
					values[i] = tuple.get(i) ;
			}
		}
		
		// Store each values into each location.
		for( int i=0 ; i < count ; ++i ) {
			if( ! locs[i].isWritable() ) 
				vm.error( "Tried to write to a location " +
					      "that is not writable.") ;
			if( ! locs[i].getType().containsValue( values[i]) )
				vm.error(
					"Tried to write value " +values[i]+
					" to a location of type " +locs[i].getType()+ "." ) ;
			locs[i].setValue( values[i] ) ;
		}
		vm.push( val ) ;
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "store(%d)", count) ;
	}
}

class UnlockLocation extends Instruction {
	UnlockLocation(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Location loc = vm.popLocation() ;
		loc.setWritable( true );
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "unlockLocation()") ;
	}
}

class PopInstruction extends Instruction {
	
	private int count ; 
	
	PopInstruction(int n, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		count = n ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.pop( count );
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "pop()") ;
	}
}

class DuplicateInstruction extends Instruction {
	DuplicateInstruction(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		StackItem item = vm.pop();
		vm.push( item ); 
		vm.push( item ); 
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "duplicate()") ;
	}
}

class RotateUpInstruction extends Instruction {
	private int count ;
	
	RotateUpInstruction(int n, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		Assert.check( n > 0 );
		count = n ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		StackItem[] items = vm.pop( count ) ;
		vm.push( items[count-1] );
		for( int i= 0 ; i < count-1 ; ++i )
			vm.push( items[i] );
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "rotateUp(%d)", count) ;
	}
}

class RotateDownInstruction extends Instruction {
	private int count ;
	
	RotateDownInstruction(int n, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		Assert.check( n > 0 );
		count = n ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		StackItem[] items = vm.pop( count ) ;
		for( int i= 1 ; i < count ; ++i )
			vm.push( items[i] );
		vm.push( items[0] ) ;
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "rotateDown(%d)", count) ;
	}
}

class LookupInstruction extends Instruction {
	
	LookupInstruction(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value name = vm.popValue() ;
		Value val = vm.popValue() ;
		if( ! name.isString() )
			vm.error("Property name is not a string.") ;
		String nameString = ((StringValue) name).getString() ;
		Value result = val.lookup(nameString) ;
		if( result == null ) 
			vm.error("Value " +val+
					" does not have a property named " +nameString+ "." ) ;
		vm.push( result ) ;
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "lookup()" ) ;
	}
}

class ApplyInstruction extends Instruction {
	
	ApplyInstruction(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value arg = vm.popValue() ;
		Value f = vm.popValue() ;
		f.apply( arg, vm ) ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "apply()" ) ;
	}
}

class ReturnNowInstruction extends Instruction {
	
	ReturnNowInstruction(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.doReturn() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "returnNow()" ) ;
	}
}

class JumpInstruction extends Instruction {
	
	private int offset;

	JumpInstruction(int offset, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.offset = offset ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.tick( offset ) ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "jump( %d )", offset ) ;
	}
}

class JumpOnTrueInstruction extends Instruction {
	
	private int offset;

	JumpOnTrueInstruction(int offset, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.offset = offset ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value v = vm.popValue() ;
		if( ! v.isBool() ) 
			vm.error( "Expected boolean JumpOnTrue instruction." );
		BoolValue bv = (BoolValue) v ;
		if( bv.isTrue() ) {
			vm.tick( offset ) ;
		} else {
			vm.tick(1) ; }
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "jumpOnTrue( %d )", offset ) ;
	}
}

class JumpOnFalseInstruction extends Instruction {
	
	private int offset;

	JumpOnFalseInstruction(int offset, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.offset = offset ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Value v = vm.popValue() ;
		if( ! v.isBool() ) 
			vm.error( "Expected boolean JumpOnTrue instruction." );
		BoolValue bv = (BoolValue) v ;
		if( bv.isTrue() ) {
			vm.tick( 1 ) ;
		} else {
			vm.tick( offset ) ; }
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "jumpOnFalse( %d )", offset ) ;
	}
}

class LockLocation extends Instruction {
	LockLocation(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Location loc = vm.popLocation() ;
		loc.setWritable( false );
		vm.tick();
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "lockLocation()") ;
	}
}



class NewFrame extends Instruction {
	private int count ;
	
	NewFrame(int count, SourceCoords sourceCoords) {
		super(sourceCoords) ;
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		Frame topFrame = vm.getFrame() ;
		Frame frame = new Frame( topFrame ) ;
		String[] names = new String[ count ] ;
		Type[] types = new Type[ count ] ;
		for( int i = count-1 ; i >= 0; --i ) {
			types[i] = vm.popType() ;
			Value val = vm.popValue() ;
			if( ! val.isString() )
				vm.error("Expected a string for a variable name") ;
			names[i] = ((StringValue)val).getString() ; }
		
		for( int i = 0 ; i < count ; ++i ) {
			Location loc = new Location(names[i], types[i]) ;
			frame.addLocation( loc ) ; }
		vm.setFrame( frame ) ;
		vm.tick() ; 
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "newFrame(%d)", count) ;
	}
}


class PopFrame extends Instruction {
	
	PopFrame(SourceCoords sourceCoords) {
		super(sourceCoords) ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		vm.popFrame() ;
		vm.tick() ; 
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "popFrame()") ;
	}
}

class ConstructType extends Instruction {
	private String name ;
	private int count ;
	
	ConstructType(String name, int count, SourceCoords sourceCoords) {
		super(sourceCoords);
		this.name = name ;
		this.count = count ;
	}

	@Override
	void execute(PSAM vm) throws RunTimeError {
		TypeName tyName = TypeName.stringToName( name ) ;
		if( tyName == null )
			vm.error("No type named "+name+".") ;
		if( ! tyName.argCount(count) ) 
			vm.error("Type constructor " +name+
					 " can not have " +count+" argument(s).") ;
		Type[] children =  vm.popTypes( count ) ;
		Type type = tyName.makeType( children ) ;
		vm.push( type ) ;
		vm.tick() ;
	}

	@Override
	void show(Formatter fmt) {
		fmt.format( "constructType(%s, %d)", name, count) ;
	}
}
