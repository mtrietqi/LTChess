package LTChess.Network;

import java.awt.JobAttributes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import LTChess.Network.NetworkServer.HandlerServer;

public class NetworkClockServer {
	private Thread serverClockThread;
	private ServerSocket listener;
	private static int clientId;
	private static int whiteTime;
	private static int blackTime;
	private static List<PrintWriter> writers = new ArrayList<PrintWriter>();
	private static List<BufferedReader> readers = new ArrayList<BufferedReader>();
	private static String currentMover;
	private static int allowedTime;

	public NetworkClockServer(int minTime, int secondTime, String firstMover) {
		currentMover = firstMover;
		this.whiteTime = 1000 * (minTime * 60 + secondTime);
		this.blackTime = 1000 * (minTime * 60 + secondTime);
		allowedTime = this.whiteTime;
		serverClockThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listener = new ServerSocket(NetworkClient.port + 1);
					while (true) {
						if (clientId < 2) {
							new HandlerClockServer(listener.accept(), clientId).start();
							clientId++;
							if (clientId == 2) {
								listener.close();
								break;
							}
						}
					}
					while (true)
					{
						if (currentMover.equals("WHITE") && NetworkClockServer.whiteTime> -1)
						{
							if (NetworkClockServer.whiteTime % 1000 == 0 || NetworkClockServer.whiteTime == 0)
							{
								writers.get(0).println("WHITE "+NetworkClockServer.whiteTime);
								writers.get(1).println("WHITE "+NetworkClockServer.whiteTime);
							}
							if (allowedTime != 0)
							{
								NetworkClockServer.whiteTime--;
							}
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						else if (currentMover.equals("BLACK") && NetworkClockServer.blackTime > -1)
						{
							if (NetworkClockServer.blackTime % 1000 == 0 || NetworkClockServer.blackTime == 0)
							{
								writers.get(0).println("BLACK "+NetworkClockServer.blackTime);
								writers.get(1).println("BLACK "+NetworkClockServer.blackTime);
							}
							if (allowedTime != 0)
							{
								NetworkClockServer.blackTime--;
							}
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						else //Out of time
						{
							String winner;
							if (currentMover.equals("WHITE"))
								winner = "BLACK";
							else
								winner = "WHITE";
							writers.get(0).println("WIN "+winner);
							writers.get(1).println("WIN "+winner);
							break;
						}
					}
					writers.get(0).close();
					writers.get(1).close();
					readers.get(0).close();
					readers.get(1).close();
					listener.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		serverClockThread.start();
	}

	public class HandlerClockServer extends Thread {
		private Socket socket;
		private int clientId;
		private BufferedReader in;
		private PrintWriter out;

		public HandlerClockServer(Socket socket, int clientId) {
			this.socket = socket;
			this.clientId = clientId;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				writers.add(clientId, out);
				readers.add(clientId, in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run()
		{
			while (true)
			{
				try {
					String command = in.readLine();
					if (command.startsWith("SWAP"))
					{
						currentMover = command.substring(5);
					}
					else if (command.equals("QUIT"))
					{
						writers.get(0).close();
						writers.get(1).close();
						readers.get(0).close();
						readers.get(1).close();
						listener.close();
						break;
					}
				} catch (IOException e) {
				}
			}
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
