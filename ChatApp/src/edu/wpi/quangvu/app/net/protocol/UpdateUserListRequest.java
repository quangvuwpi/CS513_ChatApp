/**
 * 
 */
package edu.wpi.quangvu.app.net.protocol;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author quangvu
 *
 */
public class UpdateUserListRequest extends ChatAppRequest {

	public static final String HEADER = "UUL";

	public static final String PREFIX_ADD = "+";
	public static final String PREFIX_REMOVE = "-";

	public static final String FORMAT_ADD = PREFIX_ADD + "%s";
	public static final String FORMAT_REMOVE = PREFIX_REMOVE + "%s";

	public static final String REGEX = "^UUL ([\\+|\\-]((\\w+))\\s*)+$";

	protected ArrayList<String> addList = null;
	protected ArrayList<String> removeList = null;

	public UpdateUserListRequest() {
		super(HEADER);
	}

	protected UpdateUserListRequest(String request) {
		super(HEADER);
		
		addList = getAddUser(request);
		removeList = getRemoveUser(request);
	}

	public void add(String name) {
		if (addList == null) {
			addList = new ArrayList<String>();
		}
		if (!addList.contains(name)) {
			addList.add(name);
		}
	}

	public void add(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			add(list.get(i));
		}
	}

	public void remove(String name) {
		if (removeList == null) {
			removeList = new ArrayList<String>();
		}
		if (!removeList.contains(name)) {
			removeList.add(name);
		}
	}

	public void remove(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			remove(list.get(i));
		}
	}

	public ArrayList<String> getAddList() {
		return addList;
	}

	public ArrayList<String> getRemoveList() {
		return removeList;
	}

	public boolean isValid(String request) {
		if (request != null) {
			return request.trim().matches(REGEX);
		}
		return false;
	}

	protected ArrayList<String> getAddUser(String request) {
		return getUser(request, PREFIX_ADD);
	}

	protected ArrayList<String> getRemoveUser(String request) {
		return getUser(request, PREFIX_REMOVE);
	}

	protected ArrayList<String> getUser(String request, String prefix) {
		if (isValid(request)) {
			ArrayList<String> list = new ArrayList<String>();

			Pattern p = Pattern.compile(REGEX);
			
			String str = request.trim();
			int index = str.length();

			while (index != -1) {
				Matcher m = p.matcher(str.subSequence(0, index));
				if (m.matches()) {
					String entry = m.group(1);
					if (entry.startsWith(prefix)) {
						list.add(m.group(2));
					}
					index = m.start(1);
				} else {
					index = -1;
				}
			}
			return list;
		}
		return null;
	}

	public UpdateUserListRequest parse(String request) {
		if (isValid(request)) {
			return new UpdateUserListRequest(request);
		}
		return null;
	}

	protected String listToAddString(ArrayList<String> list) {
		return listToString(list, PREFIX_ADD);
	}

	protected String listToRemoveString(ArrayList<String> list) {
		return listToString(list, PREFIX_REMOVE);
	}

	protected String listToString(ArrayList<String> list, String prefix) {
		String output = "";
		for (int i = 0; i < list.size(); i++) {
			output += String.format("%s%s ", prefix, list.get(i));
		}
		return output;
	}

	@Override
	public String toString() {
		String output = HEADER + " ";

		if (addList != null) {
			output += listToAddString(addList);
		}
		if (removeList != null) {
			output += listToRemoveString(removeList);
		}

		return output;
	}

}
