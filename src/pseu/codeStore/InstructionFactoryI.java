package pseu.codeStore;

import pseu.common.SourceCoords;

public interface InstructionFactoryI<Instr extends InstructionI> {
	Instr pushBool( boolean b, SourceCoords coords ) ;
	Instr pushInt( String str, SourceCoords coords ) ; 
	Instr pushString( String str, SourceCoords coords ) ;
	Instr makeTuple( int count, SourceCoords coords ) ;
	Instr makeSet( int count, SourceCoords coords ) ;
	Instr makeSeq( int count, SourceCoords coords ) ;
    Instr makeClosure(int count, SourceCoords coords) ;
    Instr pushLocation(int depth, int number, SourceCoords coords) ;
    Instr fetch(SourceCoords coords) ;
    Instr store( int n, SourceCoords coords) ;
    Instr lockLocation(SourceCoords coords) ;
    Instr unlockLocation(SourceCoords coords) ;
    Instr pop(int n, SourceCoords coords) ;
    Instr duplicate(SourceCoords coords) ;
    Instr rotateUp(int n, SourceCoords coords) ;
    Instr rotateDown(int n, SourceCoords coords) ;
    Instr lookup(SourceCoords coords) ;
    Instr apply(SourceCoords coords) ;
    Instr returnNow(SourceCoords coords) ;
    Instr jump( int i, SourceCoords coords) ;
    Instr jumpOnFalse( int i, SourceCoords coords) ;
    Instr jumpOnTrue( int i, SourceCoords coords) ;
    Instr newFrame(int count, SourceCoords coords) ;
    Instr popFrame(SourceCoords coords) ;
    Instr constructType(String name, int n, SourceCoords coords) ;
}
