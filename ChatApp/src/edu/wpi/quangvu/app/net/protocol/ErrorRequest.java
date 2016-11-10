/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

/**
 * @author quangvu
 *
 */
public class ErrorRequest extends ChatAppRequest {
	
	public static final String HEADER = "ERR";
	
	public ErrorRequest() {
		super(HEADER);
	}
	
	@Override
	public ErrorRequest parse(String request) {
		if (isValid(request)) {
			return new ErrorRequest();
		}
		return null;
	}

}
