import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Arc
{
	private Point start;
	
	public void setStart(Point start) {
		this.start = start;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	private Point end;
	
	public Arc(Point start, Point end)
	{
		this.start = start;
		this.end = end;
	}
	
	public void drawArc(Graphics g,int diam)
	{
		if (start != null)
		{
            g.setColor(Color.BLACK);
            drawArrowLine(g,start.x,start.y,end.x,end.y,30,5,diam/2);
        }
	}
	
	public void drawArcN(Graphics g,int diam)
	{
		if (start != null)
		{
            g.setColor(Color.BLACK);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
	}
	
	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int l, int w,int diam) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D-l, xn = xm, xp, ym = w, yn = -w, yp, x;
	    double sin = dy / D, cos = dx / D, tetha;
	    
	    double angle1 = Math.atan2(y1 - y2, x1 - x2);
	    double angle2 = Math.atan2(0 , diam);
	    tetha = (angle1-angle2);

	    xp=x2+diam*Math.cos(tetha);
	    yp=y2+diam*Math.sin(tetha);  		
	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;
	    
	    int[] xpoints = {(int)xp, (int) xm, (int) xn};
	    int[] ypoints = {(int)yp, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}
}
