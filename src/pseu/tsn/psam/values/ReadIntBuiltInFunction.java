package pseu.tsn.psam.values;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import pseu.common.RunTimeError;
import pseu.tsn.psam.PSAM;

public class ReadIntBuiltInFunction extends BuiltInFunctionValue{


	public ReadIntBuiltInFunction() {
		super(TypeName.unitType, TypeName.intType );
	}

	@Override
	public void show(Formatter fmt) {
		fmt.format("readString : Unit -> String") ;
	}
	
	private static Pattern  intPattern = Pattern.compile( "(-)?[0-9]+") ;
	
	@Override
	protected Value valueOfApply( Value argument, PSAM vm )
	throws RunTimeError {
		BufferedReader reader = vm.getInputSource();
		Scanner scanner = new Scanner( reader ) ;
		String bigIntString ;
		try {
			if( scanner.hasNext( intPattern ) ) {
				bigIntString = scanner.next( intPattern ) ; }
			else {
				vm.error("Next input is not an integer." ) ;
				bigIntString = null ;
			}
		} catch (IllegalStateException  e) {
			vm.error("IllegalStateException while reading.") ;
			bigIntString = null ;
		} catch (NoSuchElementException  e) {
			vm.error("NoSuchElementException while reading.") ;
			bigIntString = null ;
		}
		return IntValue.make( bigIntString, vm ) ;
	}
}

