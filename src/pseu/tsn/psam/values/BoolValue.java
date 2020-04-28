package pseu.tsn.psam.values;

import java.math.BigInteger;
import java.util.Formatter;
import java.util.HashMap;
import java.util.function.Function;

import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.PSAM;

public class BoolValue extends Value {
	
	final boolean representation ;
	
	private static BoolValue trueValue = new BoolValue( true ) ;
	private static BoolValue falseValue = new BoolValue( false ) ;
	
	public static BoolValue make( boolean value ) {
		return value ? trueValue : falseValue ;
	}
	
	BoolValue( boolean value ) {
		representation = value ;
	}
	
	public boolean isTrue() { return representation ; }
	
	@Override public boolean isBool( ) { return true ; }

	@Override
	public void show(Formatter fmt) {
		fmt.format("%b", representation ) ;
	}
	
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		vm.error("Value " + toString() + " cannot be applied to " +argument+ "." ) ;
		return null ;
	}
	
	static HashMap<String, Function<BoolValue, Value>>  map = new HashMap<String, Function<BoolValue, Value>>() ;
	static {

		map.put("binary(=)", (BoolValue v) -> ( new BooleanEqualMethodValue( v, "binary =" )  ) ) ;
		map.put("binary(!=)", (BoolValue v) -> ( new BooleanNotEqualMethodValue( v, "binary !=" )  ) ) ;
		map.put("binary(/=)", (BoolValue v) -> ( new BooleanNotEqualMethodValue( v, "binary /=" )  ) ) ;
		map.put("binary(implies)", (BoolValue v) -> ( new ImplicationMethodValue( v, "binary implies" )  ) ) ;
		map.put("binary(==>)", (BoolValue v) -> ( new ImplicationMethodValue( v, "binary ==>" )  ) ) ;
		map.put("binary(and)", (BoolValue v) -> ( new AndMethodValue( v, "binary and" )  ) ) ;
		map.put("binary(or)", (BoolValue v) -> ( new OrMethodValue( v, "binary or" )  ) ) ;
		map.put("unary(not)", (BoolValue v) -> ( new NotMethodValue( v, "unary not" )  ) ) ;
		
	}

	@Override
	public Value lookup(String name) {
		Function<BoolValue, Value> f = map.get(name) ;
		if( f == null )  return null ;
		return f.apply( this ) ;
	}
}


abstract class BoolMethodValue extends BuiltInFunctionValue {
	
	String name ;
	BoolValue recipient ;

	BoolMethodValue(BoolValue recipient, String name, Type argumentType, Type resultType) {
		super(argumentType, resultType);
		this.name = name ;
		this.recipient = recipient ;
	}

	@Override
	public void show(Formatter fmt) {
		recipient.show( fmt ) ;
		fmt.format(".%s", name ) ;
	}
	
	protected BoolValue convertArgument( Value arg, PSAM vm)
	throws RunTimeError {
		if( arg.isBool() ) return (BoolValue) arg ;
		else {
			vm.error("Bool." + name + " expects Bool value as right operand.");
			return null; }
	}
}

abstract class BoolToBoolMethodValue extends BoolMethodValue {

	BoolToBoolMethodValue(BoolValue recipient, String name) {
		super(recipient, name, TypeName.boolType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		BoolValue arg = convertArgument( argument, vm ) ;
		boolean result = operation( recipient.representation, arg.representation ) ;
		return BoolValue.make( result ) ;
	}

	protected abstract boolean operation(boolean x, boolean y) ;
}

class ImplicationMethodValue extends BoolToBoolMethodValue {
	
	ImplicationMethodValue(BoolValue recipient, String name) {
		super(recipient, name); }
	
	protected boolean operation(boolean x, boolean y) {
		return !x || y ; }
}

class AndMethodValue extends BoolToBoolMethodValue {
	
	AndMethodValue(BoolValue recipient, String name) {
		super(recipient, name); }
	
	protected boolean operation(boolean x, boolean y) {
		return x && y ; }
}

class OrMethodValue extends BoolToBoolMethodValue {
	
	OrMethodValue(BoolValue recipient, String name) {
		super(recipient, name); }
	
	protected boolean operation(boolean x, boolean y) {
		return x || y ; }
}

class EquivMethodValue extends BoolToBoolMethodValue {
	
	EquivMethodValue(BoolValue recipient, String name) {
		super(recipient, name); }
	
	protected boolean operation(boolean x, boolean y) {
		return x == y ; }
}

class NotMethodValue extends BoolMethodValue {
	
	NotMethodValue(BoolValue recipient, String name) {
		super(recipient, name, TypeName.unitType, TypeName.boolType); }

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		// TODO Possibly check the argument
		return BoolValue.make( !recipient.representation ) ; }
}

class BooleanEqualMethodValue extends BoolMethodValue {
	
	BooleanEqualMethodValue(BoolValue recipient, String name) {
		super(recipient, name, TypeName.anyType, TypeName.boolType); }

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		if( ! argument.isBool() ) return BoolValue.make( false )  ;
		BoolValue arg = convertArgument( argument, vm ) ;
		boolean result = recipient.representation == arg.representation ;
		return BoolValue.make( result ) ; }
}

class BooleanNotEqualMethodValue extends BoolMethodValue {
	
	BooleanNotEqualMethodValue(BoolValue recipient, String name) {
		super(recipient, name, TypeName.anyType, TypeName.boolType); }

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		if( ! argument.isBool() ) return BoolValue.make( true )  ;
		BoolValue arg = convertArgument( argument, vm ) ;
		boolean result = recipient.representation != arg.representation ;
		return BoolValue.make( result ) ; }
}