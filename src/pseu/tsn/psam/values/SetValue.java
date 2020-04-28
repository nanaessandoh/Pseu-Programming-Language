package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class SetValue extends CompositeValue {
	
	static SetValue theEmptySet = new SetValue( new Value[0] ) ;
	
	public static SetValue make( Value[] values ) {
		// Precondition. All values are unequal.
		// TODO Make sure this precondition
		// is respected everywhere.
		if( values.length == 0 ) return theEmptySet ;
		else return new SetValue(values) ;
	}
	
	SetValue( Value[] values ) {
		super( values ) ;
	}

	@Override public boolean isSet( ) { return true ; }
	
	@Override
	public void show(Formatter fmt) {
		fmt.format( "{" ) ;
		super.show( fmt ) ;
		fmt.format( "}" ) ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		vm.error("Value " + toString() + " cannot be applied to " +argument+ "." ) ;
		return null ;
	}
}