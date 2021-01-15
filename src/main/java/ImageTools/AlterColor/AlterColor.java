package ImageTools.AlterColor;

import Editor.GUI.Controllers.KeyboardController;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public abstract class AlterColor extends Observable {
    public abstract void increaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract void decreaseByOne(boolean ch1, boolean ch2, boolean ch3);
    public abstract BufferedImage alterImage(BufferedImage input);
    public abstract String toStringCoeff();
}
