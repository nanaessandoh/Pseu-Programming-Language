package pseu.tsn.psam.values;

import java.util.Formatter;
import java.util.HashMap;
import java.util.function.Function;

import pseu.common.RunTimeError;
import pseu.common.SourceCoords;

public abstract class CompositeValue extends Value {
	
	final Value[] representation ;
	
	CompositeValue( Value[] values ) {
		representation = values ;
	}
	
	public int size() { return representation.length ; }
	
	public Value get( int i) { return representation[i] ; }

	@Override
	public void show(Formatter fmt) {
		for( int i=0, sz=representation.length ; i < sz ; ++i ) {
			representation[i].show( fmt );
			if( i < sz-1 ) fmt.format(", ") ; }
	}
	
	static HashMap<String, Function<CompositeValue, Value>>  map
	    = new HashMap<String, Function<CompositeValue, Value>>() ;
	static {
		map.put("length", (CompositeValue v) -> ( new IntValue( v.size() )  ) ) ;
		map.put("size", map.get("length" ) ) ;
	}


	@Override
	public Value lookup(String name) {
		Function<CompositeValue, Value> f = map.get(name) ;
		if( f == null )  return null ;
		return f.apply( this ) ;
	}
}