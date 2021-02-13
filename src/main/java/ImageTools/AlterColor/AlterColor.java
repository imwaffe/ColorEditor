package ImageTools.AlterColor;

import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class AlterColor extends Observable {
    public abstract void increaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract void decreaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract BufferedImage alterImage(BufferedImage input);
    public abstract String toStringCoeff();
    public abstract void reset();

    /** Returns a BufferedImage given an int[] RGB raster. If no size is given, the size of displayed image is used. */
    public static BufferedImage getBufferedImage(int[] rgbRaster, int width, int height){
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.setRGB(0,0, width, height,rgbRaster,0, width);
        return buffImg;
    }
}
