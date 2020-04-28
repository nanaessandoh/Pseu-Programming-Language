package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class TupleValue extends CompositeValue {
	
	static TupleValue theEmptyTuple = new TupleValue( new Value[0] ) ;
	
	public static TupleValue make( Value[] values ) {
		if( values.length == 0 ) return theEmptyTuple ;
		else return new TupleValue(values) ;
	}
	
	TupleValue( Value[] values ) {
		super( values ) ;
	}
	
	@Override public boolean isTuple( ) { return true ; }

	@Override
	public void show(Formatter fmt) {
		fmt.format( "(" ) ;
		super.show( fmt ) ;
		fmt.format( ")" ) ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		vm.error("Value " + toString() + " cannot be applied to " +argument+ "." ) ;
		return null ;
	}
}