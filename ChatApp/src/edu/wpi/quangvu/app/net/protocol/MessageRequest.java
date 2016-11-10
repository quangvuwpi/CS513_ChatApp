/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author quangvu
 *
 */
public class MessageRequest extends ChatAppRequest {

	public static final String HEADER = "MSG";
	public static final String FORMAT = HEADER + " @%s #%s <|%s|>";

	public static final String REGEX = "^MSG @(\\w*)\\s+#(\\w+)\\s+\\<\\|((.*))\\|\\>$";

	String recipient = "";
	String sender = "";
	String message = "";

	public MessageRequest() {
		super(HEADER);
	}
	
	public MessageRequest(String recipient, String sender, String message) {
		super(HEADER);
		
		this.recipient = recipient;
		this.sender = sender;
		this.message = message;
	}

	protected MessageRequest(String request) {
		super(HEADER);
		
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(request.trim());

		if (m.matches()) {
			recipient = m.group(1);
			sender = m.group(2);
			message = m.group(3);
		}
	}

	public String getRecipient() {
		return recipient;
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}	

	@Override
	public boolean isValid(String request) {
		if (request != null) {
			return request.trim().matches(REGEX);
		}
		return false;
	}

	public MessageRequest parse(String request) {
		if (isValid(request)) {
			return new MessageRequest(request);
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, recipient, sender, message);
	}

}
