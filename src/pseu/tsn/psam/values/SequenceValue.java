package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class SequenceValue extends CompositeValue {
	
	static SequenceValue theEmptySeq = new SequenceValue( new Value[0] ) ;
	
	public static SequenceValue make( Value[] values ) {
		if( values.length == 0 ) return theEmptySeq ;
		else return new SequenceValue(values) ;
	}
	
	@Override public boolean isSeq( ) { return true ; }
	
	SequenceValue( Value[] values ) {
		super( values ) ;
	}

	@Override
	public void show(Formatter fmt) {
		fmt.format( "[" ) ;
		super.show( fmt ) ;
		fmt.format( "]" ) ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		if( ! argument.isInt() )
			vm.error("Value " + toString() + " cannot be applied to " +argument+ ".") ;
		IntValue intVal = (IntValue) argument ;
		boolean inBounds = intVal.canConvertToInt() ;
		int index = inBounds ? intVal.toInt() : 0 ;
		inBounds = inBounds && index >= 0 ;
		inBounds = inBounds && index < this.size();
		if( ! inBounds ) 
			vm.error("Index out of bounds.") ;
		return this.get( index ) ;
	}
}