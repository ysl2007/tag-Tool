package tool;

import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImgPanel extends javax.swing.JPanel 
{

    private static final long serialVersionUID = 1L;
    private Image image;
    private int imgWidth;
    private int imgHeight;

    public Image getImg()
    {
        return image;
    }
    public int getImgWidth() 
    {
        return imgWidth;
    }
    public void setImgWidth(int imgWidth) 
    {
        this.imgWidth = imgWidth;
    }
    public int getImgHeight() 
    {
        return imgHeight;
    }
    public void setImgHeight(int imgHeight) 
    {
        this.imgHeight = imgHeight;
    }
    public ImgPanel() 
    {
    }

    public void setImagePath(String imgPath) {
        try 
        {
            image = ImageIO.read(new FileInputStream(imgPath));
            setImgWidth(image.getWidth(this));
            setImgHeight(image.getHeight(this));
            this.repaint();
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }

    }

    @Override
    public void paintComponent(Graphics g1) 
    {
        super.paintComponent(g1);
        int x = 0;
        int y = 0;
        Graphics g = (Graphics) g1;
        if (null == image) 
        {
            return;
        }

        g.drawImage(image, x, y, image.getWidth(this), image.getHeight(this),
            this);			
        g = null;
    }
}