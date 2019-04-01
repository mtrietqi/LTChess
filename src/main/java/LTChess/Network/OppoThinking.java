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

public class OppoThinking extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private NewGameOModeDialogServer serverDialog;

	/**
	 * Create the dialog.
	 */
	public OppoThinking(MainFrame parent) {
		setTitle("Waiting...");
		setAlwaysOnTop(true);
		setBounds(100, 100, 301, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblTheEngineIs = new JLabel("Waiting for you opponent to make a move...");
		lblTheEngineIs.setBounds(15, 152, 256, 14);
		contentPanel.add(lblTheEngineIs);

	    Icon icon = new ImageIcon("resource/oppo.gif");
		JLabel imgLabel = new JLabel(icon);
		imgLabel.setBounds(75, 8, 118, 132);
		contentPanel.add(imgLabel);
		
	
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
