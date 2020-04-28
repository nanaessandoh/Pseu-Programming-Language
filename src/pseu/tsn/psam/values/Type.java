package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.Assert;
import pseu.tsn.psam.StackItem;

public class Type implements StackItem {
	
	Type children[] ;
	
	TypeName name ;
	
	public static Type make( TypeName name, Type ...children) {
		return new Type( name, children ) ;
	}
	
	Type( TypeName name, Type[] children ) {
		Assert.check( name.argCount( children.length ) );
		this.name = name ;
		this.children = children ;
	}

	@Override
	public void show(Formatter fmt) {
		fmt.format("%s", name) ;
		if( children.length > 0 ) {
			fmt.format("[") ;
			for( int i=0 ; i < children.length ; ++i ) {
				children[i].show(fmt) ;
				if( i < children.length-1 )
					fmt.format(", ") ;
			}
			fmt.format("]") ;
		}
	}
	
	@Override public String toString( ) {
		Formatter fmt = new Formatter() ;
		show( fmt ) ;
		return fmt.toString() ;
	}
	
	public boolean containsValue( Value v ) {
		return name.containsValue( this, v ) ;
	}
	
	public boolean isSubtypeOf( Type that ) {
		return this.name.isSuptypeOf( this, that) ;
	}
}
