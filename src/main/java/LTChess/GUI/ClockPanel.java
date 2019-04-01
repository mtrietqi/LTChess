package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import LTChess.Group.Group;
import LTChess.Network.NetworkClient;

public class ClockPanel extends JPanel implements java.io.Serializable {
	private String[] columnNames = { "White's Time", "Black's Time" };
	private String[][] tableData = new String[1][2];
	private List<String> mainData = new ArrayList<String>();
	private JScrollPane scrollPane;
	private Group currentMover;
	private JTable table;
	private int whiteTime;
	private int blackTime;
	private boolean stopClock;
	private Thread t;
	private MainFrame parent;
	private int allowTime;
	private boolean useClock = true;

	// Import data
	private String whiteTimeStr, blackTimeStr, currentMoverStr;
	
	//Online mode
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ClockPanel(MainFrame parent, int minTime, int secondTime) {
		this.setLayout(new BorderLayout());
		this.parent = parent;
		if (minTime == 0)
			useClock = false;
		allowTime = 1000 * (minTime * 60 + secondTime);
		tableData[0][0] = minTime + ":" + (secondTime < 10 ? "0" + secondTime : secondTime);
		tableData[0][1] = minTime + ":" + (secondTime < 10 ? "0" + secondTime : secondTime);
		table = new JTable(tableData, columnNames);
		table.setCellSelectionEnabled(true);
		table.setFont(this.getFont().deriveFont(Font.BOLD));
		table.setDefaultEditor(Object.class, null);
		// Align text center
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.setRowHeight(0, 25);

		// table.changeSelection(0, 0, false, false);
		scrollPane = new JScrollPane();
		table.setForeground(new Color(0, 0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		add(scrollPane);
		setPreferredSize(new Dimension(180, 50));
		setVisible(true);
	}

	public void startClock(Group alliance) {
		this.currentMover = alliance;
		whiteTime = allowTime;
		blackTime = allowTime;
		stopClock = false;
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (!MainFrame.board.onlineMode) //Not use online mode
					{
						while (whiteTime >= 0 && blackTime >= 0 && !stopClock) {
							clockStart();
							Thread.sleep(1);
						}
						if (whiteTime == -1 || blackTime == -1) {
							int answer = JOptionPane.showConfirmDialog(null,
									MainFrame.board.getCurrentPlayer().getOpponent().getAlliance()
											+ " wins the game because his opponent has just ran out of time. Congratulations!"
											+ " Do you want to play again?",
									"Run out of time", JOptionPane.YES_NO_CANCEL_OPTION);
							if (answer == JOptionPane.YES_OPTION) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										parent.nearlyOpenNew();
										parent.showNewGameDialog();
									}
								});
							} else {
								System.exit(0);
							}
						}
					}
					else //Use online mode
					{
						socket = new Socket(NetworkClient.address, NetworkClient.port+1);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream(), true);
						while (true)
						{
							String command = in.readLine();
							if (command.startsWith("WHITE"))
							{
								int time = Integer.parseInt(command.substring(6));
								setWhiteTime(time);
							}
							else if (command.startsWith("BLACK"))
							{
								int time = Integer.parseInt(command.substring(6));
								setBlackTime(time);
							}
							else if (command.startsWith("WIN"))
							{
								String winner = command.substring(4);
								in.close();
								out.close();
								socket.close();
								int answer = JOptionPane.showConfirmDialog(null,
										winner+ " wins the game because his opponent has just ran out of time. Congratulations!"
												+ " Do you want to play again?",
										"Run out of time", JOptionPane.YES_NO_CANCEL_OPTION);
								if (answer == JOptionPane.YES_OPTION) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											parent.nearlyOpenNew();
											parent.showNewGameDialog();
										}
									});
								} else {
									System.exit(0);
								}
								break;
							}
						}
					}
				} catch (InterruptedException | IOException e) {}
			}
		});
		t.start();
	}

	public void clockStart() {
		if (currentMover == Group.WHITE) {
			whiteStart();
		} else if (currentMover == Group.BLACK) {
			blackStart();
		}
	}

	private void blackStart() {
		if (allowTime != 0) {
			tableData[0][1] = convertToTime(blackTime--);
		}
		table.changeSelection(0, 1, false, false);
		this.repaint();
	}
	
	private void setBlackTime(int time)
	{
		tableData[0][1] = convertToTime(time);
		table.changeSelection(0, 1, false, false);
		this.repaint();
	}

	private void whiteStart() {
		if (allowTime != 0) 
		{
			tableData[0][0] = convertToTime(whiteTime--);
		}
		table.changeSelection(0, 0, false, false);
		this.repaint();
	}
	
	private void setWhiteTime(int time)
	{
		if (allowTime != 0)
		{
			tableData[0][0] = convertToTime(time);
		}
		table.changeSelection(0, 0, false, false);
		this.repaint();
	}

	private String convertToTime(int time) {
		return time / 60000 + " : " + ((time % 60000 < 10000) ? "0" + (time % 60000) / 1000 : (time % 60000) / 1000);
	}

	public void updateClockMover() {
		currentMover = MainFrame.board.getCurrentPlayer().getAlliance();
		if (MainFrame.board.onlineMode)
		{
			out.println("SWAP "+currentMover);
		}
	}

	public void stopClock() {
		if (!MainFrame.board.onlineMode)
		{
			stopClock = true;
			if (t != null) {
				t.interrupt();
			}
		}
		else
		{
			try {
				out.println("QUIT");
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {}
			
		}
	}

	public void updateClockMoverPromotion() {
		currentMover = MainFrame.board.getCurrentPlayer().getOpponent().getAlliance();
	}

	public void reset() {
		this.removeAll();
		this.setLayout(new BorderLayout());
		table = new JTable(tableData, columnNames);
		table.setCellSelectionEnabled(true);
		table.setFont(this.getFont().deriveFont(Font.BOLD));

		// Align text center
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.setRowHeight(0, 25);

		// table.changeSelection(0, 0, false, false);
		scrollPane = new JScrollPane();
		table.setForeground(new Color(0, 0, 0));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		scrollPane.setViewportView(table);
		add(scrollPane);
		setPreferredSize(new Dimension(180, 50));
		setVisible(true);
	}

	public String exportSaveData() {
		String start = "";
		System.out.println(useClock);
		if (useClock)
			start = "yes";
		else
			start = "no";
		return whiteTime + " " + blackTime + " " + currentMover + " " + start;
	}

	public void startClockFromSaveGame(String str) {
		String[] array = str.split(" ");
		whiteTimeStr = array[0];
		blackTimeStr = array[1];
		currentMoverStr = array[2];

		if (currentMoverStr.equals("WHITE")) {
			this.currentMover = Group.WHITE;
		} else if (currentMoverStr.equals("BLACK")) {
			this.currentMover = Group.BLACK;
		}
		whiteTime = Integer.parseInt(whiteTimeStr);
		blackTime = Integer.parseInt(blackTimeStr);
		tableData[0][0] = convertToTime(whiteTime);
		tableData[0][1] = convertToTime(blackTime);
		stopClock = false;
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (whiteTime >= 0 && blackTime >= 0 && !stopClock) {
						clockStart();
						Thread.sleep(1);
					}
					if (whiteTime == -1 || blackTime == -1) {
						int answer = JOptionPane.showConfirmDialog(null,
								MainFrame.board.getCurrentPlayer().getOpponent().getAlliance()
										+ " wins the game because his opponent has just ran out of time. Congratulations!"
										+ " Do you want to play again?",
								"Run out of time", JOptionPane.YES_NO_CANCEL_OPTION);
						if (answer == JOptionPane.YES_OPTION) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									parent.showNewGameDialog();
								}
							});
						} else {
							System.exit(0);
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
		});
		if (array[3].equals("yes")) {
			useClock = true;
			t.start();
		} else {
			useClock = false;
			allowTime = 0;
			t.start();
		}
	}

	public int getBlackTime() {
		if (allowTime != 0)
		{
			return this.blackTime;
		}
		else
		{
			return 180000 ;
		}
	}

	public int getWhiteTime() {
		if (allowTime != 0)
		{
			return this.whiteTime;
		}
		else
		{
			return 180000 ;
		}
	}

	public void undoClock(int whiteTime, int blackTime) {
		if (useClock) {
			this.whiteTime = whiteTime;
			this.blackTime = blackTime;
			tableData[0][0] = convertToTime(whiteTime);
			tableData[0][1] = convertToTime(blackTime);
		}
	}

}
