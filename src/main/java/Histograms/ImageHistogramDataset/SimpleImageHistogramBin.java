package Histograms.ImageHistogramDataset;

import java.io.Serializable;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.statistics.SimpleHistogramDataset;

/**
 * A bin for the {@link ImageHistogramDataset}.
 */
public class SimpleImageHistogramBin implements Comparable,
        Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 3480862537505941742L;

    /** The lower bound for the bin. */
    private int binNumber;

    /** The item count. */
    private int itemCount;

    /**
     * Creates a new bin.
     *
     * @param binNumber  the bin number.
     */
    public SimpleImageHistogramBin(int binNumber) {
        this.binNumber = binNumber;
        this.itemCount = 0;
    }

    /**
     * Returns the bin number.
     *
     * @return the bin number.
     */
    public int getBinNumber() {
        return this.binNumber;
    }

    /**
     * Returns the item count.
     *
     * @return The item count.
     */
    public int getItemCount() {
        return this.itemCount;
    }

    /**
     * Sets the item count.
     *
     * @param count  the item count.
     */
    public void setItemCount(int count) {
        this.itemCount = count;
    }

    /**
     * Returns {@code true} if the specified value belongs in the bin,
     * and {@code false} otherwise.
     *
     * @param value  the value.
     *
     * @return A boolean.
     */
    public boolean accepts(double value) {
        if (Double.isNaN(value)) {
            return false;
        }
        if (value < this.binNumber) {
            return false;
        }
        if (value > this.binNumber+1) {
            return false;
        }
        return true;
    }

    /**
     * Compares the bin to an arbitrary object and returns the relative
     * ordering.
     *
     * @param obj  the object.
     *
     * @return An integer indicating the relative ordering of the this bin and
     *         the given object.
     */
    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof SimpleImageHistogramBin)) {
            return 0;
        }
        SimpleImageHistogramBin bin = (SimpleImageHistogramBin) obj;
        if (this.binNumber < bin.binNumber) {
            return -1;
        }
        if (this.binNumber > bin.binNumber) {
            return 1;
        }
        return 0;
    }

    /**
     * Tests this bin for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleImageHistogramBin)) {
            return false;
        }
        SimpleImageHistogramBin that = (SimpleImageHistogramBin) obj;
        if (this.binNumber != that.binNumber) {
            return false;
        }
        if (this.itemCount != that.itemCount) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the bin.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException not thrown by this class.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
