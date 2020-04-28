package pseu.interpreter;

import pseu.parser.ASTBuilderI;

public interface ASTSourceI<AST>  extends ASTBuilderI {

	/** Get the AST.
	 * <p> Precondition: This method should only be called when the whole tree has been built.
	 * @return The tree that has been built.
	 */
	AST getAST() ;

}
