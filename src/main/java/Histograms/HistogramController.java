package Histograms;

import Editor.GUI.GUIComponents.ResizableJPanel.ResizableJPanel;
import Editor.ImageControllers.ImageProxy;

import java.util.Observable;
import java.util.Observer;

public class HistogramController extends Hist implements Observer {
    private Thread t;

    public HistogramController(ResizableJPanel guiHistogramPanel, ImageProxy imageProxy) {
        super(guiHistogramPanel, imageProxy);
        guiHistogramPanel.addResizeListener(a -> {
            preferredSize = guiHistogramPanel.getPreferredSize();
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }
}
