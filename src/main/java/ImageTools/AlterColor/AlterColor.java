package ImageTools.AlterColor;

import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class AlterColor extends Observable {
    public abstract void increaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract void decreaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract BufferedImage alterImage(BufferedImage input);
    public abstract String toStringCoeff();
    public abstract void reset();
}
