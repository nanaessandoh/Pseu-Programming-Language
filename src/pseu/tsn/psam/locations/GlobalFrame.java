package pseu.tsn.psam.locations;

import pseu.tsn.psam.values.PrintBuiltInFunction;
import pseu.tsn.psam.values.ReadIntBuiltInFunction;
import pseu.tsn.psam.values.ReadStringBuiltInFunction;
import pseu.tsn.psam.values.Type;
import pseu.tsn.psam.values.TypeName;

public class GlobalFrame {

	public static Frame makeGlobalFrame() {
		Frame globalFrame = new Frame( null ) ;
	
		{	Type printType = Type.make( TypeName.FunTypeName, TypeName.anyType, TypeName.unitType ) ;
			Location printLocation = new Location( "print",  printType) ;
			printLocation.setValue( new PrintBuiltInFunction() );
			globalFrame.addLocation( printLocation );
		}
		{	Type readLineType = Type.make( TypeName.FunTypeName, TypeName.unitType, TypeName.stringType ) ;
			Location readLineLocation = new Location( "readLine",  readLineType) ;
			readLineLocation.setValue( new ReadStringBuiltInFunction() );
			globalFrame.addLocation( readLineLocation );
		}
		{	Type readIntType = Type.make( TypeName.FunTypeName, TypeName.unitType, TypeName.intType ) ;
			Location readIntLocation = new Location( "readInt",  readIntType) ;
			readIntLocation.setValue( new ReadIntBuiltInFunction() );
			globalFrame.addLocation( readIntLocation );
		}
		return globalFrame ;
	}
}
