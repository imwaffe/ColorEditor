/**
 * DataBufferInt RGB raster conversion to XYZ and Lab color spaces.
 *
 * rgb2xyz and xyz2rgb require two conversion matrices (the latter being the inverse of the former one), conversion
 * matrices for each color space may be added to private method  XYZMatrix(ColorSpace m)  and XYZMatrixInverse(ColorSpace m)
 * and recalled via ColorSpace enum.
 *
 * xyz2lab and lab2xyz require a reference white (which must be the same for both), reference whites may be added to
 * private method   ReferenceWhite(White w)   and recalled via White enum.
 * Reference whites coordinates are normalised with respect to Y.
 *
 * rgb2xyz returns a double array containing pixels' XYZ values as {x, y, z, x, y, z, ...}
 * xyz2lab and rgb2lab return a double array containing pixels' Lab values as {L, a, b, L, a, b, ...}
 *
 * IMPORTANT!
 * Care must be taken when choosing color space and reference white, pay attention to select the correct white
 * for which the color space is based on.
 *
 * Calculations are based on the following documents and studies:
 *      http://www.brucelindbloom.com/index.html?Eqn_XYZ_to_Lab.html
 *      http://www.brucelindbloom.com/index.html?Eqn_Lab_to_XYZ.html
 *      http://www.brucelindbloom.com/index.html?LContinuity.html
 *      https://en.wikipedia.org/wiki/CIELAB_color_space
 *
 * RGB to XYZ conversion matrices come from the following documents:
 *      http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html
 *
 *
 *              ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
*/

package ImageTools;

import java.awt.image.RasterFormatException;

