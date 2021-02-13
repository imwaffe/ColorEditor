package ImageTools;

import java.awt.image.RasterFormatException;
import java.util.HashMap;

public class RGB2LMS {
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    private final static int MIN_VAL = 0;   //min value of each color channel
    private final static int MAX_VAL = 255; //max value of each color channel

    private final static double GAMMA = 2.4;

    private final static HashMap<Integer, Double> linearizeRgbMap = new HashMap<>(256,1);

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

    //private final double[][][][] rgb2lms_lut = new double[MAX_VAL+1][MAX_VAL+1][MAX_VAL+1][3];

    private final double[][] xyz_hpe;
    private final double[][] xyz_hpe_inv;

    public RGB2LMS(){
        xyz_hpe = multiply3by3matrices(hpe,RGB2LAB.XYZMatrix(RGB2LAB.ColorSpace.sRGB));
        xyz_hpe_inv = multiply3by3matrices(RGB2LAB.XYZMatrixInverse(RGB2LAB.ColorSpace.sRGB), hpe_inv);

        /*for(int r=0; r<MAX_VAL; r++)
            for(int g=0; g<MAX_VAL; g++)
                for(int b=0; b<MAX_VAL; b++)
                    rgb2lms_lut[r][g][b] = rgbValToLMS((r<<RED + g<<GREEN + b<<BLUE), hpe);*/
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

    private static double[] applyMatrix(double[] inputRaster, double[][] conversionMatrix){
        double[] outputRaster = new double[(inputRaster.length*3)];

        for(int i=0;i<inputRaster.length;i+=3){
            outputRaster[i]    =   conversionMatrix[0][0]*inputRaster[i] + conversionMatrix[0][1]*inputRaster[i+1] + conversionMatrix[0][2]*inputRaster[i+2]; //X value
            outputRaster[i+1]  =   conversionMatrix[1][0]*inputRaster[i] + conversionMatrix[1][1]*inputRaster[i+1] + conversionMatrix[1][2]*inputRaster[i+2]; //Y value
            outputRaster[i+2]  =   conversionMatrix[2][0]*inputRaster[i] + conversionMatrix[2][1]*inputRaster[i+1] + conversionMatrix[2][2]*inputRaster[i+2]; //Z value
        }
        return outputRaster;
    }

    private double[] applyMatrix(int[] inputRaster, double[][] conversionMatrix){
        double[] outputRaster = new double[(inputRaster.length*3)];

        for(int i=0;i<inputRaster.length;i++){
            double rComp = linearizeRGB(((inputRaster[i]>>RED) & 0xFF), GAMMA);
            double gComp = linearizeRGB(((inputRaster[i]>>GREEN) & 0xFF), GAMMA);
            double bComp = linearizeRGB(((inputRaster[i]>>BLUE) & 0xFF), GAMMA);
            outputRaster[(i*3)]    =   conversionMatrix[0][0]*rComp + conversionMatrix[0][1]*gComp + conversionMatrix[0][2]*bComp; //X value
            outputRaster[(i*3)+1]  =   conversionMatrix[1][0]*rComp + conversionMatrix[1][1]*gComp + conversionMatrix[1][2]*bComp; //Y value
            outputRaster[(i*3)+2]  =   conversionMatrix[2][0]*rComp + conversionMatrix[2][1]*gComp + conversionMatrix[2][2]*bComp; //Z value
            /*
            double[] converted = rgb2lms_lut[(inputRaster[i]>>RED) & 0xFF][(inputRaster[i]>>GREEN) & 0xFF][(inputRaster[i]>>BLUE) & 0xFF];
            for(int j=0; j<3; j++)
                outputRaster[i+j] = converted[j];
             */
        }
        return outputRaster;
    }

    private static double[] rgbValToLMS(int inputRGB, double [][] conversionMatrix){
        double[] output = new double[3];
        double rComp = linearizeRGB(((inputRGB>>RED) & 0xFF), GAMMA);
        double gComp = linearizeRGB(((inputRGB>>GREEN) & 0xFF), GAMMA);
        double bComp = linearizeRGB(((inputRGB>>BLUE) & 0xFF), GAMMA);

        output[0]    =   conversionMatrix[0][0]*rComp + conversionMatrix[0][1]*gComp + conversionMatrix[0][2]*bComp; //X value
        output[1]  =   conversionMatrix[1][0]*rComp + conversionMatrix[1][1]*gComp + conversionMatrix[1][2]*bComp; //Y value
        output[2]  =   conversionMatrix[2][0]*rComp + conversionMatrix[2][1]*gComp + conversionMatrix[2][2]*bComp; //Z value

        return output;
    }

    public static int[] applyMatrixRGB(double[] inputRaster, double[][] conversionMatrix){
        if(inputRaster.length%3!=0)
            throw new RasterFormatException("Given XYZ raster length should be a multiple of 3");
        int[] rgbRaster = new int[inputRaster.length/3];

        for(int i=0;i<inputRaster.length;i+=3){
            int rComp   = unlinearizeRGB( (( conversionMatrix[0][0]*inputRaster[i]  +  conversionMatrix[0][1]*inputRaster[i+1]  +  conversionMatrix[0][2]*inputRaster[i+2])), GAMMA) << RED;   //R value
            int gComp   = unlinearizeRGB( (( conversionMatrix[1][0]*inputRaster[i]  +  conversionMatrix[1][1]*inputRaster[i+1]  +  conversionMatrix[1][2]*inputRaster[i+2])), GAMMA) << GREEN; //G value
            int bComp   = unlinearizeRGB( (( conversionMatrix[2][0]*inputRaster[i]  +  conversionMatrix[2][1]*inputRaster[i+1]  +  conversionMatrix[2][2]*inputRaster[i+2])), GAMMA) << BLUE;  //B value

            rgbRaster[i/3] = rComp+gComp+bComp;
        }

        return rgbRaster;
    }

    private static double linearizeRGB (int rgb, double gamma){
        if(linearizeRgbMap.containsKey(rgb))
            return linearizeRgbMap.get(rgb);
        double v = 10.31475;
        double linearRGB;
        if(rgb <= v)
            linearRGB = rgb/(MAX_VAL*12.92);
        else
            linearRGB = Math.pow((((double)rgb/MAX_VAL + 0.055)/1.055),gamma);
        linearizeRgbMap.put(rgb, linearRGB);
        return linearRGB;
    }

    private static int unlinearizeRGB (double linearRGB, double gamma){
        double v = 0.0031308;
        int rgb;
        if(linearRGB <= v)
            rgb = (int)Math.round(MAX_VAL*12.92*linearRGB);
        else
            rgb = (int)Math.round(MAX_VAL*(1.055*Math.pow(linearRGB,(1/gamma))-0.055));
        if(rgb<MIN_VAL)
            rgb = 0;
        else if(rgb>MAX_VAL)
            rgb = 255;
        return rgb;
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