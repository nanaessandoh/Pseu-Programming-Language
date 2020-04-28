package tests;

import pseu.codeStore.InstructionFactoryI;
import pseu.common.SourceCoords;

public class MockInstructionFactory implements InstructionFactoryI<MockInstruction> {
	@Override
	public MockInstruction pushBool(boolean b, SourceCoords coords) {
		String info = "pushBool "+b ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction pushInt(String str, SourceCoords coords) {
		String info = "pushInt "+str ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction pushString(String str, SourceCoords coords) {
		String info = "pushString "+str ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction makeTuple(int count, SourceCoords coords) {
		String info = "makeTuple "+count ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction makeSet(int count, SourceCoords coords) {
		String info = "makeSet "+count ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction makeSeq(int count, SourceCoords coords) {
		String info = "makeSeq "+count ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction makeClosure(int count, SourceCoords coords) {
		String info = "makeClosure "+count ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction pushLocation(int depth, int number, SourceCoords coords) {
		String info = "pushLocation " +depth+ " " +number ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction fetch(SourceCoords coords) {
		String info = "fetch" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction store(int n, SourceCoords coords) {
		String info = "store "+n ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction lockLocation(SourceCoords coords) {
		String info = "lockLocation" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction unlockLocation(SourceCoords coords) {
		String info = "unlockLocation" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction pop(int n, SourceCoords coords) {
		String info = "pop "+n ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction duplicate(SourceCoords coords) {
		String info = "duplicate" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction rotateUp(int n, SourceCoords coords) {
		String info = "rotateUp "+n ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction rotateDown(int n, SourceCoords coords) {
		String info = "rotateDown "+n ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction lookup(SourceCoords coords) {
		String info = "lookup" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction apply(SourceCoords coords) {
		String info = "apply" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction returnNow(SourceCoords coords) {
		String info = "returnNow" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction jump(int i, SourceCoords coords) {
		String info = "jump "+i ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction jumpOnFalse(int i, SourceCoords coords) {
		String info = "jumpOnFalse "+i ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction jumpOnTrue(int i, SourceCoords coords) {
		String info = "jumpOnTrue "+i ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction newFrame(int count, SourceCoords coords) {
		String info = "newFrame "+count ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction popFrame(SourceCoords coords) {
		String info = "popFrame" ;
		return new MockInstruction( info, coords ) ;
	}

	@Override
	public MockInstruction constructType(String name, int n, SourceCoords coords) {
		String info = "constructType " +name+ " " +n;
		return new MockInstruction( info, coords ) ;
	}

}
