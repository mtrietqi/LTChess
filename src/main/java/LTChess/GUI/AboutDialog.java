package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */

	/**   
	 * Create the dialog.
	 */
	public AboutDialog() {
		setResizable(false);
		setTitle("About");
		setBounds(100, 100, 439, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblLtchess = new JLabel("LTChess Project 2017");
			lblLtchess.setFont(new Font("Times New Roman", Font.BOLD, 15));
			lblLtchess.setHorizontalAlignment(SwingConstants.CENTER);
			lblLtchess.setBounds(10, 11, 403, 24);
			contentPanel.add(lblLtchess);
		}
		
		JLabel lblStudentsNguyenBao = new JLabel("Students: Nguyen Bao Hoang Long, Ta Minh Triet");
		lblStudentsNguyenBao.setBounds(20, 69, 393, 14);
		contentPanel.add(lblStudentsNguyenBao);
		{
			JLabel lblInstructorBuiTien = new JLabel("Instructor: Bui Tien Len");
			lblInstructorBuiTien.setBounds(20, 48, 359, 14);
			contentPanel.add(lblInstructorBuiTien);
		}
		{
			JLabel imgLabel = new JLabel("");
			imgLabel.setBounds(318, 17, 78, 66);
			BufferedImage img = null;
			try {
			    img = ImageIO.read(new File("resource/logo.png"));
				Image imgResize = img.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(),
				        Image.SCALE_SMOOTH);
				ImageIcon imageIcon = new ImageIcon(imgResize);
				imgLabel.setIcon(imageIcon);
				contentPanel.add(imgLabel);
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
