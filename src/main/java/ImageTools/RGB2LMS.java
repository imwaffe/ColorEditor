package ImageTools;

import java.awt.image.RasterFormatException;
import java.util.HashMap;
import java.util.TreeMap;

public class RGB2LMS {
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    private final static int MIN_VAL = 0;   //min value of each color channel
    private final static int MAX_VAL = 255; //max value of each color channel

    private final static double UNLINEAR_RGB_THRS = 0.0031308;  //threshold used for reversing linearization
    private final static double LINEAR_RGB_THRS = 10.31475;     //threshold used for linearization

    private final static double GAMMA = 2.4;    //gamma, used for linearization (and reverting back)

    private final static HashMap<Integer, double[]> rgb2lmsLut = new HashMap<>(10000);
    private final static TreeMap<Double, Integer> unlinearizeLut = new TreeMap<>();

    /** Hunt-Pointer-Estevez transformation matrix from XYZ to LMS normalized to D65 */
    private final static double[][] hpe ={
            {   0.4002,  0.7076,  -0.0808},
            {  -0.2263,  1.1653,   0.0457},
            {   0,       0,        0.9182}
    };

    /** Inverse Hunt-Pointer-Estevez transformation matrix from LSM to XYZ normalized to D65*/
    private final static double[][] hpe_inv ={
            {   1.860070,  -1.129480,   0.219898},
            {   0.361223,   0.638804,  -0.000007},
            {   0,          0,          1.089090}
    };

    private final double[][] xyz_hpe;
    private final double[][] xyz_hpe_inv;


    /**
     * Initialize two matrices that combines XYZ (and inverted XYZ) conversion matrices and HPE (and inverted HPE)
     * matrices, to speed up processing.
     * */
    public RGB2LMS(){
        xyz_hpe = multiply3by3matrices(hpe,RGB2LAB.XYZMatrix(RGB2LAB.ColorSpace.sRGB));
        xyz_hpe_inv = multiply3by3matrices(RGB2LAB.XYZMatrixInverse(RGB2LAB.ColorSpace.sRGB), hpe_inv);
        for(int i=MIN_VAL; i<=MAX_VAL; i++){
            unlinearizeLut.put(linearizeRGB(i,GAMMA),i);
        }
    }

    public double[] xyz2lms(double[] xyzRaster){
        return applyMatrix(xyzRaster, hpe);
    }

    public double[] lms2xyz(double[] lmsRaster){
        return applyMatrix(lmsRaster, hpe_inv);
    }

    public double[] rgb2lms(int[] rgbRaster){
        return applyMatrix(rgbRaster, xyz_hpe);
    }

    public int[] lms2rgb(double[] lmsRaster){
        return applyMatrixRGB(lmsRaster, xyz_hpe_inv);
    }

    private double[] applyMatrix(double[] inputRaster, double[][] conversionMatrix){
        double[] outputRaster = new double[(inputRaster.length*3)];

        for(int i=0;i<inputRaster.length;i+=3){
            outputRaster[i]   = conversionMatrix[0][0]*inputRaster[i] + conversionMatrix[0][1]*inputRaster[i+1] + conversionMatrix[0][2]*inputRaster[i+2]; //X value
            outputRaster[i+1] = conversionMatrix[1][0]*inputRaster[i] + conversionMatrix[1][1]*inputRaster[i+1] + conversionMatrix[1][2]*inputRaster[i+2]; //Y value
            outputRaster[i+2] = conversionMatrix[2][0]*inputRaster[i] + conversionMatrix[2][1]*inputRaster[i+1] + conversionMatrix[2][2]*inputRaster[i+2]; //Z value
        }
        return outputRaster;
    }

    private double[] applyMatrix(int[] inputRaster, double[][] conversionMatrix){
        double[] outputRaster = new double[(inputRaster.length*3)];

        for(int i=0;i<inputRaster.length;i++){
            double[] lmsVals = new double[3];
            if(!rgb2lmsLut.containsKey(inputRaster[i])) {
                double rComp = linearizeRGB(((inputRaster[i] >> RED) & 0xFF), GAMMA);
                double gComp = linearizeRGB(((inputRaster[i] >> GREEN) & 0xFF), GAMMA);
                double bComp = linearizeRGB(((inputRaster[i] >> BLUE) & 0xFF), GAMMA);
                lmsVals[0] = conversionMatrix[0][0] * rComp + conversionMatrix[0][1] * gComp + conversionMatrix[0][2] * bComp; //X value
                lmsVals[1] = conversionMatrix[1][0] * rComp + conversionMatrix[1][1] * gComp + conversionMatrix[1][2] * bComp; //Y value
                lmsVals[2] = conversionMatrix[2][0] * rComp + conversionMatrix[2][1] * gComp + conversionMatrix[2][2] * bComp; //Z value
                rgb2lmsLut.put(inputRaster[i], lmsVals);
            }
            lmsVals = rgb2lmsLut.get(inputRaster[i]);
            outputRaster[(i * 3)] = lmsVals[0];
            outputRaster[(i * 3) + 1] = lmsVals[1];
            outputRaster[(i * 3) + 2] = lmsVals[2];
        }
        return outputRaster;
    }

    private int[] applyMatrixRGB(double[] inputRaster, double[][] conversionMatrix){
        if(inputRaster.length%3!=0)
            throw new RasterFormatException("Given XYZ raster length should be a multiple of 3");
        int[] rgbRaster = new int[inputRaster.length/3];

        for(int i=0;i<inputRaster.length;i+=3){
            int rComp = unlinearizeRGB( (( conversionMatrix[0][0]*inputRaster[i]  +  conversionMatrix[0][1]*inputRaster[i+1]  +  conversionMatrix[0][2]*inputRaster[i+2]))) << RED;   //R value
            int gComp = unlinearizeRGB( (( conversionMatrix[1][0]*inputRaster[i]  +  conversionMatrix[1][1]*inputRaster[i+1]  +  conversionMatrix[1][2]*inputRaster[i+2]))) << GREEN; //G value
            int bComp = unlinearizeRGB( (( conversionMatrix[2][0]*inputRaster[i]  +  conversionMatrix[2][1]*inputRaster[i+1]  +  conversionMatrix[2][2]*inputRaster[i+2]))) << BLUE;  //B value

            rgbRaster[i/3] = rComp+gComp+bComp;
        }

        return rgbRaster;
    }

    protected double linearizeRGB (int rgb, double gamma){
        double linearRGB;
        if(rgb <= LINEAR_RGB_THRS)
            linearRGB = rgb/(MAX_VAL*12.92);
        else
            linearRGB = Math.pow((((double)rgb/MAX_VAL + 0.055)/1.055),gamma);
        return linearRGB;
    }

    protected int unlinearizeRGB (double linearRGB){
        try{
            return unlinearizeLut.floorEntry(linearRGB).getValue();
        } catch (NullPointerException e){
            return 0;
        }
    }

    private static double[][] multiply3by3matrices(double[][] a, double[][] b){
        double[][] result = new double[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                result[i][j]=0;
                for(int k=0;k<3;k++)
                    result[i][j]+=a[i][k]*b[k][j];
            }
        }

        return result;
    }
}