package Histograms;

import Editor.ImageControllers.ImageProxy;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HistCharts implements Observer {
    private List<XYChart> charts = new ArrayList<>();
    private final static int CHANNELS_NUMBER = 3;
    private final static double CHANNEL_COLOR_LVLS = 256;

    private JPanel histogramPanel = new JPanel();
    private final ImageProxy imageProxy;

    public HistCharts(JPanel guiHistogramPanel, ImageProxy imageProxy){
        this.imageProxy = imageProxy;
        guiHistogramPanel.add(histogramPanel);

        for (int i = 0; i < CHANNELS_NUMBER; i++) {
            XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(600).height(400).build();
            chart.getStyler().setYAxisMin(0.0);
            chart.getStyler().setYAxisMax(CHANNEL_COLOR_LVLS-1);
            charts.add(chart);
        }
        charts.get(0).setTitle("RED");
        charts.get(0).getStyler().setSeriesColors(new Color[]{ Color.RED } );
        charts.get(1).setTitle("GREEN");
        charts.get(1).getStyler().setSeriesColors(new Color[]{ Color.GREEN } );
        charts.get(2).setTitle("BLUE");
        charts.get(2).getStyler().setSeriesColors(new Color[]{ Color.BLUE } );
    }

    public void loadImage(BufferedImage img){
        Raster raster = img.getRaster();
        int w = img.getWidth();
        int h = img.getHeight();
        int[] values = new int[w*h];
        for(int i=0; i<CHANNELS_NUMBER; i++){
            values = raster.getSamples(0, 0, w, h, i, values);
            charts.get(i).addSeries("val", values);
        }
    }

    public XChartPanel getR(){
        return new XChartPanel(charts.get(0));
    }
    public XChartPanel getG(){
        return new XChartPanel(charts.get(1));
    }
    public XChartPanel getB(){
        return new XChartPanel(charts.get(2));
    }

    @Override
    public void update(Observable o, Object arg) {
        loadImage(imageProxy.getScaledOutputImage());

        try {
            histogramPanel.remove(0);
        } catch (ArrayIndexOutOfBoundsException e){};
        histogramPanel.add(getR());
        histogramPanel.revalidate();
        histogramPanel.repaint();
    }
}
