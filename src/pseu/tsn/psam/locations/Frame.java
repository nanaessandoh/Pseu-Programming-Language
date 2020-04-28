package pseu.tsn.psam.locations;

import java.util.ArrayList;
import java.util.Formatter;

import pseu.common.Assert;

public class Frame {
	
	private final ArrayList<Location> locations ;
	private final Frame[] display ;
	
	public Frame( Frame parent ) {
		locations = new ArrayList<Location>(0) ;
		if( parent == null) {
			display = new Frame[1] ;
			display[0] = this ;
		} else {
			display = new Frame[ 1+parent.display.length ] ;
			display[0] = this ;
			for( int i=1 ; i < display.length ; ++i ) 
				display[i] = parent.display[i-1] ;
		}
	}
	
	public boolean hasParent(  ) {
		return display.length > 1 ;
	}
	
	public Frame getParent(  ) {
		return display[1] ;
	}
	
	public void addLocation( Location loc ) {
		locations.add( loc ) ;
	}
	
	public boolean hasLocation( int depth, int address ) {
		if( 0 <= depth && depth < display.length ) {
			ArrayList<Location> list = display[depth].locations ;
			return 0 <= address && address < list.size() ;
		} else return false ;
	}
	
	/** Will return null on bad location */
	public Location getLocation( int depth, int address ) {
		if( 0 <= depth && depth < display.length ) {
			ArrayList<Location> list = display[depth].locations ;
			if(  0 <= address && address < list.size()  )
				return list.get( address ) ;
			else
				return null ;
		} else {
			return null ; }
	}
	
	/** Show all locations */
	@Override
	public String toString() {
		Formatter fmt = new Formatter() ;
		for( int f=0 ; f<display.length ; ++f) {
			fmt.format("Frame %d\n", f) ;
			for( int i = 0 ; i < display[f].locations.size() ; ++i ) {
				fmt.format( "   (%d,%d): ", f, i ) ;
				display[f].locations.get(i).show( fmt ) ; 
				fmt.format( "\n" ) ; } }
		return fmt.toString() ;
	}

}
