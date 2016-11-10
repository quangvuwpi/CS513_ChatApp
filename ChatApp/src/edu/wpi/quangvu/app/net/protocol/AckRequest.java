/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

/**
 * @author quangvu
 *
 */
public class AckRequest extends ChatAppRequest {
	
	public static final String HEADER = "ACK";
	
	public AckRequest() {
		super(HEADER);
	}
	
	@Override
	public AckRequest parse(String request) {
		if (isValid(request)) {
			return new AckRequest();
		}
		return null;
	}

}
