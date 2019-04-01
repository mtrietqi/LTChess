package LTChess.Network;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import LTChess.GUI.MainFrame;
import LTChess.Network.NetworkServer.HandlerServer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NewGameOModeDialogClient extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtIp;
	private JTextField txtPort;
	
	private MainFrame parent;
	private int portNum;
	private JTextField txtName;
	
	
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public NewGameOModeDialogClient(MainFrame parent) {
		setTitle("New Game Dialog (O Mode) - Client");
		this.parent = parent;
		setBounds(100, 100, 356, 197);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(16, 24, 83, 14);
		contentPanel.add(lblIpAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(16, 57, 46, 14);
		contentPanel.add(lblPort);
		
		txtIp = new JTextField();
		txtIp.setBounds(87, 21, 232, 20);
		contentPanel.add(txtIp);
		txtIp.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("8189");
		txtPort.setBounds(86, 54, 61, 20);
		contentPanel.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(16, 90, 46, 14);
		contentPanel.add(lblName);
		
		txtName = new JTextField();
		txtName.setBounds(87, 87, 162, 20);
		contentPanel.add(txtName);
		txtName.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (txtIp == null || txtIp.equals(""))
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogClient.this,
									"The IP cannot be empty",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						else if (txtPort.getText() == null || txtPort.getText().equals(""))
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogClient.this,
									"The Port cannot be empty",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						if (txtName == null || txtName.equals(""))
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogClient.this,
									"The Name cannot be empty",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						try
						{
							portNum = Integer.parseInt(txtPort.getText());
						}
						catch (NumberFormatException e2)
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogClient.this,
									"The Port cannot contain character","Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						NetworkClient networkClient = new NetworkClient(txtIp.getText(), portNum,1, parent, null, NewGameOModeDialogClient.this);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		this.setVisible(true);
	}
}
