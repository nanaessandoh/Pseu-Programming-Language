package pseu.tsn.psam.values;

import java.math.BigInteger;
import java.util.Formatter;
import java.util.HashMap;
import java.util.function.Function;

import pseu.common.RunTimeError;
import pseu.common.SourceCoords;
import pseu.tsn.psam.PSAM;

public class IntValue extends Value {
	
	final BigInteger representation ;
	
	public static IntValue make( String str, PSAM vm )
	throws RunTimeError {
		try { return new IntValue( str ) ; }
		catch( NumberFormatException ex ) {
			vm.error( "Bad integer constant." );
			return null ;
		}
	}
	
	private IntValue( String str ) {
		representation = new BigInteger( str, 10) ;
	}
	
	IntValue( int val ) {
		String str = Integer.toString(val, 10) ;
		representation = new BigInteger(  str ) ;
	}
	
	IntValue( BigInteger rep ) {
		representation = rep ;
	}
	
	@Override public boolean isInt( ) { return true ; }

	@Override
	public void show(Formatter fmt) {
		fmt.format("%s", representation.toString(10) ) ;
	}
	
	public boolean canConvertToInt() {
		try { representation.intValueExact() ; }
		catch( ArithmeticException e ) { return false ; }
		return true ;
	}
	
	public int toInt() {
		return representation.intValueExact() ; 
	}
	
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		vm.error("Value " + toString() + " cannot be applied to " +argument+ "." ) ;
		return null ;
	}
	
	static HashMap<String, Function<IntValue, Value>>  map = new HashMap<String, Function<IntValue, Value>>() ;
	static {
		map.put("binary(=)", (IntValue v) -> ( new IntEqualsInt( v, "binary =" )  ) ) ;
		map.put("binary(/=)", (IntValue v) -> ( new IntNotEqualsInt( v, "binary /=" )  ) ) ;
		map.put("binary(!=)", (IntValue v) -> ( new IntNotEqualsInt( v, "binary !=" )  ) ) ;
		map.put("binary(+)", (IntValue v) -> ( new IntPlusInt( v, "binary +" )  ) ) ;
		map.put("binary(-)", (IntValue v) -> ( new IntMinusInt( v, "binary -" )  ) ) ;
		map.put("binary(*)", (IntValue v) -> ( new IntTimesInt( v, "binary *" )  ) ) ;
		map.put("binary(div)", (IntValue v) -> ( new IntDivInt( v, "binary div" )  ) ) ;
		map.put("binary(/)", (IntValue v) -> ( new IntDivInt( v, "binary /" )  ) ) ;
		map.put("binary(mod)", (IntValue v) -> ( new IntModInt( v, "binary mod" )  ) ) ;
		map.put("binary(<)", (IntValue v) -> ( new IntLessThanInt( v, "binary <" )  ) ) ;
		map.put("binary(_<)", (IntValue v) -> ( new IntLessOrEqualInt( v, "binary _<" )  ) ) ;
		map.put("binary(<=)", (IntValue v) -> ( new IntLessOrEqualInt( v, "binary <=" )  ) ) ;
		map.put("binary(\\le)", (IntValue v) -> ( new IntLessOrEqualInt( v, "binary \\le" )  ) ) ;
		map.put("binary(>)", (IntValue v) -> ( new IntGreaterThanInt( v, "binary >" )  ) ) ;
		map.put("binary(>_)", (IntValue v) -> ( new IntGreaterOrEqualInt( v, "binary >_" )  ) ) ;
		map.put("binary(>=)", (IntValue v) -> ( new IntGreaterOrEqualInt( v, "binary >=" )  ) ) ;
		map.put("binary(\\ge)", (IntValue v) -> ( new IntGreaterOrEqualInt( v, "binary \\ge" )  ) ) ;
		map.put("unary(-)", (IntValue v) -> ( new IntNegate( v, "binary \\ge" ) ) ) ;
	}


	@Override
	public Value lookup(String name) {
		Function<IntValue, Value> f = map.get(name) ;
		if( f == null )  return null ;
		return f.apply( this ) ;
	}
}

abstract class IntMethodValue extends BuiltInFunctionValue {
	
	String name ;
	IntValue recipient ;

	IntMethodValue(IntValue recipient, String name, Type argumentType, Type resultType) {
		super(argumentType, resultType);
		this.name = name ;
		this.recipient = recipient ;
	}

	@Override
	public void show(Formatter fmt) {
		recipient.show( fmt ) ;
		fmt.format(".%s", name ) ;
	}
	
	protected IntValue convertArgument( Value arg, PSAM vm)
	throws RunTimeError {
		if( arg.isInt() ) return (IntValue) arg ;
		else {
			vm.error("Int." + name + " expects Int value as right operand.");
			return null; }
	}
}

class IntPlusInt extends IntMethodValue {

	IntPlusInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		BigInteger bint = recipient.representation.add( arg.representation ) ;
		return new IntValue( bint ) ;
	}
}

class IntTimesInt extends IntMethodValue {

	IntTimesInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		BigInteger bint = recipient.representation.multiply( arg.representation ) ;
		return new IntValue( bint ) ;
	}
}

class IntMinusInt extends IntMethodValue {

	IntMinusInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		BigInteger bint = recipient.representation.subtract( arg.representation ) ;
		return new IntValue( bint ) ;
	}
}


class IntDivInt extends IntMethodValue {

	IntDivInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		BigInteger bint ;
		try {
			bint = recipient.representation.divide( arg.representation ) ;
		} catch( ArithmeticException err ) {
			vm.error( "Divide by zero.") ;
			bint = null ;
		}
		return new IntValue( bint ) ;
	}
}

class IntModInt extends IntMethodValue {

	IntModInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		BigInteger bint ;
		try {
			bint = recipient.representation.mod( arg.representation ) ;
		} catch( ArithmeticException err ) {
			vm.error( "Divide by zero.") ;
			bint = null ;
		}
		return new IntValue( bint ) ;
	}
}

class IntNegate extends IntMethodValue {

	IntNegate(IntValue recipient, String name) {
		super(recipient, name, TypeName.unitType, TypeName.intType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		// Todo.  Possibly check the argument?
		BigInteger bint = recipient.representation.negate( ) ;
		return new IntValue( bint ) ;
	}
}

class IntEqualsInt extends IntMethodValue {

	IntEqualsInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.anyType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		if( ! argument.isInt() ) return BoolValue.make(false) ;
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( recipient.representation.equals( arg.representation ) ) ;
	}
}

class IntNotEqualsInt extends IntMethodValue {

	IntNotEqualsInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.anyType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		if( ! argument.isInt() ) return BoolValue.make(true) ;
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( ! recipient.representation.equals( arg.representation ) ) ;
	}
}

class IntLessThanInt extends IntMethodValue {

	IntLessThanInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( recipient.representation.compareTo( arg.representation ) < 0 );
	}
}

class IntLessOrEqualInt extends IntMethodValue {

	IntLessOrEqualInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( recipient.representation.compareTo( arg.representation ) <= 0 ) ;
	}
}

class IntGreaterThanInt extends IntMethodValue {

	IntGreaterThanInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( recipient.representation.compareTo( arg.representation ) > 0 ) ;
	}
}

class IntGreaterOrEqualInt extends IntMethodValue {

	IntGreaterOrEqualInt(IntValue recipient, String name) {
		super(recipient, name, TypeName.intType, TypeName.boolType);
	}

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
			throws RunTimeError {
		IntValue arg = convertArgument( argument, vm ) ;
		return BoolValue.make( recipient.representation.compareTo( arg.representation ) >= 0 ) ;
	}
}