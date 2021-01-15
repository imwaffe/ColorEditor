package Editor.GUI.Controllers;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import java.util.Observable;
import java.util.Observer;

public class GuiObserver implements Observer{
    private final GUI gui;
    private final ImageProxy imageProxy;

    public GuiObserver(GUI gui, ImageProxy imageProxy){
        this.gui = gui;
        this.imageProxy = imageProxy;
    }

    @Override
    public void update(Observable o, Object arg) {
        gui.setImage(imageProxy.getScaledOutputImage());
        System.gc();
    }
}
