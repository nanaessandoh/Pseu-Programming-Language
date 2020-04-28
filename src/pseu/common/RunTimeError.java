package pseu.common;

public class RunTimeError  extends Exception {
	private static final long serialVersionUID = 5417770750076060213L;
	
	private SourceCoords coords ;
	private int segmentNumber ;
	private int address ;
	
	public RunTimeError( String message, SourceCoords coords, int segmentNumber, int address ) {
		super( message +" At "+ coords.toString()
		     +". Machine code address ("+
			segmentNumber+", "+address+")" ) ;
		this.coords = coords ;
		this.segmentNumber = segmentNumber ;
		this.address = address ;
	}
	
	public SourceCoords getCoords() {
		return coords ;
	}
	
	public int getSegmentNumber() {
		return segmentNumber ;
	}
	
	public int getAddress() {
		return address ;
	}
}
