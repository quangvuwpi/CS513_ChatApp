/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

/**
 * Request passed between clients and server
 * 
 * @author quangvu
 *
 */
public abstract class ChatAppRequest {

	public String HEADER = "";

	public ChatAppRequest(String header) {
		HEADER = header;
	}

	public boolean isValid(String request) {
		if (request != null) {
			return request.trim().matches(HEADER);
		}
		return false;
	}

	public ChatAppRequest parse(String request) {
		return null;
	}

	@Override
	public String toString() {
		return HEADER;
	}

}
