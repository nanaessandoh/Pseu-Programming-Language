package pseu.common;

public class CompileTimeError extends Exception {
	private static final long serialVersionUID = 5417770750076060213L;
	
	private SourceCoords coords ;
	
	public CompileTimeError( String message, SourceCoords coords ) {
		super( message + " At " + coords.toString() ) ;
		this.coords = coords ;
	}
	
	public SourceCoords getCoords() {
		return coords ;
	}
}
