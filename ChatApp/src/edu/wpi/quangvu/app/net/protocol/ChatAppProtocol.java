/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

import java.util.HashMap;

/**
 * Network protocol layer of ChatApp
 * 
 * @author quangvu
 *
 */
public class ChatAppProtocol {

	/**
	 * Maximum size of a single request
	 */
	public static final int MAX_REQUEST_SIZE = 50;

	private static ChatAppProtocol instance = null;

	HashMap<String, ChatAppRequest> implemented = new HashMap<String, ChatAppRequest>();

	protected ChatAppProtocol() {
		implemented.put(AckRequest.HEADER, new AckRequest());		
		implemented.put(ErrorRequest.HEADER, new ErrorRequest());
		implemented.put(NewUserRequest.HEADER, new NewUserRequest());
		implemented.put(MessageRequest.HEADER, new MessageRequest());
		implemented.put(DisconnectRequest.HEADER, new DisconnectRequest());
		implemented.put(GetUserListRequest.HEADER, new GetUserListRequest());		
		implemented.put(UpdateUserListRequest.HEADER, new UpdateUserListRequest());
	}

	public static ChatAppProtocol getInstance() {
		if (instance == null) {
			instance = new ChatAppProtocol();
		}
		return instance;
	}

	public ChatAppRequest parse(String request) {
		if (request == null) {
			return null;
		}
		
		String header = request.trim().split("\\s+")[0];

		if (implemented.containsKey(header)) {
			return implemented.get(header).parse(request);
		}

		return null;
	}

}
