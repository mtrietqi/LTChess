package LTChess.Network;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;

import javax.swing.JLabel;

public class ServerWaiting extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private NewGameOModeDialogServer serverDialog;
	
	private String ipAddress;
	/**
	 * Create the dialog.
	 */
	public ServerWaiting(MainFrame parent, NewGameOModeDialogServer serverDialog ) {
		setTitle("Waiting...");
		setAlwaysOnTop(true);
		this.serverDialog = serverDialog;
		setBounds(100, 100, 286, 252);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblTheEngineIs = new JLabel("Waiting for someone to join...");
		lblTheEngineIs.setBounds(45, 145, 186, 14);
		contentPanel.add(lblTheEngineIs);

	    Icon icon = new ImageIcon("resource/ServerWaiting.gif");
		JLabel imgLabel = new JLabel(icon);
		imgLabel.setBounds(82, 11, 125, 125);
		contentPanel.add(imgLabel);
		
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel lblIpAddress = new JLabel("IP Address: "+ipAddress);
		lblIpAddress.setBounds(45, 167, 170, 14);
		contentPanel.add(lblIpAddress);
		
		JLabel lblPort = new JLabel("Port: "+serverDialog.getPort());
		lblPort.setBounds(87, 185, 96, 14);
		contentPanel.add(lblPort);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				ServerWaiting.this.dispose();
				//If user decides to stop server while it doesn't have any client to join
				if (!serverDialog.isEnoughClient())
				{
					serverDialog.stopServer();
					serverDialog.setVisible(true);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		setVisible(true);
	}
}
