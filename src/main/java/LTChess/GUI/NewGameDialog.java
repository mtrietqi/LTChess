package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import LTChess.Group.Group;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NewGameDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MainFrame parent;
	private JSpinner minSpinner;
	private JCheckBox enableClock;
	private JSpinner secondSpinner; 
	private JComboBox firstMoverComboBox;
	private JButton okButton;
	
	private EditInitialBoard editBoard;
	public NewGameDialog(MainFrame parent) {
		setResizable(false);
		this.parent = parent;
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("New Game (E mode)");
		setBounds(100, 100, 392, 219);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblFirstMover = new JLabel("First mover:");
			lblFirstMover.setBounds(10, 14, 71, 14);
			contentPanel.add(lblFirstMover);
		}
		{
			firstMoverComboBox = new JComboBox();
			firstMoverComboBox.setModel(new DefaultComboBoxModel(Group.values()));
			firstMoverComboBox.setBounds(88, 11, 107, 20);
			contentPanel.add(firstMoverComboBox);
		}
		
		enableClock = new JCheckBox("Enable clock");
		enableClock.setSelected(true);
		enableClock.setBounds(6, 47, 185, 23);
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
		contentPanel.add(enableClock);
		
		minSpinner = new JSpinner();
		minSpinner.setModel(new SpinnerNumberModel(5, 0, 60, 1));
		minSpinner.setBounds(85, 79, 42, 20);
		contentPanel.add(minSpinner);
		
		secondSpinner = new JSpinner();
		secondSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		secondSpinner.setBounds(153, 79, 42, 20);
		contentPanel.add(secondSpinner);
		
		JLabel lblSetTime = new JLabel("Set time:");
		lblSetTime.setBounds(10, 82, 71, 14);
		contentPanel.add(lblSetTime);
		
		JLabel label = new JLabel(":");
		label.setBounds(138, 82, 46, 14);
		contentPanel.add(label);
		
		JButton btnCustomBoard = new JButton("Custom Board");
		btnCustomBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editBoard = new EditInitialBoard(parent,NewGameDialog.this);
				NewGameDialog.this.setVisible(false);
				parent.gameFrame.setVisible(false);
				editBoard.setVisible(true);
			}
		});
		btnCustomBoard.setBounds(10, 117, 117, 23);
		contentPanel.add(btnCustomBoard);
		{
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
						if (parent.board == null || !parent.getBoardPanel().isVisible()) {
							if (enableClock.isSelected()) {
								parent.startNewGame(true, (Group) firstMoverComboBox.getSelectedItem(),
										(int) minSpinner.getValue(), (int) secondSpinner.getValue(), false, null);
							} else {

								parent.startNewGame(true, (Group) firstMoverComboBox.getSelectedItem(), -1, -1, false,
										null);
							}
							dispose();
						}
						//This case means user used to start a game or user used custom board
						else if (parent.board != null && parent.getBoardPanel().isVisible()){
							int answer = JOptionPane.showConfirmDialog(null,
									"A game is currently running. Do you really want to create a new game?",
									"Create new game", JOptionPane.YES_NO_CANCEL_OPTION);
							if (answer == JOptionPane.YES_OPTION) {
								parent.prepareToStartNewGame();
								if (enableClock.isSelected()) {
									parent.startAnotherGame(true, (Group) firstMoverComboBox.getSelectedItem(),
											(int) minSpinner.getValue(), (int) secondSpinner.getValue(), false, null);
								} else {
									parent.startAnotherGame(true, (Group) firstMoverComboBox.getSelectedItem(), -1, -1,
											false, null);
								}
								dispose();
							}
						}
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
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
	
	public Group getFirstMover()
	{
		return (Group)firstMoverComboBox.getSelectedItem();
	}
	
	public boolean isClockEnabled()
	{
		return enableClock.isSelected();
	}
	
	public void startAnotherGame()
	{
		if (enableClock.isSelected())
		{
			parent.startAnotherGame(false,(Group) firstMoverComboBox.getSelectedItem()
					,(int)minSpinner.getValue(),(int)secondSpinner.getValue(), false, null);
		}
		else
		{
			parent.startAnotherGame(false,(Group) firstMoverComboBox.getSelectedItem()
					,-1,-1, false, null);
		}
		dispose();
	}
}
