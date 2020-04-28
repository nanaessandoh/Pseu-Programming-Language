package pseu.parser;

import pseu.common.SourceCoords ;

public interface ASTBuilderI {
	
	/** Precondition i will consist only of digits */
	void mkIntConstant( String i, SourceCoords coords ) ;
	
	void mkStringConstant( String str, SourceCoords coords ) ;
	
	void mkTrue(SourceCoords coords) ;
	
	void mkFalse(SourceCoords coords) ;
	
	/** mkVariable is used when an
	 * an identifier is used as a variable
	 * reference. 
	 * For example
	 *   "a := b"
	 * results in the following sequence of calls
	 *    mkVariable( "a", ...) <-----
	 *    mkVariable("b", ... ) <-----
	 *    mkFetch() 
	 *    mkAssignment(1, ... ) 
	 */
	void mkVariable(String name, SourceCoords coords) ;
	
	/** mkFetch is used when an
	 * a variable reference
	 * is used where an expression is expected.
	 * For example
	 *   "a := b"
	 * results in the following sequence of calls
	 *    mkVariable( "a", ...) <--- No fetch here
	 *    mkVariable("b", ... )
	 *    mkFetch() <--------
	 *    mkAssignment(1, ... ) 
	 */
	void mkFetch() ;
	
	/** mkSet is invoked after the expressions
	 * have been built
	 * 
	 * So for example if the expression is "{X,Y,Z}",
	 * the sequence of calls will be
	 *    calls to build X
	 *    calls to build Y
	 *    calls to build Z
	 *    mkSet(3, ...)
	 */
	void mkSet(int count, SourceCoords coords) ;
	
	/** mkSequence is invoked after the expressions
	 * have been built
	 * 
	 * So for example if the expression is "[X,Y,Z]",
	 * the sequence of calls will be
	 *    calls to build X
	 *    calls to build Y
	 *    calls to build Z
	 *    mkSequence(3, ...)
	 */
	void mkSequence(int count, SourceCoords coords) ;
	
	/** mkTuple is invoked only after
	 * the expressions have been built.
	 * So for example if the code is "(X,Y,Z)",
	 * the sequence of calls will be
	 *    calls to build X
	 *    calls to build Y
	 *    calls to build Z
	 *    mkTuple(3, ...)
	 * The count will be 0, 2, or more, but never 1
	 */
	void mkTuple(int count, SourceCoords coords) ;
	
	/** Lookup a name from an value
	 *  
	 * For example, if the code is "X.m",
	 * the sequence of calls will be
	 * 
	 *    calls to for expression X
	 *    mkLookup( "m", ... )
	 *    
	 * 
	 */
	void mkLookup(String name, SourceCoords coords) ;
	
	/** Apply a function.
	 *
	 * For an application F(E) the pattern of calls
	 * is
	 *      calls for F
	 *      calls for R
	 *      mkApplication( ... )
	 * 
	 * When there are more or less than one
	 * argument, the parser will treat the
	 * application as if there is one argument,
	 * so the pattern above is the only one
	 * to worry about.
	 *    
	 * In particular: 
	 * 
	 * (a) When there are no argument,
	 * the sequence of calls will be as if the
	 * there was one argument that is an empty tuple.
	 * E.g., the parser will treat X() as if it
	 * were X(()).
	 *    calls to build X
	 *    mkTuple(0, ...)
	 *    mkApplication( ... )
	 *    
	 * (b) When there are multiple arguments
	 * the parser will treat the application
	 * as if there were one argument that is
	 * a tuple expression.  E.g. X(Y,Z) is
	 * treated as if were X((Y,Z)) so
	 *    calls to build expression X
	 *    calls to build expression Y
	 *    calls to build expression Z
	 *    mkTuple(2, ...)
	 *    mkApplication( ...)
	 */
	void mkApplication(SourceCoords coords) ;
	
	/** Function expressions.
	 * An expression fun (a:T, b:U) : V do ... end fun
	 * results in a sequence of calls as follows
	 *     mkVariable("a", ... )
	 *     calls for type T
	 *     mkVariable("b", ... )
	 *     calls for type U
	 *     calls for type V
	 *     startBlock()
	 *     calls for the statements and declarations within the block
	 *     endBlock()
	 *     functionExp( 2, true ... )
	 *     
	 * An expression fun () do ... end fun
	 * results in a sequence of calls as follows
	 *     startBlock()
	 *     calls for the statements and declarations within the block
	 *     endBlock()
	 *     functionExp( 0, false ... )
	 * 
	 * @param count  The number of parameters. Never negative.
	 * @param isTyped True if there is a result type given.
	 * @param coords The coordinates for the whole expression.
	 */
	void mkFunctionExp(int count, boolean isTyped, SourceCoords coords) ;
	
