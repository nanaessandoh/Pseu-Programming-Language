package pseu.tsn.psam.values;

public abstract class FunctionValue extends Value {

	final protected Type argumentType ;
	final protected Type resultType ;
	
	FunctionValue( Type argumentType, Type resultType ) {
		this.argumentType = argumentType ;
		this.resultType = resultType ;
	}
	
	@Override public boolean isFunction() {
		return true ;
	}
}
