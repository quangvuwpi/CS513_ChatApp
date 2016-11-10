/**
 * 
 */
package edu.wpi.quangvu.app.resource;

import java.nio.channels.AsynchronousSocketChannel;
import edu.wpi.quangvu.app.net.protocol.ChatAppRequest;
import edu.wpi.quangvu.app.net.transport.NetConnection;
import edu.wpi.quangvu.app.net.transport.TransportTask;
import edu.wpi.quangvu.app.session.ChatAppSession.STATE;

/**
 * ChatApp client class
 * 
 * @author quangvu
 *
 */
public class Client {

	/** User name **/
	private String name = "Anonymous";

	/** Associated socket channel **/
	private final NetConnection connection;

	/** Session state **/
	private STATE currentState;

	public Client(AsynchronousSocketChannel channel) {
		connection = new NetConnection(channel, this);
		TransportTask.getInstance().add(connection);		
		setState(STATE.CONNECT);
	}

	public Client(AsynchronousSocketChannel channel, String name) {
		this.name = name;

		connection = new NetConnection(channel, this);
		TransportTask.getInstance().add(connection);
		setState(STATE.CONNECT);
	}

	public ChatAppRequest getNextRequest() {
		return connection.getNext();
	}

	public void sendRequest(ChatAppRequest request) {
		connection.send(request);
	}

	public void disconnect() {
		TransportTask.getInstance().remove(connection);
		connection.close();
	}

	public STATE getState() {
		return currentState;
	}

	public void setState(STATE state) {
		currentState = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
