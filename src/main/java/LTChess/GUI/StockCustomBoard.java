package LTChess.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import LTChess.Group.*;
import LTChess.Pieces.*;


public class StockCustomBoard extends JPanel
{
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private static List<Piece> whitePieces = new ArrayList<Piece>();
	private static List<Piece> blackPieces = new ArrayList<Piece>();
	
	public static Piece currentFocusPiece = null;
	
	public StockCustomBoard()
	{
		this.setLayout(new BorderLayout());
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		upperPanel.setLayout(new GridLayout(8,2));
		lowerPanel.setLayout(new GridLayout(8,2));

		
		addPiece(new Rook(0, Group.BLACK));
		addPiece(new Knight(1, Group.BLACK));
		addPiece(new Bishop(2, Group.BLACK));
		addPiece(new Queen(3, Group.BLACK));
		addPiece(new Bishop(5, Group.BLACK));
		addPiece(new Knight(6, Group.BLACK));
		addPiece(new Rook(7, Group.BLACK));
		addPiece(new Pawn(8, Group.BLACK));
		addPiece(new Pawn(9, Group.BLACK));
		addPiece(new Pawn(10, Group.BLACK));
		addPiece(new Pawn(11, Group.BLACK));
		addPiece(new Pawn(12, Group.BLACK));
		addPiece(new Pawn(13, Group.BLACK));
		addPiece(new Pawn(14, Group.BLACK));
		addPiece(new Pawn(15, Group.BLACK));
		// White pieces
		addPiece(new Pawn(48, Group.WHITE));
		addPiece(new Pawn(49, Group.WHITE));
		addPiece(new Pawn(50, Group.WHITE));
		addPiece(new Pawn(51, Group.WHITE));
		addPiece(new Pawn(52, Group.WHITE));
		addPiece(new Pawn(53, Group.WHITE));
		addPiece(new Pawn(54, Group.WHITE));
		addPiece(new Pawn(55, Group.WHITE));
		addPiece(new Rook(56, Group.WHITE));
		addPiece(new Knight(57, Group.WHITE));
		addPiece(new Bishop(58, Group.WHITE));
		addPiece(new Queen(59, Group.WHITE));
		addPiece(new Bishop(61, Group.WHITE));
		addPiece(new Knight(62, Group.WHITE));
		addPiece(new Rook(63, Group.WHITE));
		this.add(upperPanel,BorderLayout.SOUTH);
		this.add(lowerPanel,BorderLayout.NORTH);
		this.setPreferredSize(new Dimension(70,80));
	}
	
	public void addPiece(Piece piece)
	{
		if (piece.getPieceGroup() == Group.WHITE)
		{
			whitePieces.add(piece);
			Collections.sort(whitePieces,new Comparator<Piece>() {
				@Override
				public int compare(Piece piece1,Piece piece2)
				{
					return piece1.getPieceType().getValue()- piece2.getPieceType().getValue();
				}
			});
			
		}
		else
		{
			blackPieces.add(piece);
			Collections.sort(blackPieces,new Comparator<Piece>() {
				@Override
				public int compare(Piece piece1,Piece piece2)
				{
					return piece1.getPieceType().getValue()- piece2.getPieceType().getValue();
				}
			});
		}
		drawStockCustomBoardPanel();
	}
	
	public void removeCurrentPiece()
	{
		if (currentFocusPiece.getPieceGroup() == Group.WHITE)
		{
			whitePieces.remove(currentFocusPiece);
		}
		else if (currentFocusPiece.getPieceGroup() == Group.BLACK)
		{
			blackPieces.remove(currentFocusPiece);
		}
		drawStockCustomBoardPanel();
	}

	private void drawStockCustomBoardPanel() 
	{
		lowerPanel.removeAll();
		upperPanel.removeAll();
		for (Piece piece:whitePieces)
		{
			String pieceIconPath = "resource/";
			try {
				BufferedImage image = ImageIO.read(new File(pieceIconPath 
						+ piece.getPieceGroup().toString().substring(0,1)+
						piece.toString()+".png"));
				ImageIcon ic = new ImageIcon(image);
				JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 20, ic.getIconWidth() - 20, Image.SCALE_SMOOTH)));
				imageLabel.addMouseListener(new MouseAdapter(){
						@Override
			            public void mouseClicked(MouseEvent e) {
			               imageLabel.requestFocusInWindow();
			               imageLabel.setBorder(BorderFactory.createEmptyBorder());
			            }
				});
				imageLabel.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						imageLabel.setBorder(BorderFactory.createEmptyBorder());
					}
					
					@Override
					public void focusGained(FocusEvent e) {
						imageLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
						currentFocusPiece = piece;
					}
				});
				upperPanel.add(imageLabel);
				validate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		for (Piece piece : blackPieces) {
			String pieceIconPath = "resource/";
			try {
				BufferedImage image = ImageIO.read(new File(
						pieceIconPath + piece.getPieceGroup().toString().substring(0, 1) + piece.toString() + ".png"));
				ImageIcon ic = new ImageIcon(image);
				JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(ic.getIconWidth() - 20,
						ic.getIconWidth() - 20, Image.SCALE_SMOOTH)));
				imageLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						imageLabel.requestFocusInWindow();
					}
				});
				imageLabel.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent e) {
						// TODO Auto-generated method stub
						imageLabel.setBorder(BorderFactory.createEmptyBorder());
					}

					@Override
					public void focusGained(FocusEvent e) {
						imageLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
						currentFocusPiece = piece;
					}
				});
				lowerPanel.add(imageLabel);
				validate();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void reset()
	{
		upperPanel.removeAll();
		lowerPanel.removeAll();
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		this.repaint();
	}
	
	public Piece getCurrentFocusPiece()
	{
		return this.currentFocusPiece;
	}
	
	public void resetCurrentFocusPiece()
	{
		currentFocusPiece = null;
	}
	
}
