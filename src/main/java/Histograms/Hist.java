package Histograms;

import Editor.ImageControllers.ImageProxy;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Observable;
import java.util.Observer;

public class Hist implements Observer {
    public final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    public final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    public final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)
    private final static int BINS  = 256;

    private JPanel histogramPanel = new JPanel();
    private final ImageProxy imageProxy;
    private Dimension preferredSize = new Dimension(150,150);

    private SimpleHistogramDataset reds = new SimpleHistogramDataset("Red");
    private SimpleHistogramDataset greens = new SimpleHistogramDataset("Green");
    private SimpleHistogramDataset blues = new SimpleHistogramDataset("Blue");

    public Hist(JPanel guiHistogramPanel, ImageProxy imageProxy){
        guiHistogramPanel.add(histogramPanel);
        this.imageProxy = imageProxy;
        for(int i=0; i<BINS; i+=2){
            reds.addBin(new SimpleHistogramBin(i, i+1));
            greens.addBin(new SimpleHistogramBin(i, i+1));
            blues.addBin(new SimpleHistogramBin(i, i+1));
        };
        //preferredSize = guiHistogramPanel.getPreferredSize();
    }

    public void loadImage(BufferedImage img){
        Raster raster = img.getRaster();
        int w = img.getWidth();
        int h = img.getHeight();
        double[] values = new double[w*h];
        values = raster.getSamples(0, 0, w, h, 0, values);
        reds.clearObservations();
        reds.addObservations(values);
        values = raster.getSamples(0, 0, w, h, 1, values);
        greens.clearObservations();
        greens.addObservations(values);
        values = raster.getSamples(0, 0, w, h, 2, values);
        blues.clearObservations();
        blues.addObservations(values);
    }

    private static JFreeChart createChart(SimpleHistogramDataset dataset){
        final JFreeChart chart = ChartFactory.createXYBarChart(
                (String) dataset.getSeriesKey(0),
                "X",
                false,
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;
    }

    private JPanel getPanel(JFreeChart chart){
        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(preferredSize);
        return cp;
    }

    public JPanel getRed(){
        return getPanel(createChart(reds));
    }
    public JPanel getGreen(){
        return getPanel(createChart(greens));
    }
    public JPanel getBlues(){
        return getPanel(createChart(blues));
    }

    public static int[] getChannelRaster(int[] img, int channel){
        int[] outputRaster = new int[img.length/3];

        for(int i=0; i<outputRaster.length; i++) {
            outputRaster[i] = (img[i]>>channel) & 0xFF;
        }
        return outputRaster;
    }

    @Override
    public void update(Observable o, Object arg) {
        loadImage(imageProxy.getScaledOutputImage());
        JPanel newPanel = getPanel(createChart(reds));
        newPanel.setPreferredSize(new Dimension(150,150));
        newPanel.setBackground(new Color(255,0,0));
        try {
            histogramPanel.remove(0);
        } catch (ArrayIndexOutOfBoundsException e){};
        histogramPanel.add(newPanel);
        histogramPanel.revalidate();
        histogramPanel.repaint();
    }
}
