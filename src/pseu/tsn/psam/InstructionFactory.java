package pseu.tsn.psam;

import pseu.codeStore.InstructionFactoryI;
import pseu.common.Assert;
import pseu.common.SourceCoords;

public class InstructionFactory implements InstructionFactoryI<Instruction> {

	@Override
	public Instruction pushBool(boolean b, SourceCoords coords) {
		return new PushBool( b, coords ) ;
	}

	@Override
	public Instruction pushInt(String str, SourceCoords coords) {
		return new PushInt( str, coords ) ;
	}

	@Override
	public Instruction pushString(String str, SourceCoords coords) {
		return new PushString( str, coords ) ;
	}

	@Override
	public Instruction makeTuple(int count, SourceCoords coords) {
		Assert.check( count != 1 );
		return new MakeTuple( count, coords ) ;
	}

	@Override
	public Instruction makeSet(int count, SourceCoords coords) {
		return new MakeSet( count, coords ) ;
	}

	@Override
	public Instruction makeSeq(int count, SourceCoords coords) {
		return new MakeSeq( count, coords ) ;
	}

	@Override
	public Instruction makeClosure(int count, SourceCoords coords) {
		return new MakeClosure(count, coords) ;
	}

	@Override
	public Instruction pushLocation(int depth, int number, SourceCoords coords) {
		return new PushLocation( depth, number, coords ) ;
	}

	@Override
	public Instruction fetch(SourceCoords coords) {
		return new Fetch( coords ) ;
	}

	@Override
	public Instruction store(int n, SourceCoords coords) {
		return new Store( n, coords ) ;
	}

	@Override
	public Instruction lockLocation(SourceCoords coords) {
		return new LockLocation( coords ) ;
	}

	@Override
	public Instruction unlockLocation(SourceCoords coords) {
		return new UnlockLocation( coords ) ;
	}

	@Override
	public Instruction pop(int n, SourceCoords coords) {
		return new PopInstruction( n, coords ) ;
	}

	@Override
	public Instruction duplicate(SourceCoords coords) {
		return new DuplicateInstruction( coords ) ;
	}

	@Override
	public Instruction rotateUp(int n, SourceCoords coords) {
		return new RotateUpInstruction( n, coords ) ;
	}

	@Override
	public Instruction rotateDown(int n, SourceCoords coords) {
		return new RotateDownInstruction( n, coords ) ;
	}

	@Override
	public Instruction lookup(SourceCoords coords) {
		return new LookupInstruction( coords ) ;
	}

	@Override
	public Instruction apply(SourceCoords coords) {
		return new ApplyInstruction( coords ) ;
	}

	@Override
	public Instruction returnNow(SourceCoords coords) {
		return new ReturnNowInstruction( coords ) ;
	}

	@Override
	public Instruction jump(int offset, SourceCoords coords) {
		return new JumpInstruction( offset, coords ) ;
	}

	@Override
	public Instruction jumpOnFalse(int offset, SourceCoords coords) {
		return new JumpOnFalseInstruction( offset, coords ) ;
	}

	@Override
	public Instruction jumpOnTrue(int offset, SourceCoords coords) {
		return new JumpOnTrueInstruction( offset, coords ) ;
	}

	@Override
	public Instruction newFrame(int count, SourceCoords coords) {
		return new NewFrame( count, coords) ;
	}

	@Override
	public Instruction popFrame(SourceCoords coords) {
		return new PopFrame( coords) ;
	}

	@Override
	public Instruction constructType(String name, int n, SourceCoords coords) {
		return new ConstructType( name, n, coords) ;
	}
}
