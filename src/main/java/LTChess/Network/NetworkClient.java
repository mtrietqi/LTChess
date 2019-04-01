package LTChess.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;
import LTChess.Group.Group;

public class NetworkClient {
	public static String address;
	public static int port;
	private int id;
	public MainFrame parent;
	private NewGameOModeDialogServer serverDialog;
	private NewGameOModeDialogClient clientDialog;
	
	public NetworkClient(String address, int port, int id, MainFrame parent, NewGameOModeDialogServer serverDialog,
			NewGameOModeDialogClient clientDialog) {
		this.parent = parent;
		this.address = address;
		this.port = port;
		this.id = id;
		this.serverDialog = serverDialog;
		this.clientDialog = clientDialog;
		new ClientHandler().start();
	}

	public class ClientHandler extends Thread {
		
		private Group firstMoverGroup;
		private int minTime;
		private int secondTime;
		private Group sideGroup;
		public  Socket socket;
		public BufferedReader in;
		public PrintWriter out;
		public boolean closeMe = false;

		public ClientHandler() {
			parent.setClientHandler(this);
		}
		
		
		public Group getFirstMoverGroup() {
			return firstMoverGroup;
		}



		public int getMinTime() {
			return minTime;
		}



		public int getSecondTime() {
			return secondTime;
		}



		public Group getSideGroup() {
			return sideGroup;
		}
		


		public Socket getSocket() {
			return socket;
		}



		public BufferedReader getIn() {
			return in;
		}



		public PrintWriter getOut() {
			return out;
		}
		
		
		public void setCloseMe(boolean closeMe)
		{
			this.closeMe = closeMe;
		}


		public void run()
		{
			try {
				socket = new Socket(address, port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true)
				{
					String line = in.readLine();
					//Client 1 side
					if (line.equals("GIVEMEINFOR"))
					{
						firstMoverGroup = serverDialog.getFirstMoverGroup();
						sideGroup = serverDialog.getSideGroup();
						minTime = Integer.parseInt(serverDialog.getMinTime());
						secondTime = Integer.parseInt(serverDialog.getSecondTime());
						
						out.println(serverDialog.getFirstMoverGroup().toString());
						out.println(serverDialog.getSideGroup().toString());
						out.println(serverDialog.getMinTime().toString());
						out.println(serverDialog.getSecondTime());
						line = in.readLine();
						if (line.equals("STARTGAME"))
						{
							serverDialog.stopServerWaiting();
							break;
						}
					}
					else if (line.equals("GIVEYOUINFOR"))
					{
						line = in.readLine();
						if (line.equals("WHITE"))
						{
							firstMoverGroup = Group.WHITE;
						}
						else
						{
							firstMoverGroup = Group.BLACK;
						}
						line = in.readLine();
						if (line.equals("WHITE"))
						{
							sideGroup = Group.BLACK;
						}
						else
						{
							sideGroup = Group.WHITE;
						}
						line = in.readLine();
						minTime = Integer.parseInt(line);
						line = in.readLine();
						secondTime = Integer.parseInt(line);
						line = in.readLine();
						if (line.equals("STARTGAME"))
						{
							clientDialog.dispose();
							break;
						}
					}
				}
				
				//Start new game
				if (parent.board == null)
				{
					parent.startNewGame(true, firstMoverGroup, minTime, secondTime, false,ClientHandler.this);
				}
				else
				{
					parent.startAnotherGame(true, firstMoverGroup, minTime, secondTime, false,ClientHandler.this);
				}
				Thread.sleep(1500);
				while(!closeMe)
				{
					Thread.sleep(100);
					if (BoardPanel.blockMouseSelection) //This client is waiting for his opponent
					{
						String move = in.readLine();
						if (move.equals("CLOSE")) //Lost connection already, this client has to close
						{
							parent.lostConnection();
							break;
						}
						else if (move.contains("promo"))
						{
							String[] info = move.split(" ");
							int current = Integer.parseInt(info[1]);
							int des = Integer.parseInt(info[2]);
							String piece = info[3];
							parent.oppoPromote(current, des, piece);
							BoardPanel.blockMouseSelection = false;
						}
						else
						{
							parent.oppoMakeMove(move);
							BoardPanel.blockMouseSelection = false;
						}
					}
					else //This client is the move maker
					{
						while(!closeMe)
						{
							Thread.sleep(100);
							if (BoardPanel.messageSend != null)
							{
								out.println(BoardPanel.messageSend);
								BoardPanel.messageSend = null;
								BoardPanel.blockMouseSelection = true;
								break;
							}
						}
					}
				}
			} catch (ConnectException e) {
				if (id == 1)
				{
					JOptionPane.showMessageDialog(null,
							"An error occured in connection process, maybe the server is full or it has been closed already",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			catch (NullPointerException e)
			{
				JOptionPane.showMessageDialog(null,
						"Cannot connect to your opponent because the server has been closed already",
						"Error", JOptionPane.ERROR_MESSAGE);
				closeMe = true;
				parent.lostConnection();
			}
			catch (UnknownHostException e)
			{
				JOptionPane.showMessageDialog(null,
						"The connection address is invalid. Please check it again.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return ;
			} catch (SocketException e)
			{
				JOptionPane.showMessageDialog(null,
						"Cannot connect to your opponent because the server has been closed already",
						"Error", JOptionPane.ERROR_MESSAGE);
				closeMe = true;
				parent.lostConnection();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
