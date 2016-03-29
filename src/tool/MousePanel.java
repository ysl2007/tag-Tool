package tool;
import java.awt.*;
import java.awt.event.*;
public class MousePanel extends ImgPanel//JPanel
{
	private static final long serialVersionUID = -2541762596249393943L;
	int x_pos,y_pos,x_start,y_start,x_end,y_end,maxX,minX,maxY,minY;
    public int getPointX()
    {
        return (x_start+x_end)/2;
    }
    public int getPointY()
    {
        return (y_start+y_end)/2;
    }
    
    public int getPointLeftTtopX()
    {
        return minX;
    }
    public int getPointLeftTtopY()
    {
        return minY;
    }
    
    public int getPointRightLowX()
    {
        return maxX;
    }
    public int getPointRightLowY()
    {
        return maxY;
    }
    public MousePanel()
    {
        addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {
                x_pos=e.getX();
                y_pos=e.getY();
                //repaint();
            }

            public void mouseEntered(MouseEvent e)
            { }

            public void mouseExited(MouseEvent e)
            { }

            public void mousePressed(MouseEvent e)
            {
                x_start=e.getX();
                y_start=e.getY();
            }

            public void mouseReleased(MouseEvent e) 
            {
                x_end=e.getX();
                y_end=e.getY();
                //System.out.println(x_end);
                //System.out.println(y_end);
                if(y_end>y_start)
                {
                    maxY=y_end;
                    minY=y_start;
                }
                else
                {
                    maxY=y_start;
                    minY=y_end;
                }
                if(x_end>x_start)
                {
                    maxX=x_end;
                    minX=x_start;
                }
                else
                {
                    maxX=x_start;
                    minX=x_end;
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseMoved(MouseEvent e)
            { }
            public void mouseDragged(MouseEvent e){}
        });
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawRect(minX, minY,maxX-minX,maxY-minY);
    }
}