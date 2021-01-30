package Histograms;

import Editor.ImageControllers.ImageProxy;
import Histograms.ImageHistogramDataset.ImageHistogramDataset;
import Histograms.ImageHistogramDataset.SimpleImageHistogramBin;
import ImageTools.ImageScaler;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;

public class Histogram implements Runnable {
    private final static int BINS  = 256;

    protected JPanel histogramPanel = new JPanel();
    protected final ImageProxy imageProxy;

    protected final JPanel parentPanel;

    protected ImageHistogramDataset reds = new ImageHistogramDataset("Red");
    protected ImageHistogramDataset greens = new ImageHistogramDataset("Green");
    protected ImageHistogramDataset blues = new ImageHistogramDataset("Blue");

    protected ArrayList<JFreeChart> charts = new ArrayList<>();
    protected ArrayList<JPanel> histogramsPanels = new ArrayList<>();

    public Histogram(JPanel parentPanel, ImageProxy imageProxy){
        this.parentPanel = parentPanel;
        parentPanel.add(histogramPanel);

        this.imageProxy = imageProxy;
        for(int i = 1; i<BINS; i++){
            reds.addBin(new SimpleImageHistogramBin(i));
            greens.addBin(new SimpleImageHistogramBin(i));
            blues.addBin(new SimpleImageHistogramBin(i));
        };
        charts.add(createChart(reds));
        charts.add(createChart(greens));
        charts.add(createChart(blues));

        histogramPanel.setLayout(new BoxLayout(histogramPanel,BoxLayout.Y_AXIS));

        Paint[] paintArray = {
                new Color(0x80ff0000, true),
                new Color(0x8000ff00, true),
                new Color(0x800000ff, true)
        };

        for(int i=0; i<charts.size(); i++) {
            Paint[] pa = {paintArray[i]};
            XYPlot plot = (XYPlot) charts.get(i).getPlot();
            XYBarRenderer renderer;
            renderer = (XYBarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardXYBarPainter());
            plot.setDrawingSupplier(new DefaultDrawingSupplier(
                    pa,
                    pa,
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                    DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));

            histogramsPanels.add(getPanel(charts.get(i)));
            histogramPanel.add(histogramsPanels.get(i));
        }

        resizeHistograms(parentPanel.getPreferredSize());
    }

    private void refreshImage(BufferedImage loadedImage) {
        int w = loadedImage.getWidth();
        int h = loadedImage.getHeight();
        int size = w*h;
        Raster raster = loadedImage.getRaster();
        double values[] = new double[size];
        reds.setNormalizationFactor(size/100);
        greens.setNormalizationFactor(size/100);
        blues.setNormalizationFactor(size/100);
        reds.setValues(raster.getSamples(0,0,w,h,0,values));
        greens.setValues(raster.getSamples(0,0,w,h,1,values));
        blues.setValues(raster.getSamples(0,0,w,h,2,values));
    }

    public void resizeHistograms(Dimension preferredSize){
        preferredSize = new Dimension((int)preferredSize.getWidth()-10, (int)preferredSize.getHeight()/3);
        for(JPanel panel : histogramsPanels)
            panel.setPreferredSize(preferredSize);
        histogramPanel.revalidate();
        histogramPanel.repaint();
    }
    public void resizeHistograms(){
        resizeHistograms(parentPanel.getPreferredSize());
    }

    private static JFreeChart createChart(ImageHistogramDataset dataset){
        JFreeChart chart = ChartFactory.createHistogram(
                (String) dataset.getSeriesKey(0),
                null,
                "%",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        return chart;
    }

    private JPanel getPanel(JFreeChart chart){
        ChartPanel cp = new ChartPanel(chart,false,true,false,false,false);
        return cp;
    }

    public void calculateHighResolutionHistogram(){
        refreshImage(imageProxy.getFullscaleOutputImage());
    }

    @Override
    public void run() {
        refreshImage(ImageScaler.resizeImage(imageProxy.getScaledOutputImage(),new Dimension(100,100)));
    }
}
