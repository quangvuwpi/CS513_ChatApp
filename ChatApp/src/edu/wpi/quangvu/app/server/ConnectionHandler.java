package edu.wpi.quangvu.app.server;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import edu.wpi.quangvu.app.resource.Client;
import edu.wpi.quangvu.app.resource.ClientQueue;

class ConnectionHandler implements
		CompletionHandler<AsynchronousSocketChannel, String> {

	final ClientQueue queue;

	public ConnectionHandler(ClientQueue q) {
		queue = q;
	}

	@Override
	public void completed(AsynchronousSocketChannel result, String attachment) {
		Client c = new Client(result, attachment);
		queue.add(c);
	}

	@Override
	public void failed(Throwable exc, String attachment) {
		exc = new Exception("Client failed to connect");
	}

}