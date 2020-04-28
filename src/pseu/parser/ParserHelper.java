package pseu.parser;


import pseu.common.Assert ;

class ParserHelper {
	static String unescapeString( String str ) {
        // See JLS 3.10.4-6
        StringBuffer result = new StringBuffer() ;
        for( int i=0, len = str.length() ; i<len ; ) {
            char ch = str.charAt(i) ;

            // Set ch and increment i ;
            if( ch == '\\' )  {
                ch = str.charAt(i+1) ;
                switch( ch ) {
                    case 'b' : ch = '\b' ; i += 2 ; break ;
                    case 't' : ch = '\t' ; i += 2 ; break ;
                    case 'n' : ch = '\n' ; i += 2 ; break ;
                    case 'f' : ch = '\f' ; i += 2 ; break ;
                    case 'r' : ch = '\r' ; i += 2 ; break ;
                    case '"' : case '\'' : case '\\' : i+= 2 ; break ;
                    case '0' : case '1' : case '2' : case '3' :
                    case '4' : case '5' : case '6' : case '7' : {
                        // Octal escapes \a, \ab, \abc
                        int oct0 = (int)ch - (int)'0' ;
                        char ch1 = (char)0 ;
                        char ch2= (char)0 ;
                        if( i+2 < len ) {
                            ch1 = str.charAt(i+2) ;
                            if( i+3 < len ) {
                                ch2 = str.charAt(i+3) ; } }
                        if( '0' <= ch && ch <= '3'
                         && '0' <= ch1 && ch1 <= '7'
                         && '0' <= ch2 && ch2 <= '7' ) {
                            // Length is 4 \acb
                            int oct1 = (int)ch1 - (int)'0' ;
                            int oct2 = (int)ch2 - (int)'0' ;
                            ch = (char)( 64* oct0 + 8*oct1 + oct2 );
                            i += 4 ; }
                        else if( '0' <= ch1 && ch1 <= '7' ) {
                            // Length is 3 \ab
                            int oct1 = (int)ch1 - (int)'0' ;
                            ch = (char)( 8*oct0 + oct1 ) ;
                            i += 3 ;}
                        else  {
                            // Length is 2 \a
                            ch = (char)( oct0 ) ;
                            i += 2 ; } }
                    break ;
                    default: // The parser should have picked up illegal escapes already
                        Assert.unreachable() ; } }
            else {
                i += 1 ; }
            result.append( ch ) ; }
        return result.toString() ;
    }
	
	static String cleanUpInt( String str ) {
		StringBuffer result = new StringBuffer() ;
        for( int i=0, len = str.length() ; i<len ; ++i ) {
            char ch = str.charAt(i) ;
			if( ch != '_' ) result.append( ch ) ; }
		return result.toString() ;
	}

}
