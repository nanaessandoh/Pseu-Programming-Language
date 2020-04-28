package pseu.tsn.psam.values;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Formatter;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class ReadStringBuiltInFunction extends BuiltInFunctionValue{


	public ReadStringBuiltInFunction() {
		super(TypeName.unitType, TypeName.stringType );
	}

	@Override
	public void show(Formatter fmt) {
		fmt.format("readString : Unit -> String") ;
	}
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
	throws RunTimeError {
		BufferedReader reader = vm.getInputSource();
		String line ;
		try {
			line = reader.readLine() ;
		} catch (IOException e) {
			vm.error("IOException while reading.") ;
			line = null ;
		}
		return new StringValue( line ) ;
	}
}
