package Histograms;

import Editor.GUI.GUIComponents.ResizableJPanel.ResizableJPanel;
import Editor.ImageControllers.ImageProxy;
import java.util.Observable;
import java.util.Observer;

public class HistogramController extends Histogram implements Observer {
    private Thread t;
    private boolean updateEnabled = false;

    public HistogramController(ResizableJPanel guiHistogramPanel, ImageProxy imageProxy) {
        super(guiHistogramPanel, imageProxy);
        guiHistogramPanel.addResizeListener(a -> {
            resizeHistograms();
        });
    }

    public HistogramController(ResizableJPanel guiHistogramPanel, ImageProxy imageProxy, boolean updateEnabled){
        this(guiHistogramPanel, imageProxy);
        updateEnable(updateEnabled);
    }

    public void updateEnable(boolean updateEnabled){
        this.updateEnabled = updateEnabled;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(updateEnabled && (t==null || !t.isAlive())){
            t = new Thread(this);
            t.start();
        }
    }
}
