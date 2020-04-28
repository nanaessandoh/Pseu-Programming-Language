package pseu.tsn.psam.locations;

import java.util.Formatter;

import pseu.tsn.psam.StackItem;
import pseu.tsn.psam.values.Type ;
import pseu.tsn.psam.values.Value;

public class Location implements StackItem {
	private String name;
	private Type type ;
	private boolean writable = false ;
	private boolean readable = false ;
	private Value value = null ;
	
	public Location( String name, Type type ) {
		this.name = name ;
		this.type = type ;
	}
	
	@Override
	public void show(Formatter fmt) {
		fmt.format("Loc[name=%s, writable=%b, readable=%b, value=",
				name, writable, readable ) ;
		if( value == null ) fmt.format("none") ;
		else value.show( fmt ) ; 
		fmt.format( "]" ) ;
	}
	
	@Override public String toString( ) {
		Formatter fmt = new Formatter() ;
		show( fmt ) ;
		return fmt.toString() ;
	}
	
	public Type getType() { return type ; }
	
	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isReadable() {
		return readable;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
		this.readable = true ;
	}

	public String getName() {
		return name;
	}
}
