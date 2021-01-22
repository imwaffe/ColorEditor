package Histograms;

import Editor.ImageControllers.ImageProxy;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.Raster;
import java.util.ArrayList;

public class Hist implements Runnable {
    private final static int BINS  = 256;

    protected JPanel histogramPanel = new JPanel();
    protected final ImageProxy imageProxy;

    protected final JPanel parentPanel;

    protected SimplerHistogramDataset reds = new SimplerHistogramDataset("Red");
    protected SimplerHistogramDataset greens = new SimplerHistogramDataset("Green");
    protected SimplerHistogramDataset blues = new SimplerHistogramDataset("Blue");

    protected ArrayList<JFreeChart> charts = new ArrayList<>();
    protected ArrayList<JPanel> histogramsPanels = new ArrayList<>();

    public Hist(JPanel parentPanel, ImageProxy imageProxy){
        this.parentPanel = parentPanel;
        parentPanel.add(histogramPanel);

        this.imageProxy = imageProxy;
        for(int i=0; i<BINS; i+=2){
            reds.addBin(new SimpleHistogramBin(i, i+1));
            greens.addBin(new SimpleHistogramBin(i, i+1));
            blues.addBin(new SimpleHistogramBin(i, i+1));
        };
        charts.add(createChart(reds));
        charts.add(createChart(greens));
        charts.add(createChart(blues));

        histogramPanel.setLayout(new BoxLayout(histogramPanel,BoxLayout.Y_AXIS));

        for(int i=0; i<charts.size(); i++) {
            histogramsPanels.add(getPanel(charts.get(i)));
            histogramPanel.add(histogramsPanels.get(i));
        }

        resizeHistograms(parentPanel.getPreferredSize());
    }

    private void refreshImage() throws CloneNotSupportedException {
        int w = imageProxy.getScaledOutputImage().getWidth()/2;
        int h = imageProxy.getScaledOutputImage().getHeight()/2;
        double size = w*h;
        Raster raster = imageProxy.getScaledOutputImage().getSubimage(0,0,w,h).getRaster();
        double values[] = new double[w*h];
        reds.setValues(raster.getSamples(0,0,w,h,0,values));
        blues.setValues(raster.getSamples(0,0,w,h,1,values));
        greens.setValues(raster.getSamples(0,0,w,h,2,values));
    }

    public void resizeHistograms(Dimension preferredSize){
        preferredSize = new Dimension((int)preferredSize.getWidth(), (int)preferredSize.getHeight()/3);
        for(JPanel panel : histogramsPanels)
            panel.setPreferredSize(preferredSize);
        histogramPanel.revalidate();
        histogramPanel.repaint();
    }
    public void resizeHistograms(){
        resizeHistograms(parentPanel.getPreferredSize());
    }

    private static JFreeChart createChart(SimpleHistogramDataset dataset){
        JFreeChart chart = ChartFactory.createXYBarChart(
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
    public void run() {
        try {
            refreshImage();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        histogramPanel.revalidate();
        histogramPanel.repaint();
    }
}