	/** Assignment command.
	 * The general pattern is 
	 *    one or more mkVariable calls
	 *    calls for an expression
	 *    mkAssignment( count )
	 * where count is the number of variables.
	 *    
	 * For example "a:=E" results in 
	 *    mkVariable( "a" )
	 *    calls for E
	 *    mkAssignment(1, ... ) ;
	 *    
	 * For example an assignment a,b,c := E,F,G results in
	 *    mkVariable( "a" )
	 *    mkVariable( "b" )
	 *    mkVariable( "c" )
	 *    calls for E
	 *    calls for F
	 *    calls for G
	 *    mkTuple(3)
	 *    mkAssignment( 3, ... ) ;
	 *    
	 * For example an assignment a := E,F,G results in
	 *    mkVariable( "a" )
	 *    calls for E
	 *    calls for F
	 *    calls for G
	 *    mkTuple(3)
	 *    mkAssignment( 1, ... ) ;
	 *    
	 */
	void mkAssignment(int count, SourceCoords coords) ;
	
	
	/** The sequence for "if X then B else C end if" 
	 * is
	 *     
	 *     calls for X
	 *     calls for block B
	 *     calls for block C
	 *     mkIf(...)
	 */
	void mkIf(SourceCoords coords) ;
	
	/** The sequence for "while X do B end while" 
	 * is
	 *     
	 *     calls for X
	 *     calls for block B
	 *     mkWhile()
	 */
	void mkWhile(SourceCoords coords) ;
	
	/** The sequence for "for a <- X do B end for" 
	 * is
	 *     
	 *     mkVariable( "a" )
	 *     calls for X
	 *     calls for block B
	 *     mkFor(...)
	 */
	void mkFor(SourceCoords coords) ;
	
	/** The sequence for "return X" 
	 * is
	 *     
	 *     calls for X
	 *     mkReturn( ... )
	 * 
	 */
	void mkReturn(SourceCoords coords) ;
	
	/** mkExpressionCommand is used when a command
	 * consists of an expression.
	 * The main use for this is when a message
	 * send is used as a command. For example if
	 *    f.apply( X )
	 * appears in a block, the sequence of calls will
	 * be
	 *     calls for representing the expression f.apply(X)
	 *     mkExpressionCommand
	 *
	 */
	void mkExpressionCommand() ;
	
	/** val declaration
	 * Declares a named value. This is essentially
	 * a variable that can not be altered.
	 * 
	 * A val declaration such as val a : T := X
	 * would be coded as
	 *     mkVariable( a )
	 *     calls for building type T
	 *     calls for building expression X
	 *     valDeclaration( true, ... )
	 *     
	 * A val declaration such as val a := X
	 * would be coded as
	 *     mkVariable( a )
	 *     calls for building expression X
	 *     valDeclaration( false, ... )
	 */
	void valDeclaration(boolean isTyped, SourceCoords coords) ;
	
	/** var declaration
	 * Declares a variable
	 * 
	 * A var declaration such as var a : T := X
	 * would be coded as
	 *     mkVariable( a )
	 *     calls for building type T
	 *     calls for building type X
	 *     varDeclaration( true ... )
	 * 
	 * A var declaration such as var a : T 
	 *     mkVariable( a ) 
	 *     calls for building type T
	 *     varDeclaration( false ... )
	 */
	void varDeclaration(boolean isInitialized, SourceCoords coords) ;
	
	/** When an identifier is used as a type, 
	 * the sequence of calls will be
	 *    mkIdentifierType( ... )
	 */
	void mkIdentifierType(String name, SourceCoords coords) ;
	
	/** Type application.
	 * A type application such as Foo[T,U]
	 * will result in this sequence
	 *    calls for T
	 *    calls for U
	 *    mkTypeApplication("Foo", 2)
	 * 
	 *<p>
	 * Product types and function types are also represented using this call.
	 * E.g. T * U * V results in 
	 *    calls for T
	 *    calls for U
	 *    calls for V
	 *    mkTypeApplication("Product", 3)
	 * <p>
	 * And T -> U results in 
	 *    calls for T
	 *    calls for U
	 *    mkTypeApplication("Fun", 2 )
	 * <p>
	 * Precondition: count will be 1 or more.
	 */
	void mkTypeApplication(String name, int count, SourceCoords coords) ;
		
	/** The sequence of calls for any block will start 
	 * with startBlock() and end with endBlock()
	 * For example the empty block will cause a
	 * sequence of calls
	 *     startBlock()
	 *     endBlock()
	 * A block with three commands or declarations A B C
	 * will look like this
	 *     
	 *     startBlock()
	 *     calls for A
	 *     calls for B
	 *     calls for C
	 *     endBlock(3, ...)
	 *     
	 * An empty block will look like this
	 *     startBlock()
	 *     endBlock(0, ...)
	 */
	void startBlock() ;
	void endBlock(int count, SourceCoords coords ) ;
}
