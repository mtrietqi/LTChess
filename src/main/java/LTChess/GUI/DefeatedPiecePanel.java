package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.google.common.primitives.Ints;

import LTChess.Group.*;
import LTChess.Pieces.*;

public class DefeatedPiecePanel extends JPanel implements java.io.Serializable
{
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private List<Piece> defeatedWhitePieces = new ArrayList<Piece>();
	private List<Piece> defeatedBlackPieces = new ArrayList<Piece>();
	
	public DefeatedPiecePanel()
	{
		this.setLayout(new BorderLayout());
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		upperPanel.setLayout(new GridLayout(8,2));
		lowerPanel.setLayout(new GridLayout(8,2));

		this.add(upperPanel,BorderLayout.SOUTH);
		this.add(lowerPanel,BorderLayout.NORTH);
		this.setPreferredSize(new Dimension(60,80));
	}
	
	public void addDefeatedPiece(Piece piece)
	{
		if (piece.getPieceGroup() == Group.WHITE)
		{
			defeatedWhitePieces.add(piece);
			Collections.sort(defeatedWhitePieces,new Comparator<Piece>() {
				@Override
				public int compare(Piece piece1,Piece piece2)
				{
					return piece1.getPieceType().getValue()- piece2.getPieceType().getValue();
				}
			});
			
		}
		else
		{
			defeatedBlackPieces.add(piece);
			Collections.sort(defeatedBlackPieces,new Comparator<Piece>() {
				@Override
				public int compare(Piece piece1,Piece piece2)
				{
					return piece1.getPieceType().getValue()- piece2.getPieceType().getValue();
				}
			});
		}
		drawDefeatedPanel();
	}

	public void drawDefeatedPanel() 
	{
		lowerPanel.removeAll();
		upperPanel.removeAll();
		for (Piece piece:defeatedWhitePieces)
		{
			String pieceIconPath = "resource/";
			try {
				BufferedImage image = ImageIO.read(new File(pieceIconPath 
						+ piece.getPieceGroup().toString().substring(0,1)+
						piece.toString()+".png"));
				ImageIcon ic = new ImageIcon(image);
				JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 20, ic.getIconWidth() - 20, Image.SCALE_SMOOTH)));
				upperPanel.add(imageLabel);
				validate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		for (Piece piece:defeatedBlackPieces)
		{
			String pieceIconPath = "resource/";
			try {
				BufferedImage image = ImageIO.read(new File(pieceIconPath 
						+ piece.getPieceGroup().toString().substring(0,1)+
						piece.toString()+".png"));
				ImageIcon ic = new ImageIcon(image);
				JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 20, ic.getIconWidth() - 20, Image.SCALE_SMOOTH)));
				lowerPanel.add(imageLabel);
				validate();
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		validate();

	}
	
	public void reset()
	{
		upperPanel.removeAll();
		lowerPanel.removeAll();
		defeatedWhitePieces = new ArrayList<Piece>();
		defeatedBlackPieces = new ArrayList<Piece>();
		this.repaint();
	}

	public List<Piece> getDefeatedWhitePieces() {
		return defeatedWhitePieces;
	}

	public void setDefeatedWhitePieces(List<Piece> defeatedWhitePieces) {
		this.defeatedWhitePieces = defeatedWhitePieces;
	}

	public List<Piece> getDefeatedBlackPieces() {
		return defeatedBlackPieces;
	}

	public void setDefeatedBlackPieces(List<Piece> defeatedBlackPieces) {
		this.defeatedBlackPieces = defeatedBlackPieces;
	}
	
	public String toString()
	{
		return "white: "+defeatedWhitePieces.size()+" black: "+defeatedBlackPieces.size()+"\n";
	}
	
	
}
