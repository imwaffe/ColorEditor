/**
 *  Alter the image in the LMS space, aiming to simulate protan and deutan vision.
 *
 *  kl and km coefficients:
 *      - when kl is equal to 0, the L value of each pixel of the output image is the same as the original image
 *      - when kl is equal to 1, the L value of each pixel of the output image is calculated as a function of solely
 *        the M and S values, simulating protanopia (missing L cones)
 *      The same applies for km coefficient, but it refers to the way the M value is calculated and is meant for
 *      simulating deuteranopia (missing M cones)
 *
 *  KLM, KLS, KML and KMS coefficients are calculated under the constraint that for both deuteranopes and protanopes
 *  white and blue colors should not change. Reference white is represented by the vector {1,1,1} in LMS color space.
 *  For reference blue it's been chosen the native blue with RGB value represented by the vector {0,0,255}, converted
 *  to LMS via Hunt-Pointer-Estevez transformation matrix (after being converted to XYZ color space).
 *
 *  If we were to simulate (for example) protanopia just by setting to 0 all the L values in the image, we would obtain
 *  an inaccurate image, altering also blue and turning white into a greenish white; whereas making the L value a
 *  function of the M and S value, we can make the result independent of the original L value (simulating the lacking
 *  L cones in the retina), and also calculate two coefficients (with which we'll make a linear combination of S and M
 *  values) under the constraint that white should remain white and blue should remain blue (protanopes
 *  have no problems in seeing white or blue).
 *  The same applies to deuteranopia simulation, but by making the M value (and not the L value anymore) a function
 *  of L and S values.
 *
 *  When kl=1, L' in the output image is calculated as L'=KLM*M + KLS*S (where M and S are input image's M and S values)
 *  When km=1, M' in the output image is calculated as M'=KML*L + KMS*S (where L and S are input image's L and S values)
 *
 *  For every value of kl (or km) between 0 and 1, the effective output image L (or M) value is a combination of all
 *  three L, M and S input values.
 *  When kl is between 0 and 1:     L' = (1-kl)*L + kl*(KLM*S) + kl*(KLS*S)
 *  When km is between 0 and 1:     M' = km*(KML*L) + (1-km)*M + km*(KMS*S)
 *
 *  When kl (or km) is equal to 0, the output image is the same as the input image.
 *
 *  This class allows the user to gradually reduce the effect of the M or L value.
 *
 *
 *               ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
 *
 * */

package ImageTools.AlterColor;

import ImageTools.RGB2LMS;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class AlterLMS extends AlterColor{
    private final static double KLM =  1.05118294;  // M coefficient to calculate L and simulate protanopia
    private final static double KLS = -0.05116099;  // S coefficient to calculate L and simulate protanopia
    private final static double KML =  0.95133125;  // L coefficient to calculate M and simulate deuteranopia
    private final static double KMS =  0.04866874;  // S coefficient to calculate M and simulate deuteranopia
    private final static double STEPS = 0.05; // how fast kl and km change (between 0 and 1)
    private double kl = 0;
    private double km = 0;

    private BufferedImage inputImage;
    private double[] inputLMSraster;

    private final RGB2LMS rgb2lms = new RGB2LMS();

    @Override
    public void decreaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        if(ch1 && !ch2 && !ch3){
            km = 0;
            if(kl < 1-STEPS) {
                kl += STEPS;
                setChanged();
                notifyObservers();
            }
        }
        else if(!ch1 && ch2 && !ch3){
            kl = 0;
            if(km < 1-STEPS) {
                km += STEPS;
                setChanged();
                notifyObservers();
            }
        }
    }

    @Override
    public void increaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        if(ch1 && !ch2 && !ch3){
            km = 0;
            if(kl > STEPS) {
                kl -= STEPS;
                setChanged();
                notifyObservers();
            }
        }
        if(!ch1 && ch2 && !ch3){
            kl = 0;
            if(km > STEPS) {
                km -= STEPS;
                setChanged();
                notifyObservers();
            }
        }
    }

    @Override
    public BufferedImage alterImage(BufferedImage inputImage) {
        if (this.inputImage != inputImage) {
            this.inputImage = inputImage;
            int[] rgbRaster = ((DataBufferInt) inputImage.getRaster().getDataBuffer()).getData();
            inputLMSraster = rgb2lms.rgb2lms(rgbRaster);
        }
        double[] outputLMSraster = Arrays.copyOf(inputLMSraster, inputLMSraster.length);
        for (int i = 0; i < outputLMSraster.length; i += 3) {
            outputLMSraster[i] = (1 - kl) * outputLMSraster[i] + (kl * KLM) * outputLMSraster[i + 1] + (kl * KLS) * outputLMSraster[i + 2];
            outputLMSraster[i+1] = (km * KML) * outputLMSraster[i] + (1 - km) * outputLMSraster[i + 1] + (km * KMS) * outputLMSraster[i + 2];
        }
        return getBufferedImage(rgb2lms.lms2rgb(outputLMSraster), this.inputImage.getWidth(), this.inputImage.getHeight());
    }

    @Override
    public String toStringCoeff() {
        String output = "[";
        if(kl!=0)
            output+="KL,"+(int)(kl*100);
        else if(km != 0)
            output+="KM,"+(int)(km*100);
        else
            output+="na";
        output+="]";
        return output;
    }

    @Override
    public void reset() {
        kl = 0;
        km = 0;
    }
}
