package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.Assert;
import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.PSAM;
import pseu.tsn.psam.locations.Frame;
import pseu.tsn.psam.locations.Location;

public class ClosureValue extends FunctionValue {

	final String[] paramNames ;
	final Type[] paramTypes ;
	final int seg;
	final Frame frame;
	private SourceCoords coords;

	public ClosureValue(
			Type argType,
			Type resultType,
			String[] paramNames,
			Type[] paramTypes,
			int seg,
			Frame frame,
			SourceCoords coords ) {
		super( argType, resultType ) ;
		this.paramNames = paramNames ;
		this.paramTypes = paramTypes ;
		this.seg = seg ;
		this.frame = frame ;
		this.coords = coords ;
	}
	
	@Override
	public void show(Formatter fmt) {
		fmt.format( "function defined at %s : %s -> %s",
				    coords.toString(),
				    argumentType.toString(),
				    resultType.toString() ) ;

	}
	
	@Override public boolean isClosure() { return true ; }

	@Override
	public Value lookup(String name) {
		return null ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		Assert.check( false ) ;
		return null ;
	}
	
	
	@Override public void apply( Value argument, PSAM vm )
	throws RunTimeError {
		// Type check the argument
		if( ! argumentType.containsValue( argument ) ) {
			// TODO Better error messages to pinpoint the
			// problem.
			vm.error( String.format("Argument %s is not of type %s.",
					argument, argumentType ) ) ;
		}
		Frame paramFrame = new Frame( frame ) ;
		if( paramNames.length == 0 ) {
			// Nothing to do
		} else if( paramNames.length == 1 ) {
			Location loc = new Location( paramNames[0], paramTypes[0] ) ;
			paramFrame.addLocation( loc ) ;
			loc.setWritable( true ) ;
			loc.setValue( argument ) ;
			loc.setWritable( false ) ;
		} else {
			Assert.check( argument.isTuple() ) ;
			TupleValue argTuple = (TupleValue) argument ;
			for( int i = 0 ; i < paramNames.length ; ++i ) {
				Location loc = new Location(paramNames[i], paramTypes[i]) ;
				paramFrame.addLocation(loc);
				loc.setWritable( true ) ;
				loc.setValue( argTuple.get(i) ) ;
				loc.setWritable( false ) ; } }
		vm.call( seg, paramFrame, resultType) ; 
	}

}
