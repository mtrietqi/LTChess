package LTChess.MoveAnimation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import LTChess.ChessBoard.Square;
import LTChess.GUI.BoardPanel;
import LTChess.GUI.MainFrame;
import LTChess.GUI.BoardPanel.SquarePanel;
import LTChess.Pieces.Piece;

public class GlassPane extends JPanel {
	private static final int DELAY = 20;
	private double t = 0;
	private final double delta_t = 0.2;
	private Point2D.Double[] s;
	private Point2D.Double[] d;
	private Point2D[] p;
	private BufferedImage image;
	private Timer timer;
	public static boolean done = false;

	public GlassPane() {
		s = new Point2D.Double[1];
		d = new Point2D.Double[1];
		setLayout(null);
	}

	public void doAnimation(Piece movedPiece, Square source, Square destination, SquarePanel sourceSquarePanel, SquarePanel desSquarePanel) {
		if (sourceSquarePanel != null) 
		{
			t= 0;
			String pieceIconPath = "resource/";
			try {

				image = ImageIO.read(new File(pieceIconPath
						+ MainFrame.board.getSquare(source.getSquareCoordinate()).getPiece().getPieceGroup().toString().substring(0, 1)
						+ MainFrame.board.getSquare(source.getSquareCoordinate()).getPiece().toString() + ".png"));
				s[0]= new Point2D.Double((int) sourceSquarePanel.getLocation().getX()+71,(int) sourceSquarePanel.getLocation().getY()+33);
				d[0] = new Point2D.Double((int) desSquarePanel.getLocation().getX()+71,(int) desSquarePanel.getLocation().getY()+33);
				p= Animation.Tween(s,d,1,t);
				if (timer != null && timer.isRunning())
				{
					timer.stop();
				}
				
				timer = new Timer(DELAY, event-> {
					t+= delta_t;
					if(t>1)
					{
						timer.stop();
						repaint();
						MainFrame.drawBoard();
					};
					p= Animation.Tween(s,d,1,t);
					repaint();
				});
				timer.start();
				p= Animation.Tween(s,d,1,t);
				repaint();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
//		Line2D l= new Line2D.Double(p[0],p[1]);
		if (image != null && p != null  && timer.isRunning())
		{
			g2.drawImage(image, (int) p[0].getX(),(int) p[0].getY(),null);
		}

//		g2.draw(l);
	}
}
