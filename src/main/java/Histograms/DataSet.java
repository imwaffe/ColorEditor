package Histograms;

import org.jfree.data.statistics.SimpleHistogramDataset;

public class DataSet extends SimpleHistogramDataset {
    /**
     * Creates a new histogram dataset.  Note that the
     * {@code adjustForBinSize} flag defaults to {@code true}.
     *
     * @param key the series key ({@code null} not permitted).
     */
    public DataSet(Comparable key) {
        super(key);
    }
}
