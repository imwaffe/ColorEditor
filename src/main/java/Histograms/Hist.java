package Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Hist {
    public final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    public final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    public final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)
    private final static int BINS  = 256;

    private HistogramDataset reds = new HistogramDataset();
    private HistogramDataset blues = new HistogramDataset();
    private HistogramDataset greens = new HistogramDataset();
    private Dimension preferredSize;

    public Hist(Dimension preferredSize){
        this.preferredSize = preferredSize;
    }
    public Hist(Dimension preferredSize, BufferedImage inputImage){
        this(preferredSize);
        loadImage(inputImage);
    }

    public void loadImage(BufferedImage img){
        Raster raster = img.getRaster();
        int w = img.getWidth();
        int h = img.getHeight();
        double[] values = new double[w*h];
        values = raster.getSamples(0, 0, w, h, 0, values);
        reds.addSeries("Red", values, BINS);
        values = raster.getSamples(0, 0, w, h, 1, values);
        greens.addSeries("Green", values, BINS);
        values = raster.getSamples(0, 0, w, h, 2, values);
        blues.addSeries("Blue", values, BINS);
    }

    private static JFreeChart createChart(HistogramDataset dataset){
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
}
