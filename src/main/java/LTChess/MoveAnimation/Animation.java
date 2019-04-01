package LTChess.MoveAnimation;

import java.awt.geom.Point2D;

public class Animation {
	public static Point2D.Double[] Tween(Point2D.Double[] s,Point2D.Double[] d,int n,double t)
	{
		Point2D.Double[] p = new Point2D.Double[n];
		for(int i=0;i<n;i++)
		{
			p[i]= new Point2D.Double();
			p[i].x=(1-t)*s[i].x+t*d[i].x;
			p[i].y=(1-t)*s[i].y+t*d[i].y;
		}
		return p;
	}

}
