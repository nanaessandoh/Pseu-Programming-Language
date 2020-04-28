package pseu.common;

public class Assert {
    
    /** Check an assertion
     * 
     * @param proposition  The assertion that ought to be true.
     */
    public static void check(boolean proposition ) {
        if( ! proposition ) fail( "" ) ;	}

    /** Check an assertion
     * 
     * @param proposition  The assertion that ought to be true.
     * @param message Explanation.
     */
    public static void check(boolean proposition, String message ) {
        if( ! proposition ) fail( message ) ; }
    
    /** Mark code that should not be reached.
     */
    public static void unreachable(  ) {
        fail( "Unreachable code reached!") ;	}

    /** Throw an AssertionError
     */
    public static void fail( String message ) {
        throw new AssertionError("Assertion failed: "+message);	}

    /** Throw an AssertionError with a nested exception.
     * Use this to mark exceptions that should not be caught.
     * @param e
     */
    public static void fail(Throwable e ) {
        throw new AssertionError(e) ;  }
    
    /** Mark a place where there is work to be done.
     * 
     * @param message What needs to be done.
     */
    public static void toBeDone( String message ) {
        fail( "To be done: " + message)  ; }

}
