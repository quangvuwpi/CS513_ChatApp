/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

/**
 * @author quangvu
 *
 */
public class DisconnectRequest extends ChatAppRequest {

	public static final String HEADER = "DIS";

	public DisconnectRequest() {
		super(HEADER);
	}
	
	@Override
	public DisconnectRequest parse(String request) {
		if (isValid(request)) {
			return new DisconnectRequest();
		}
		return null;
	}
	
}
