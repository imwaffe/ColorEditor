package Editor.GUI.Controllers;

import Editor.GUI.Controllers.FileController.FileController;
import Editor.GUI.GUI;
import Editor.GUI.GUIComponents.Menu.ModeMenu;
import Editor.ImageControllers.ImageProxy;
import ImageTools.AlterColor.AlterColorStrategy;
import ImageTools.AlterColor.AlterLMS.AlterLMSWhite;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class GUIObserver extends GUI implements Observer{
    private ImageProxy imageProxy;

    public void setImageProxy(ImageProxy ip) {
        imageProxy = ip;
    }

    public GUIObserver(ImageProxy imageProxy) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super();
        this.imageProxy = imageProxy;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof FileController && ModeMenu.getInstance().whiteRef()) {
            AlterColorStrategy.getInstance(new AlterLMSWhite(imageProxy));
        }
        super.setImage(imageProxy.getScaledOutputImage());
        System.gc();
    }
}
