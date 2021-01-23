package Histograms.ImageHistogramDataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.util.ObjectUtils;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.PublicCloneable;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class ImageHistogramDataset extends AbstractIntervalXYDataset
        implements IntervalXYDataset, Cloneable, PublicCloneable,
        Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7997996479768018443L;

    /** The series key. */
    private Comparable key;

    /** The bins. */
    private List bins;

    /** The normalization factor (number of pixels) */
    private double normalizationFactor;

    /**
     * Creates a new histogram dataset.  Note that the
     * {@code adjustForBinSize} flag defaults to {@code true}.
     *
     * @param key  the series key ({@code null} not permitted).
     */
    public ImageHistogramDataset(Comparable key) {
        Args.nullNotPermitted(key, "key");
        this.key = key;
        this.bins = new ArrayList();
    }

    /**
     * Set the normalization factor (equals to the total number of pixels)
     *
     * @param normalizationFactor scaling factor to normalize data
     */
    public void setNormalizationFactor(double normalizationFactor){
        this.normalizationFactor = normalizationFactor;
    }

    /**
     * Set the dataset values.
     * First it clears all the observations, then sets the new observations values, then notifies
     * listeners that dataset has changed.
     *
     * @param values new observations
     */
    public void setValues(double[] values){
        this.clearObservations();
        this.addObservations(values);
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * Returns the number of series in the dataset (always 1 for this dataset).
     *
     * @return The series count.
     */
    @Override
    public int getSeriesCount() {
        return 1;
    }

    /**
     * Returns the key for a series.  Since this dataset only stores a single
     * series, the {@code series} argument is ignored.
     *
     * @param series  the series (zero-based index, ignored in this dataset).
     *
     * @return The key for the series.
     */
    @Override
    public Comparable getSeriesKey(int series) {
        return this.key;
    }

    /**
     * Returns the order of the domain (or X) values returned by the dataset.
     *
     * @return The order (never {@code null}).
     */
    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    /**
     * Returns the number of items in a series.  Since this dataset only stores
     * a single series, the {@code series} argument is ignored.
     *
     * @param series  the series index (zero-based, ignored in this dataset).
     *
     * @return The item count.
     */
    @Override
    public int getItemCount(int series) {
        return this.bins.size();
    }

    /**
     * Adds a bin to the dataset.  An exception is thrown if the bin overlaps
     * with any existing bin in the dataset.
     *
     * @param bin  the bin ({@code null} not permitted).
     */
    public void addBin(SimpleImageHistogramBin bin) {
        this.bins.add(bin);
        Collections.sort(this.bins);
    }

    /**
     * Adds an observation to the dataset (by incrementing the item count for
     * the appropriate bin).  A runtime exception is thrown if the value does
     * not fit into any bin and is different from 0 (black pixels are not displayed)
     *
     * @param value  the value.
     */
    private void addObservation(double value) {
        boolean placed = false;
        Iterator iterator = this.bins.iterator();
        while (iterator.hasNext() && !placed) {
            SimpleImageHistogramBin bin = (SimpleImageHistogramBin) iterator.next();
            if (bin.accepts(value)) {
                bin.setItemCount(bin.getItemCount() + 1);
                placed = true;
            }
        }
        if (!placed && value != 0) {
            throw new RuntimeException("No bin: "+value);
        }
    }

    /**
     * Adds a set of values to the dataset
     *
     * @param values  the values ({@code null} not permitted).
     *
     * @see #clearObservations()
     */
    public void addObservations(double[] values) {
        for (int i = 0; i < values.length; i++) {
            addObservation(values[i]);
        }
    }

    /**
     * Removes all current observation data
     *
     * @since 1.0.6
     *
     * @see #addObservations(double[])
     */
    public void clearObservations() {
        Iterator iterator = this.bins.iterator();
        while (iterator.hasNext()) {
            SimpleImageHistogramBin bin = (SimpleImageHistogramBin) iterator.next();
            bin.setItemCount(0);
        }
    }

    /**
     * Returns the x-value for an item within a series.  The x-values may or
     * may not be returned in ascending order, that is up to the class
     * implementing the interface.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The x-value (never {@code null}).
     */
    @Override
    public Number getX(int series, int item) {
        return getXValue(series, item);
    }

    /**
     * Returns the x-value (as a double primitive) for an item within a series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The x-value.
     */
    @Override
    public double getXValue(int series, int item) {
        SimpleImageHistogramBin bin = (SimpleImageHistogramBin) this.bins.get(item);
        return (bin.getBinNumber() + bin.getBinNumber()+1) / 2.0;
    }

    /**
     * Returns the y-value for an item within a series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The y-value (possibly {@code null}).
     */
    @Override
    public Number getY(int series, int item) {
        return getYValue(series, item);
    }

    /**
     * Returns the y-value (as a double primitive) for an item within a series
     * normalized using the normalization factor
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The y-value.
     *
     * @see #setNormalizationFactor(double)
     */
    @Override
    public double getYValue(int series, int item) {
        SimpleImageHistogramBin bin = (SimpleImageHistogramBin) this.bins.get(item);
        return bin.getItemCount()/normalizationFactor;
    }

    /**
     * Returns the starting X value for the specified series and item.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public Number getStartX(int series, int item) {
        return getStartXValue(series, item);
    }

    /**
     * Returns the start x-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return The start x-value.
     */
    @Override
    public double getStartXValue(int series, int item) {
        SimpleImageHistogramBin bin = (SimpleImageHistogramBin) this.bins.get(item);
        return bin.getBinNumber();
    }

    /**
     * Returns the ending X value for the specified series and item.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public Number getEndX(int series, int item) {
        return getEndXValue(series, item);
    }

    /**
     * Returns the end x-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The end x-value.
     */
    @Override
    public double getEndXValue(int series, int item) {
        SimpleImageHistogramBin bin = (SimpleImageHistogramBin) this.bins.get(item);
        return bin.getBinNumber()+1;
    }

    /**
     * Returns the starting Y value for the specified series and item.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public Number getStartY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Returns the start y-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The start y-value.
     */
    @Override
    public double getStartYValue(int series, int item) {
        return getYValue(series, item);
    }

    /**
     * Returns the ending Y value for the specified series and item.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public Number getEndY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Returns the end y-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The end y-value.
     */
    @Override
    public double getEndYValue(int series, int item) {
        return getYValue(series, item);
    }

    /**
     * Compares the dataset for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImageHistogramDataset)) {
            return false;
        }
        ImageHistogramDataset that = (ImageHistogramDataset) obj;
        if (!this.key.equals(that.key)) {
            return false;
        }
        if (!this.bins.equals(that.bins)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the dataset.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException not thrown by this class, but maybe
     *         by subclasses (if any).
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ImageHistogramDataset clone = (ImageHistogramDataset) super.clone();
        clone.bins = (List) ObjectUtils.deepClone(this.bins);
        return clone;
    }

}
