package pseu.tsn.psam.values;


import pseu.common.RunTimeError;
import pseu.common.SourceCoords;

public abstract class BuiltInFunctionValue extends FunctionValue {

	BuiltInFunctionValue(Type argumentType, Type resultType) {
		super(argumentType, resultType);
	}

	/** Default behaviour: No properties. */
	@Override
	public Value lookup(String name) {
		return null;
	}
}