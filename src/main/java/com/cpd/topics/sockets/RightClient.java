package com.cpd.topics.sockets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author mpop
 */
public class RightClient {

	private static Logger logger = LoggerFactory.getLogger(RightClient.class);

	private Socket socket;

	private BufferedReader in;

	private PrintWriter out;

	private String right;

	public RightClient(String right) {
		this.right = right;

		long errors = 0;

		long limit = 15;

//		try {
//			Locks.barrier.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		while (errors < limit) {
			try {
				socket = new Socket("127.0.0.1", Integer.parseInt(right));
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				logger.info("Connected to socket: " + right);
				break;
			} catch (IOException e) {
				//e.printStackTrace();
				errors += 1;

				try {
					Thread.sleep(250L);
				} catch (InterruptedException ex) {
					logger.error(e.toString());
				}
			}
		}

		if (errors >= limit) {
			logger.info("Failed to connect to right socket");
		}
	}

	public void sendToken() {
		out.println("token");
	}

	public void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
}
