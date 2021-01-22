package Histograms;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class SimplerHistogramDataset extends SimpleHistogramDataset {
    /**
     * Creates a new histogram dataset.  Note that the
     * {@code adjustForBinSize} flag defaults to {@code true}.
     *
     * @param key the series key ({@code null} not permitted).
     */
    public SimplerHistogramDataset(Comparable key) {
        super(key);
    }

    public void setValues(double[] values){
        super.clearObservations();
        super.addObservations(values);
    }
}
