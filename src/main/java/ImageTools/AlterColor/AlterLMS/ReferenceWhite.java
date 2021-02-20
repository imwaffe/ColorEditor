package ImageTools.AlterColor.AlterLMS;

import Editor.ImageControllers.ImageProxy;
import ImageTools.RGB2LMS;

import java.awt.image.DataBufferInt;

public class ReferenceWhite {
    private final static int RED = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    /**
     * @return x.length=3
     */
    public static int greyWorld(ImageProxy image) {
        double[] mean = new double[3];
        int[] inputRaster = ((DataBufferInt) image.getScaledOriginalImage().getRaster().getDataBuffer()).getData();
        for (int i = 0; i < inputRaster.length; i++) {
            mean[0] += (double) ((inputRaster[i] >> RED) & 0xFF) / inputRaster.length;
            mean[1] += (double) ((inputRaster[i] >> GREEN) & 0xFF) / inputRaster.length;
            mean[2] += (double) ((inputRaster[i] >> BLUE) & 0xFF) / inputRaster.length;
        }
        return ((int) Math.round(mean[0]) << RED) + ((int) Math.round(mean[1]) << GREEN) + ((int) Math.round(mean[2]) << BLUE);
    }

    public static double[] greyWorldLMS(ImageProxy image) {
        int[] input = {greyWorld(image)};
        RGB2LMS rgb2LMS = new RGB2LMS();
        return rgb2LMS.rgb2lms(input);
    }
}