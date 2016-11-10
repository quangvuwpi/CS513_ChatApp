/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * New user register request
 * 
 * @author quangvu
 *
 */
public class NewUserRequest extends ChatAppRequest {

	public static final String HEADER = "NEW";
	public static final String FORMAT = HEADER + " %s";
	
	public static final String REGEX = "^NEW (\\w+)$";

	public String name = null;

	public NewUserRequest() {
		super(HEADER);
	}

	public NewUserRequest(String name) {
		super(HEADER);
		
		this.name = name;
	}

	public boolean isValid(String request) {
		if (request != null) {
			return request.trim().matches(REGEX);
		}
		return false;
	}

	public String getName(String request) {
		if (isValid(request)) {
			Matcher m = Pattern.compile(REGEX).matcher(request.trim());
			if (m.matches()) {
				return m.group(1);
			}
		}
		return null;
	}

	public NewUserRequest parse(String request) {
		if (isValid(request)) {
			return new NewUserRequest(getName(request));
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, name);
	}

}
