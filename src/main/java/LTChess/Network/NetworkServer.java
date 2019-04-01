package LTChess.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;


public class NetworkServer {

	private static List<PrintWriter> writers = new ArrayList<PrintWriter>();
	private static List<BufferedReader> readers = new ArrayList<BufferedReader>();
	private static int clientId;
	private Thread serverMainThread;
	public static ServerSocket listener;
	public static boolean onesideClosed = false;
	public MainFrame parent;
	
	public NetworkServer(String name, int port, MainFrame parent) throws Exception {
		System.out.println("The LTChess server is running.");
		serverMainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listener = new ServerSocket(port);
					try {
						while (true) {
							if (clientId < 2) {
								new HandlerServer(listener.accept(), clientId).start();
								clientId++;
								if (clientId == 2)
								{
									listener.close();
									serverMainThread.interrupt();
								}
							}
							else
							{
								throw new ConnectException("Cannot connect because the server is full");
							}
						}
					} finally {
						listener.close();
					}
				} 
				catch (SocketException e1)
				{
					//Do nothing because the user want to close it
					System.out.println("Socket closed");
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		serverMainThread.start();
	}
	
	public static class HandlerServer extends Thread {
		private String name;
		public static BufferedReader in;
		public static PrintWriter out;
		private int clientId;
		private PrintWriter currentWriter;
		private BufferedReader currentReader;
		private PrintWriter oppoWriter;
		private BufferedReader oppoReader;
		public static Socket socket;
		
		public HandlerServer(Socket socket, int clientId) {
            try {
            	this.socket = socket;
            	this.clientId = clientId;
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out = new PrintWriter(socket.getOutputStream(), true);
	            writers.add(clientId,out);
	            readers.add(clientId,in);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
		public void run() 
		{
			if (isEnoughClient())
			{
				while (true)
				{
					//Ask the client 1 the give information about the board setup and send to client 2
					try {
						writers.get(0).println("GIVEMEINFOR");
						writers.get(1).println("GIVEYOUINFOR");
						//Send the first mover group to client 1
						String firstMoverGroup = readers.get(0).readLine();
						writers.get(1).println(firstMoverGroup);
						
						String sideGroup = readers.get(0).readLine();
						writers.get(1).println(sideGroup);
						
						//Send the minute to client 1
						String minTime = readers.get(0).readLine();
						writers.get(1).println(minTime);
						
						//Send the second time group to client 1
						String secondTime = readers.get(0).readLine();
						writers.get(1).println(secondTime);
						
						//Calculate the first mover
						if (firstMoverGroup.equals(sideGroup)) //Client 0 is the first mover
						{
							currentWriter = writers.get(0);
							currentReader = readers.get(0);
							oppoWriter = writers.get(1);
							oppoReader = readers.get(1);
						}
						else //Client 1 is the first mover
						{
							currentWriter = writers.get(1);
							currentReader = readers.get(1);
							oppoWriter = writers.get(0);
							oppoReader = readers.get(0);
						}
						
						//Start server for the clock
						new  NetworkClockServer(Integer.parseInt(minTime), Integer.parseInt(secondTime), firstMoverGroup);
						writers.get(0).println("STARTGAME");
						writers.get(1).println("STARTGAME");	
						break;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				while(true)
				{
					try {
						String move = currentReader.readLine();
						if (move == null)
							break;
						if (move.equals("swap"))
						{
							//Swap between currentReader and oppoReader
							BufferedReader tempRead = currentReader;
							PrintWriter teampWrite = currentWriter;
							currentReader = oppoReader;
							currentWriter = oppoWriter;
							oppoReader = tempRead;
							oppoWriter = teampWrite;
						}
						else
						{
							oppoWriter.println(move);
							//Swap between currentReader and oppoReader
							BufferedReader tempRead = currentReader;
							PrintWriter teampWrite = currentWriter;
							currentReader = oppoReader;
							currentWriter = oppoWriter;
							oppoReader = tempRead;
							oppoWriter = teampWrite;
						}
						
					} 
					catch (SocketException e)
					{
						JOptionPane.showMessageDialog(null,
								"An error occured in connection, maybe the connection of the client has been closed already",
								"Error", JOptionPane.ERROR_MESSAGE);
						oppoWriter.println("CLOSE");
						currentWriter.println("CLOSE");
						onesideClosed = true;
						break;
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					writers.get(0).close();
					writers.get(1).close();
					readers.get(0).close();
					readers.get(1).close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public static void stopServer() {	
		try {
			listener.close();
			clientId = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isEnoughClient()
	{
		return clientId >= 2;
	}
	
	public static int getClientId()
	{
		return clientId;
	}


}
