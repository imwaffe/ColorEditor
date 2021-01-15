package Editor.GUI.Controllers;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class GuiObserver extends GUI implements Observer{
    private final ImageProxy imageProxy;

    public GuiObserver(ImageProxy imageProxy) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super();
        this.imageProxy = imageProxy;
    }

    @Override
    public void update(Observable o, Object arg) {
        super.setImage(imageProxy.getScaledOutputImage());
        System.gc();
    }
}
