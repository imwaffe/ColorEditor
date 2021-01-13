package ImageTools;

import java.awt.image.BufferedImage;

public interface AlterColor {
    public void increaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public void decreaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public BufferedImage alterImage(BufferedImage input);
    public String toStringCoeff();
}
