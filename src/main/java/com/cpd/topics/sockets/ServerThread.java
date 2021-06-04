package com.cpd.topics.sockets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author mpop
 */
public class ServerThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ServerThread.class);

	private ServerSocket serverSocket;

	private TokenService tokenService;

	public ServerThread(ServerSocket serverSocket, TokenService tokenService) {
		this.serverSocket = serverSocket;
		this.tokenService = tokenService;
	}

	@Override
	public void run() {
		Socket socket;

		while (true) {
			try {
				socket = serverSocket.accept();
				logger.info("Received left connection.");
				break;
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}

		BufferedReader in = null;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.info("Socket server ready to receiveToken messages");
		} catch (IOException e) {
			logger.error(e.toString());
		}

		if (in == null) {
			return;
		}

		while (true) {
			try {
				String message = in.readLine();

				if (message != null &&  "token".equals(message.toLowerCase())) {
					try {
						logger.info("Received token message, {}", tokenService);
						tokenService.receiveToken();
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
	}
}