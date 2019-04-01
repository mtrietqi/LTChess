package LTChess.Network;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;
import LTChess.Group.Group;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;

public class NewGameOModeDialogServer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MainFrame parent;
	private JSpinner minSpinner;
	private JCheckBox enableClock;
	private JSpinner secondSpinner; 
	private JButton okButton;
	private JComboBox<?> firstMoverCombo;
	private JLabel lblName;
	private JTextField txtUserName;
	private JLabel lblGroup;
	private JComboBox groupCombo;
	private static ServerWaiting serverWaiting;
	private JLabel lblPort;
	private JTextField txtPort;
	
	private int port;
	private String name;
	private Group firstMoverGroup;
	private Group sideGroup;
	private int minTime;
	private int secondTime;
	public  NetworkServer server;
	
	public NewGameOModeDialogServer(MainFrame parent) {
		setResizable(false);
		this.parent = parent;
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("New Game Dialog (O Mode) - Server");
		setBounds(100, 100, 364, 231);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		enableClock = new JCheckBox("Enable clock");
		enableClock.setBounds(13, 105, 185, 23);
		enableClock.setSelected(true);
		enableClock.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableClock.isSelected())
				{
					minSpinner.setEnabled(true);
					secondSpinner.setEnabled(true);
				}
				else
				{
					minSpinner.setEnabled(false);
					secondSpinner.setEnabled(false);
				}
			}
		});
		contentPanel.setLayout(null);
		contentPanel.add(enableClock);
		
		minSpinner = new JSpinner();
		minSpinner.setBounds(90, 133, 42, 20);
		minSpinner.setModel(new SpinnerNumberModel(5, 0, 60, 1));
		contentPanel.add(minSpinner);
		
		secondSpinner = new JSpinner();
		secondSpinner.setBounds(158, 133, 42, 20);
		secondSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		contentPanel.add(secondSpinner);
		
		JLabel lblSetTime = new JLabel("Set time:");
		lblSetTime.setBounds(15, 136, 71, 14);
		contentPanel.add(lblSetTime);
		
		JLabel label = new JLabel(":");
		label.setBounds(142, 136, 46, 14);
		contentPanel.add(label);
		
		JLabel lblFirstMover = new JLabel("First mover:");
		lblFirstMover.setBounds(13, 82, 71, 14);
		contentPanel.add(lblFirstMover);
		
		firstMoverCombo = new JComboBox();
		firstMoverCombo.setBounds(93, 76, 107, 20);
		firstMoverCombo.setModel(new DefaultComboBoxModel(Group.values()));
		contentPanel.add(firstMoverCombo);
		
		lblName = new JLabel("Name:");
		lblName.setBounds(13, 11, 46, 14);
		contentPanel.add(lblName);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(91, 8, 142, 20);
		contentPanel.add(txtUserName);
		txtUserName.setColumns(10);
		
		lblGroup = new JLabel("Group:");
		lblGroup.setBounds(13, 48, 76, 14);
		contentPanel.add(lblGroup);
		
		groupCombo = new JComboBox();
		groupCombo.setModel(new DefaultComboBoxModel(Group.values()));
		groupCombo.setBounds(93, 41, 107, 20);
		contentPanel.add(groupCombo);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(243, 11, 46, 14);
		contentPanel.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setText("8189");
		txtPort.setBounds(278, 8, 42, 20);
		contentPanel.add(txtPort);
		txtPort.setColumns(10);
		
		
		JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						MainFrame.board.customBoardMode = false;
						//Set the engine based on engineComboBox
						firstMoverGroup = (Group)firstMoverCombo.getSelectedItem();
						sideGroup = (Group)groupCombo.getSelectedItem();
						
						name = txtUserName.getText();
						if (!enableClock.isSelected())
						{
							minTime = 0;
							secondTime = 0;
						}
						else
						{
							minTime = (int) minSpinner.getValue();
							secondTime = (int) secondSpinner.getValue();
						}
						//Check the values which are entered by user
						if (name == null || name.equals(""))
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogServer.this,
									"The Name cannot be empty",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						try
						{
							port = Integer.parseInt(txtPort.getText());
						}
						catch (NumberFormatException e1)
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogServer.this,
									"The Port cannot contain characters",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						if (port < 8000)
						{
							JOptionPane.showMessageDialog(NewGameOModeDialogServer.this,
									"The Port has to be greater than 8000",
									"Error", JOptionPane.ERROR_MESSAGE);
							return ;
						}
						serverWaiting = new ServerWaiting(parent, NewGameOModeDialogServer.this);
						NewGameOModeDialogServer.this.setVisible(false);
						try {
							server = new NetworkServer(name,port, parent);
							NetworkClient networkClient = new NetworkClient("localhost", port,0,parent, NewGameOModeDialogServer.this,null);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
		
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
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			
			}
		});
		setVisible(true);
	}
	
	
	public boolean isClockEnabled()
	{
		return enableClock.isSelected();
	}


	public JCheckBox getEnableClock() {
		return enableClock;
	}


	public int getPort() {
		return port;
	}


	public String getUserName() {
		return name;
	}


	public Group getFirstMoverGroup() {
		return firstMoverGroup;
	}
	
	public ServerWaiting getServerWaiting()
	{
		return this.serverWaiting;
	}
	
	public void destroyServer()
	{
		server.stopServer();
	}
	
	public boolean isEnoughClient()
	{
		return NetworkServer.isEnoughClient();
	}


	public void stopServer() 
	{
		server.stopServer();
	}
	
	public void stopServerWaiting()
	{
		serverWaiting.dispose();
		NewGameOModeDialogServer.this.dispose();
	}

	public String getMinTime() {
		return Integer.toString(minTime);
	}
	
	public String getSecondTime(){
		return Integer.toString(secondTime);
	}
	
	public Group getSideGroup()
	{
		return sideGroup;
	}
	
}
