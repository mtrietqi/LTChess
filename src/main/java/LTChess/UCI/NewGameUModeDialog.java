package LTChess.UCI;

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

public class NewGameUModeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MainFrame parent;
	private JSpinner minSpinner;
	private JCheckBox enableClock;
	private JSpinner secondSpinner; 
	private JComboBox levelComboBox;
	private JButton okButton;
	public static UCI uci;
	private JComboBox<?> firstMoverCombo;
	
	public NewGameUModeDialog(MainFrame parent) {
		setResizable(false);
		this.parent = parent;
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("New Game (U mode)");
		setBounds(100, 100, 392, 218);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblOpponent = new JLabel("Level:");
		lblOpponent.setBounds(13, 24, 71, 14);
		contentPanel.add(lblOpponent);
		
		levelComboBox = new JComboBox();
		levelComboBox.setModel(new DefaultComboBoxModel(UCILevel.values()));
		levelComboBox.setBounds(91, 21, 107, 20);
		contentPanel.add(levelComboBox);
		
		enableClock = new JCheckBox("Enable clock");
		enableClock.setSelected(true);
		enableClock.setBounds(11, 83, 185, 23);
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
		minSpinner.setBounds(88, 111, 42, 20);
		contentPanel.add(minSpinner);
		
		secondSpinner = new JSpinner();
		secondSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		secondSpinner.setBounds(156, 111, 42, 20);
		contentPanel.add(secondSpinner);
		
		JLabel lblSetTime = new JLabel("Set time:");
		lblSetTime.setBounds(13, 114, 71, 14);
		contentPanel.add(lblSetTime);
		
		JLabel label = new JLabel(":");
		label.setBounds(141, 88, 46, 14);
		contentPanel.add(label);
		
		JLabel lblFirstMover = new JLabel("First mover:");
		lblFirstMover.setBounds(13, 60, 71, 14);
		contentPanel.add(lblFirstMover);
		
		firstMoverCombo = new JComboBox();
		firstMoverCombo.setModel(new DefaultComboBoxModel(Group.values()));
		firstMoverCombo.setBounds(91, 52, 107, 20);
		contentPanel.add(firstMoverCombo);
		
		
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
					// Set the engine based on engineComboBox
					uci = new UCI("resource/engine/stockfish_8_x32.exe", (UCILevel) levelComboBox.getSelectedItem());
					Group firstMoverGroup = (Group) firstMoverCombo.getSelectedItem();
					if (parent.board == null || !parent.getBoardPanel().isVisible()) {
						if (enableClock.isSelected()) {
							parent.startNewGame(true, firstMoverGroup, (int) minSpinner.getValue(),
									(int) secondSpinner.getValue(), true, null);
						} else {
							parent.startNewGame(true, firstMoverGroup, -1, -1, true, null);
						}
						dispose();
					}
					// This case means user used to start a game or user used
					// custom board
					else {
						int answer = JOptionPane.showConfirmDialog(null,
								"A game is currently running. Do you really want to create a new game?",
								"Create new game", JOptionPane.YES_NO_CANCEL_OPTION);
						if (answer == JOptionPane.YES_OPTION) {
							parent.prepareToStartNewGame();
							if (enableClock.isSelected()) {
								parent.startAnotherGame(true, firstMoverGroup, (int) minSpinner.getValue(),
										(int) secondSpinner.getValue(), true, null);
							} else {
								parent.startAnotherGame(true, firstMoverGroup, -1, -1, true, null);
							}
							dispose();
						}
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
	
	public UCI getUCI()
	{
		return this.uci;
	}
}
