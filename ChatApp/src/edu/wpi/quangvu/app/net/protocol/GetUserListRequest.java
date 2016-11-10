/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

/**
 * @author quangvu
 *
 */
public class GetUserListRequest extends ChatAppRequest {

	public static final String HEADER = "GUL";
	
	public GetUserListRequest() {
		super(HEADER);
	}

	@Override
	public GetUserListRequest parse(String request) {
		if (isValid(request)) {
			return new GetUserListRequest();
		}
		return null;
	}

}
