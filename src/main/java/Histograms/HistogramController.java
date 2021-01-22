package Histograms;

import Editor.GUI.Controllers.FileController.FileController;
import Editor.GUI.GUIComponents.ResizableJPanel.ResizableJPanel;
import Editor.ImageControllers.ImageProxy;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class HistogramController extends Hist implements Observer {
    private Thread t;

    public HistogramController(ResizableJPanel guiHistogramPanel, ImageProxy imageProxy) {
        super(guiHistogramPanel, imageProxy);
        guiHistogramPanel.addResizeListener(a -> {
            resizeHistograms();
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof FileController) {
            resizeHistograms(parentPanel.getPreferredSize());
            System.out.println(parentPanel.getPreferredSize().toString());
        }
        if(t==null || !t.isAlive()){
            t = new Thread(this);
            t.start();
        }
    }
}