public class RGB2LAB {
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (RGB BufferedImage red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (RGB BufferedImage green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (RGB BufferedImage blue channel bit mask)
    private final static int MAX_RGB_VAL = 255;

    public enum ColorSpace {sRGB, AdobeRGB}
    public enum White {D65, D50}

    private static double[][] XYZMatrix(ColorSpace m){
        double[][] srgb2xyz_d65 ={
                {   0.4124564,  0.3575761,  0.1804375   },
                {   0.2126729,  0.7151522,  0.0721750   },
                {   0.0193339,  0.1191920,  0.9503041   }
        };
        double[][] adobergb2xyz_d65 = {
                {0.5767309,  0.1855540,  0.1881852},
                {0.2973769,  0.6273491,  0.0752741},
                {0.0270343,  0.0706872,  0.9911085}
        };

        switch(m){
            case sRGB:
                return srgb2xyz_d65;
            case AdobeRGB:
                return adobergb2xyz_d65;
            default:
                throw new IllegalArgumentException("Not a valid matrix");
        }
    }
    private static double[][] XYZMatrixInverse(ColorSpace m)  {
        double[][] srgb2xyz_d65_inv ={
                {   3.2404542, -1.5371385, -0.4985314  },
                {  -0.9692660,  1.8760108,  0.0415560  },
                {   0.0556434, -0.2040259,  1.0572252  }
        };
        double[][] adobergb2xyz_d65_inv={
                {   2.0413690,  -0.5649464, -0.3446944  },
                {  -0.9692660,   1.8760108,  0.0415560  },
                {   0.0134474,  -0.1183897,  1.0154096  }
        };

        switch(m){
            case sRGB:
                return srgb2xyz_d65_inv;
            case AdobeRGB:
                return adobergb2xyz_d65_inv;
            default:
                throw new IllegalArgumentException("Not a valid matrix");
        }
    }
    private static double[] ReferenceWhite(White w){
        switch(w){
            case D50:
                return new double[]{0.9642, 1.0000, 0.8251};
            case D65:
                return new double[]{0.9504, 1.0000, 1.0888};
            default:
                throw new IllegalArgumentException("Not a valid reference white");
        }
    }

    public static double[] rgb2xyz(int[] rgbRaster, ColorSpace m){
        double[] xyzRaster = new double[(rgbRaster.length*3)];
        double[][] conversionMatrix = XYZMatrix(m);

        for(int i=0;i<rgbRaster.length;i++){
            double rComp = ((rgbRaster[i]>>RED) & 0xFF)   / (double) MAX_RGB_VAL;
            double gComp = ((rgbRaster[i]>>GREEN) & 0xFF) / (double) MAX_RGB_VAL;
            double bComp = ((rgbRaster[i]>>BLUE) & 0xFF)  / (double) MAX_RGB_VAL;
            xyzRaster[(i*3)]    =   conversionMatrix[0][0]*rComp + conversionMatrix[0][1]*gComp + conversionMatrix[0][2]*bComp; //X value
            xyzRaster[(i*3)+1]  =   conversionMatrix[1][0]*rComp + conversionMatrix[1][1]*gComp + conversionMatrix[1][2]*bComp; //Y value
            xyzRaster[(i*3)+2]  =   conversionMatrix[2][0]*rComp + conversionMatrix[2][1]*gComp + conversionMatrix[2][2]*bComp; //Z value
        }
        return xyzRaster;
    }
    public static int[] xyz2rgb(double[] xyzRaster, ColorSpace m){
        if(xyzRaster.length%3!=0)
            throw new RasterFormatException("Given XYZ raster length should be a multiple of 3");
        int[] rgbRaster = new int[xyzRaster.length/3];
        double[][] conversionMatrix = XYZMatrixInverse(m);

        for(int i=0;i<xyzRaster.length;i+=3){
            int rComp   = ((int) (( conversionMatrix[0][0]*xyzRaster[i]  +  conversionMatrix[0][1]*xyzRaster[i+1]  +  conversionMatrix[0][2]*xyzRaster[i+2])*MAX_RGB_VAL)) << RED;   //R value
            int gComp   = ((int) (( conversionMatrix[1][0]*xyzRaster[i]  +  conversionMatrix[1][1]*xyzRaster[i+1]  +  conversionMatrix[1][2]*xyzRaster[i+2])*MAX_RGB_VAL)) << GREEN; //G value
            int bComp   = ((int) (( conversionMatrix[2][0]*xyzRaster[i]  +  conversionMatrix[2][1]*xyzRaster[i+1]  +  conversionMatrix[2][2]*xyzRaster[i+2])*MAX_RGB_VAL)) << BLUE;  //B value

            rgbRaster[i/3] = rComp+gComp+bComp;
        }

        return rgbRaster;
    }
    public static double[] xyz2lab(double[] xyzRaster, White w){
        if(xyzRaster.length%3!=0)
            throw new RasterFormatException("Given XYZ raster length should be a multiple of 3");
        final double[] refWhite = ReferenceWhite(w);
        final double e=(double)216/(double)24389;
        final double k=(double)24389/(double)27;

        double[] labRaster = new double[xyzRaster.length];

        for(int i=0;i<xyzRaster.length;i+=3){
            double xr = xyzRaster[i] / refWhite[0];
            double yr = xyzRaster[i+1] / refWhite[1];
            double zr = xyzRaster[i+2] / refWhite[2];

            double fx = (xr > e) ? Math.cbrt(xr) : ((k * xr + 16) / 116);
            double fy = (yr > e) ? Math.cbrt(yr) : ((k * yr + 16) / 116);
            double fz = (zr > e) ? Math.cbrt(zr) : ((k * zr + 16) / 116);

            labRaster[i] = 116 * fy - 16;
            labRaster[i+1] = 500 * (fx - fy);
            labRaster[i+2] = 200 * (fy - fz);
        }
        return labRaster;
    }
    public static double[] lab2xyz(double[] labRaster, White w){
        if(labRaster.length%3!=0)
            throw new RasterFormatException("Given XYZ raster length should be a multiple of 3");
        final double[] refWhite = ReferenceWhite(w);
        final double e=(double)216/(double)24389;
        final double k=(double)24389/(double)27;

        double[] xyzRaster = new double[labRaster.length];

        for(int i=0;i<labRaster.length;i+=3){
            double fy = (labRaster[i] + 16)/116;
            double fx = (labRaster[i+1] / 500) + fy;
            double fz = fy - (labRaster[i+2] / 200);

            double xr = (Math.pow(fx,3) > e) ? Math.pow(fx,3) : ((116 * fx - 16)/k);
            double yr = (labRaster[i] > k*e) ? Math.pow(((labRaster[i]+16)/116),3) : labRaster[i]/k;
            double zr = (Math.pow(fz,3) > e) ? Math.pow(fz,3) : ((116 * fz - 16)/k);

            xyzRaster[i] = xr*refWhite[0];
            xyzRaster[i+1] = yr*refWhite[1];
            xyzRaster[i+2] = zr*refWhite[2];
        }

        return xyzRaster;
    }
    public static double[] rgb2lab(int[] rgbRaster, ColorSpace colorSpace, White referenceWhite){
        return xyz2lab(rgb2xyz(rgbRaster,colorSpace),referenceWhite);
    }
    public static int[] lab2rgb(double[] labRaster, ColorSpace colorSpace, White referenceWhite){
        return xyz2rgb(lab2xyz(labRaster,referenceWhite),colorSpace);
    }
}