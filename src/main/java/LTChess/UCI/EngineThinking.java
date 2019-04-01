package LTChess.UCI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import LTChess.GUI.BoardPanel;

import javax.swing.JLabel;

public class EngineThinking extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public EngineThinking() {
		setTitle("Thinking...");
		setAlwaysOnTop(true);
		setBounds(100, 100, 286, 177);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblTheEngineIs = new JLabel("The engine is thinking....");
		lblTheEngineIs.setBounds(65, 114, 147, 14);
		contentPanel.add(lblTheEngineIs);

	    Icon icon = new ImageIcon("resource/cool.gif");
		JLabel imgLabel = new JLabel(icon);
		imgLabel.setBounds(80, 11, 107, 98);
		contentPanel.add(imgLabel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
}
