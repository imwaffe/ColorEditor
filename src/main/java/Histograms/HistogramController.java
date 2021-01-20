package Histograms;

import Editor.ImageControllers.ImageProxy;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class HistogramController extends Hist implements Observer {
    private Thread t;

    public HistogramController(JPanel guiHistogramPanel, ImageProxy imageProxy) {
        super(guiHistogramPanel, imageProxy);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(t == null){
            t = new Thread(this);
            t.start();
        }
    }
}
