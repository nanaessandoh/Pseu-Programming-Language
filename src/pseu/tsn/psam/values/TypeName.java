package pseu.tsn.psam.values;

import pseu.common.Assert;

public enum TypeName {
	NoneTypeName {
		public String toString() { return "None"; }
		
		public boolean containsValue( Type ty, Value val ) {
			return false ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return true ;
		}
	},
	AnyTypeName {
		public String toString() { return "Any"; }
		
		public boolean containsValue( Type ty, Value val ) {
			return true ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return right.name == AnyTypeName ;
		}
	},
	IntTypeName{
		public String toString() { return "Int"; }
		
		public boolean containsValue( Type ty, Value val ) {
			return val.isInt() ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return right.name == AnyTypeName || right.name == IntTypeName ;
		}
	},
    StringTypeName{
		public String toString() { return "String"; }
		
		public boolean containsValue( Type ty, Value val ) {
			return val.isString() ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return right.name == AnyTypeName || right.name == StringTypeName ;
		}
	},
    BoolTypeName{
		public String toString() { return "Bool"; }
		
		public boolean containsValue( Type ty, Value val ) {
			return val.isBool() ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return right.name == AnyTypeName || right.name == BoolTypeName ;
		}
	},
    UnitTypeName{
		public String toString() { return "Unit"; }
		
		public boolean containsValue( Type ty, Value val ) {
			if( ! val.isTuple() ) return false ;
			TupleValue tuple = (TupleValue) val ;
			return tuple.size() == 0 ; }

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			return right.name == AnyTypeName || right.name == UnitTypeName ;
		}
	},
	ProductTypeName {
		public String toString() { return "Product"; }
		
		@Override public boolean argCount( int count ) {
			return count > 1 ; }
		
		public boolean containsValue( Type ty, Value val ) {
			if( ! val.isTuple() ) return false ;
			TupleValue tuple = (TupleValue) val ;
			if( tuple.size()  !=  ty.children.length )
				return false ;
			for( int i = 0 ; i < ty.children.length  ; ++i ) {
				if( ! ty.children[i].containsValue( tuple.get(i) ) )
					return false ;
			}
			return true ;
		}

		@Override
		boolean isSuptypeOf(Type left, Type right) {
			if( right.name == AnyTypeName ) return true ;
			if( right.name != ProductTypeName ) return false ;
			if( right.children.length != left.children.length ) return false ;
			// Two products with same length. We recurse on the children
			for( int i = 0 ; i < right.children.length ; ++i ) 
				if( ! left.children[0].isSubtypeOf( right.children[i]) )
					return false ;
			return true ;
		}
	},
	FunTypeName {
		public String toString() { return "Fun"; }
		
		@Override public boolean argCount( int count ) {return count==2 ;}
		
		public boolean containsValue( Type ty, Value val ) {
			if( ! val.isFunction() ) return false ;
			FunctionValue fval = (FunctionValue) val ;
			Type valArgType = fval.argumentType ;
			Type valResultType = fval.resultType ;
			Assert.check( ty.children.length == 2 ) ;
			Type myArgType = ty.children[0] ;
			Type myResultType = ty.children[1] ;
			return myArgType.isSubtypeOf( valArgType )
				&& valResultType.isSubtypeOf( myResultType ) ;
		}
		
		@Override
		boolean isSuptypeOf(Type left, Type right) {
			if( right.name == AnyTypeName ) return true ;
			if( right.name != FunTypeName ) return false ;
			// Two function types: a0 -> r0   <=  a1 -> r1 ?
			Type a0 = left.children[0] ;
			Type r0 = left.children[1] ;
			Type a1 = right.children[0] ;
			Type r1 = right.children[1] ;
			return a1.isSubtypeOf( a0 ) && r0.isSubtypeOf( r1 ) ;
		}
	},
	SetTypeName {
		public String toString() { return "Set"; }
		
		@Override public boolean argCount( int count ) {return count==1 ;}
		
		public boolean containsValue( Type ty, Value val ) {
			if( ! val.isSet() ) return false ;
			Type childType = ty.children[0] ;
			SetValue set = (SetValue) val ;
			for( int i = 0 ; i < set.size()  ; ++i ) {
				if( ! childType.containsValue( set.get(i) ) )
					return false ;
			}
			return true ;
		}
		@Override
		boolean isSuptypeOf(Type left, Type right) {
			if( right.name == AnyTypeName ) return true ;
			if( right.name != SetTypeName ) return false ;
			// Two sets
			return left.children[0].isSubtypeOf( right.children[0] ) ;
		}
	},
	SeqTypeName {
		public String toString() { return "Seq"; }
		
		@Override public boolean argCount( int count ) {return count==1 ;}
		
		public boolean containsValue( Type ty, Value val ) {
			if( ! val.isSeq() ) return false ;
			Type childType = ty.children[0] ;
			SequenceValue seq = (SequenceValue) val ;
			for( int i = 0 ; i < seq.size()  ; ++i ) {
				if( ! childType.containsValue( seq.get(i) ) )
					return false ;
			}
			return true ;
		}
		@Override
		boolean isSuptypeOf(Type left, Type right) {
			if( right.name == AnyTypeName ) return true ;
			if( right.name != SeqTypeName ) return false ;
			// Two sequence types
			return left.children[0].isSubtypeOf( right.children[0] ) ;
		}
	},
	IteratorTypeName {
		public String toString() { return "Iterator"; }
		
		@Override public boolean argCount( int count ) {return count==1 ;}
		
		public boolean containsValue( Type ty, Value val ) {
			Assert.toBeDone("FunType.containsValue");
			return false ; }
		@Override
		boolean isSuptypeOf(Type left, Type right) {
			if( right.name == AnyTypeName ) return true ;
			if( right.name != IteratorTypeName ) return false ;
			// Two sequence types
			return left.children[0].isSubtypeOf( right.children[0] ) ;
		}
	};
	
	/** Precondition argCount( children.length ) */
	public Type makeType( Type[] children ) {
		return new Type( this, children ) ;
	}
	public boolean argCount( int count ) {
		return count==0 ; }
	
	abstract boolean containsValue( Type ty, Value val ) ;
	
	abstract boolean isSuptypeOf( Type left, Type right ) ;
	
	/** May return null */
	public static TypeName stringToName(String string) {
		for( TypeName name : TypeName.values() ) {
			if( name.toString().equals( string ) ) return name ; }
		return null ; }
	
	static public final Type intType = TypeName.IntTypeName.makeType( new Type[0] ) ;
	static public final Type boolType = TypeName.BoolTypeName.makeType( new Type[0] ) ;
	static public final Type stringType = TypeName.StringTypeName.makeType( new Type[0] ) ;
	static public final Type unitType = TypeName.UnitTypeName.makeType( new Type[0] ) ;
	static public final Type noneType = TypeName.NoneTypeName.makeType( new Type[0] ) ;
	static public final Type anyType = TypeName.AnyTypeName.makeType( new Type[0] ) ;
}
