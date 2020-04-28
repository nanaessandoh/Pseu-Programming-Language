package pseu.tsn.psam.values;

import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.PSAM;

public class StringValue extends Value {
	
	private String representation ;
	
	public static StringValue make( String str ) {
		return new StringValue( str ) ;
	}
	
	StringValue( String str ) {
		representation = str;
	}
	
	@Override public boolean isString( ) { return true ; }
	
	public String getString( ) { return representation ; }

	@Override
	public void show(Formatter fmt) {
		// TODO Add escapes
		fmt.format("\"%s\"", addEscapes(representation) ) ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		vm.error("Value " + toString() + " cannot be applied to " +argument+ "." ) ;
		return null ;
	}

	@Override
	public Value lookup(String name) {
		// TODO add some properties for strings.
		return null;
	}
	
	private static String addEscapes( String str ) {
        // See JLS 3.10.4-6
        StringBuffer result = new StringBuffer() ;
        for( int i=0, len = str.length() ; i<len ; ++i ) {
            char ch = str.charAt(i) ;
            switch( ch ) {
                case '\b' : result.append("\\b") ; break ;
                case '\t' : result.append("\\t") ; break ;
                case '\n' : result.append("\\n") ; break ;
                case '\f' : result.append("\\f") ; break ;
                case '\r' : result.append("\\r") ; break ;
                case '"' : result.append("\\\"") ; break ;
                case '\\' : result.append("\\\\") ; break ;
                default: // The parser should have picked up illegal escapes already
                	if(  ' ' <= ch && ch <= '~' ) { result.append(ch) ; }
                	else { result.append("\\u") ; result.append(String.format("%04X", (int)ch) ) ; }
            }
        }
        return result.toString() ;
    }
}