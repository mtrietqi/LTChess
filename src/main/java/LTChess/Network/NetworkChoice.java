package LTChess.Network;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import LTChess.GUI.MainFrame;

public class NetworkChoice extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public NetworkChoice(MainFrame parent) {
		setTitle("Choose a role");
		setBounds(100, 100, 354, 115);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton btnServer = new JButton("Start as server");
		btnServer.setBounds(21, 11, 138, 53);
		btnServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.board.customBoardMode = false;
				NetworkChoice.this.dispose();
				NewGameOModeDialogServer oModeServer = new NewGameOModeDialogServer(parent);
			}
		});
		contentPanel.add(btnServer);
		
		JButton btnClient = new JButton("Start as client");
		btnClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				NetworkChoice.this.dispose();
				NewGameOModeDialogClient oModeClient = new NewGameOModeDialogClient(parent);
			}
		});
		btnClient.setBounds(179, 11, 138, 53);
		contentPanel.add(btnClient);
		
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
}
