package pseu.tsn.psam.values;

import java.io.PrintWriter;
import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class PrintBuiltInFunction extends BuiltInFunctionValue {

	public PrintBuiltInFunction() {
		super(TypeName.anyType, TypeName.unitType );
	}

	@Override
	public void show(Formatter fmt) {
		fmt.format("print : Any -> Unit") ;
	}
	

	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
	throws RunTimeError {
		PrintWriter pw = vm.getOutputTarget();
		if( argument.isString() ) {
			StringValue arg = (StringValue) argument ;
			pw.append( arg.getString() ) ;
		} else {
			Formatter fmt = new Formatter() ;
			argument.show(fmt) ;
			pw.append( fmt.toString() ) ;
		}
		pw.flush() ;
		return TupleValue.theEmptyTuple ;
	}
}
