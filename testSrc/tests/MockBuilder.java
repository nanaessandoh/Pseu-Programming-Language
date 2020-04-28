package tests;

import java.util.Formatter ;


import pseu.common.SourceCoords;
import pseu.interpreter.ASTSourceI;
import pseu.parser.ASTBuilderI;

public class MockBuilder implements ASTSourceI<MockAST> {
	
	Formatter fmt = new Formatter(java.util.Locale.US);

	@Override
	public String toString() {
		return fmt.toString() ;
	}

	@Override
	public void mkIntConstant(String i, SourceCoords coords) {
		fmt.format( "mkIntConstant(%s, %s)\n", i, coords ) ;
	}

	@Override
	public void mkStringConstant(String str, SourceCoords coords) {
		fmt.format( "mkStringConstant(%s, %s)\n", str, coords ) ;
	}

	@Override
	public void mkTrue(SourceCoords coords) {
		fmt.format( "mkTrue(%s)\n", coords ) ;
	}

	@Override
	public void mkFalse(SourceCoords coords) {
		fmt.format( "mkFalse(%s)\n", coords ) ;
	}

	@Override
	public void mkVariable(String name, SourceCoords coords) {
		fmt.format( "mkVariable(%s, %s)\n", name, coords ) ;
	}

	@Override
	public void mkFetch() {
		fmt.format( "mkFetch()\n" ) ;
	}
	
	@Override
	public void mkFunctionExp(int count, boolean isTyped, SourceCoords coords) {
		fmt.format( "mkFunctionExp(%d, %b, %s)\n", count, isTyped, coords ) ;
	}

	@Override
	public void mkSet(int count, SourceCoords coords) {
		fmt.format( "mkSet(%d, %s)\n", count, coords ) ;
	}

	@Override
	public void mkSequence(int count, SourceCoords coords) {
		fmt.format( "mkSequence(%d, %s)\n", count, coords ) ;
	}

	@Override
	public void mkTuple(int count, SourceCoords coords) {
		fmt.format( "mkTuple(%d, %s)\n", count, coords ) ;
	}
	
	@Override
	public void mkLookup(String name, SourceCoords coords) {
		fmt.format( "mkLookup(%s, %s)\n", name, coords ) ;
	}
	
	@Override
	public void mkApplication( SourceCoords coords ) {
		fmt.format( "mkApplication(%s)\n", coords ) ;
	}
	
	@Override
	public void mkAssignment(int count, SourceCoords coords) {
		fmt.format( "mkAssignment(%d, %s)\n", count, coords ) ;

	}

	@Override
	public void mkIf(SourceCoords coords) {
		fmt.format( "mkIf(%s)\n", coords ) ;
	}

	@Override
	public void mkWhile(SourceCoords coords) {
		fmt.format( "mkWhile(%s)\n", coords ) ;
	}

	@Override
	public void mkFor(SourceCoords coords) {
		fmt.format( "mkFor(%s)\n", coords ) ;
	}

	@Override
	public void mkReturn(SourceCoords coords) {
		fmt.format( "mkReturn(%s)\n", coords ) ;

	}

	@Override
	public void mkExpressionCommand() {
		fmt.format( "mkExpressionCommand()\n" ) ;
	}

	@Override
	public void valDeclaration(boolean isTyped, SourceCoords coords) {
		fmt.format( "valDeclaration(%b, %s)\n", isTyped, coords ) ;
	}

	@Override
	public void varDeclaration(boolean isInitialized, SourceCoords coords) {
		fmt.format( "varDeclaration(%b, %s)\n", isInitialized, coords ) ;
	}

	@Override
	public void mkIdentifierType(String name, SourceCoords coords) {
		fmt.format( "mkIdentifierType(%s, %s)\n", name, coords ) ;

	}

	@Override
	public void mkTypeApplication(String name, int count, SourceCoords coords) {
		fmt.format( "mkTypeApplication(%s, %d, %s)\n", name, count, coords ) ;
	}

	@Override
	public void startBlock() {
		fmt.format( "startBlock()\n") ;
	}

	@Override
	public void endBlock(int count, SourceCoords coords) {
		fmt.format( "endBlock(%d, %s)\n", count, coords ) ;

	}

	@Override
	public MockAST getAST() {
		return new MockAST() ;
	}

}
