package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.PSAM;
import pseu.tsn.psam.StackItem; 

public abstract class Value implements StackItem {
	
	protected Value( ) {
	}
	
	abstract public void show( Formatter fmt ) ;
	
	@Override public String toString( ) {
		Formatter fmt = new Formatter() ;
		show( fmt ) ;
		return fmt.toString() ;
	}
	
	abstract protected Value valueOfApply( Value argument, PSAM vm )
	throws RunTimeError ;
	
	public void apply( Value argument, PSAM vm )
	throws RunTimeError {
		Value val = valueOfApply( argument, vm ) ;
		vm.push( val ) ;
		vm.tick() ;
	}
	
	public abstract Value lookup( String name ) ;
	
	public boolean isInt( ) {return false ; }
	public boolean isBool( ) {return false ; }
	public boolean isString( ) {return false ; }
	public boolean isTuple( ) {return false ; }
	public boolean isProduct( ) {return false ; }
	public boolean isFunction( ) {return false ; }
	public boolean isClosure( ) {return false ; }
	public boolean isSet( ) {return false ; }
	public boolean isSeq ( ) {return false ; }
}
