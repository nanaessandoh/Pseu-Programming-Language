package pseu.common;

import java.util.Formatter;

/** Source coordinates
 * <p>
 * Invariant: One of the following will be true
 * <ul><li> The filenames are unequal
 *     <li> The filename are equal and the start line is before the end line
 *     <li> The filenames are equal and the start and end lines are equal and
 *           the start column is less or equal to the end column.
 * <ull>
 * 
 * @author theo
 *
 */
public class SourceCoords {

	private String startFileName ;
	private int startLine ; /* Starting at 1 */
	private int startColumn ; /* Starting at 1.  Tabs count as one character */

	private String endFileName ;
	private int endLine ; /* Starting at 1 */
	private int endColumn ; /* Starting at 1.  Tabs count as one character */

	public SourceCoords( String startFileName,
						 int startLine,
						 int startColumn,
						 String endFileName,
						 int endLine,
						 int endColumn ) {
		this.startFileName = startFileName ;
		this.startLine = startLine ;
		this.startColumn = startColumn ;
		this.endFileName = endFileName ;
		this.endLine = endLine ;
		this.endColumn = endColumn ;
		if( this.startFileName.equals(this.endFileName) ) {
			if(    this.startLine > this.endLine
				||    this.startLine ==  this.endLine
				   && this.startColumn > this.endColumn ) {
				this.endLine = 	this.startLine ;
				this.endColumn = this.startColumn ; 
			}
		}
	}
	
	public SourceCoords combine( SourceCoords other ) {
		return new SourceCoords(
			this.startFileName,
			this.startLine,
			this.startColumn,
			other.endFileName,
			other.endLine,
			other.endColumn ) ;
	}
	
	@Override
	public String toString() {
		Formatter fmt = new Formatter( ) ;
		if( startFileName.equals( endFileName) ) {
			if( startLine == endLine ) {
				if( startColumn == endColumn ) {
					fmt.format("%s (line: %d column: %d)",
							startFileName, startLine, startColumn) ;}
		
				else {
					fmt.format("%s (line: %d columns: %d--%d)",
							startFileName, startLine, startColumn, endColumn) ; } } 
			else {
				fmt.format("%s (line: %d column: %d)--(line: %d column: %d)",
						startFileName, startLine, startColumn, endLine, endColumn) ; }
		} else {
			fmt.format("%s (line: %d column: %d)--%s (line: %d column: %d)",
					startFileName, startLine, startColumn, endFileName, endLine, endColumn) ; }
		try { return fmt.toString() ; }
		finally { fmt.close() ; }
	}
	
}
