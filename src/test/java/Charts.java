import Editor.GUI.GUI;
import Histograms.HistCharts;
import ImageTools.ImagesList;
import org.junit.Test;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Charts {
    @Test
    public void testChartsCreation(){
        HistCharts charts = new HistCharts();
        assertNotNull(charts.getR());
        assertNotNull(charts.getG());
        assertNotNull(charts.getB());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void acceptsBufferedImage() throws IOException {
        BufferedImage img = ImagesList.toBuffImg(new File("testpic.jpg"));
        HistCharts charts = new HistCharts();
        charts.loadImage(img);
    }

}
