/**
 * Class used to modify the colors of an image by altering its RGB values according to three "scaling factors",
 * one for each channel.
 * The BufferedImage image is first scaled to a smaller preview size and converted into an int[] RGB raster in order
 * to reduce the overhead of the pixel-by-pixel operations. This way it can be converted back to a BufferedImage only
 * when the preview needs to be shown (again, reducing overhead and keeping the interface more reactive).
 * The calculations on the full size image is done only prior to saving, again keeping the interface more reactive.
 *
 *               ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
 * */

package ImageTools.AlterColor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class AlterRGB extends AlterColor{
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    private final static int MIN_VAL = 0;   //min value of each color channel
    private final static int MAX_VAL = 255; //max value of each color channel

    private BufferedImage inputImage;
    private int[] inputRaster;

    //private TreeMap<Integer, Integer> colorsChangerLut = new TreeMap<>();

    private int scaleR=MIN_VAL, scaleG=MIN_VAL, scaleB=MIN_VAL;

    /** R, G and B channels coefficients, for every pixel the value of each channel is multiplied
     * by its relative coefficient in order to alter colors (eg: rCoeff=0.5 will cause the RED channel value
     * of each pixel to be halved) */
    private float rCoeff=1, gCoeff=1, bCoeff=1;

    /** This function accept values between -255 and +255 for each channel and calculates coefficients.
     * eg: changeColors(255,0,0) will leave only the RED channel, while changeColors(-255,0,0) will
     *     leave only GREEN and BLUE channels */
    private void changeColors(int rR, int rG, int rB) throws IllegalArgumentException{
        if(rR<MIN_VAL || rR>MAX_VAL || rG<MIN_VAL || rG>MAX_VAL || rB<MIN_VAL || rB>MAX_VAL)
            throw new IllegalArgumentException("Rate must be between "+MIN_VAL+" and "+MAX_VAL);

        rCoeff=(float)(MAX_VAL-rR)/(float)MAX_VAL;
        gCoeff=(float)(MAX_VAL-rG)/(float)MAX_VAL;
        bCoeff=(float)(MAX_VAL-rB)/(float)MAX_VAL;
    }

    /** Returns an RGB raster by modifying the one passed as parameter according to rCoeff, gCoeff and bCoeff
     * coefficients values */
    private int[] imageColorsChanger(int[] originalRgbRaster){
        int[] outputRgbRaster = new int[originalRgbRaster.length];

        for(int i=0; i<outputRgbRaster.length; i++) {
            outputRgbRaster[i] = ((int) (((originalRgbRaster[i] >> RED) & 0xFF) * rCoeff) << RED) +
                    ((int) (((originalRgbRaster[i] >> GREEN) & 0xFF) * gCoeff) << GREEN) +
                    ((int) (((originalRgbRaster[i] >> BLUE) & 0xFF) * bCoeff) << BLUE);
        }
        return outputRgbRaster;
    }

    /** The following functions decrease or increase the coefficient of each channels.
     * Both require three boolean parameters, representing the channel(s) (R, G or B) that will be emphasized
     * or attenuated */
    @Override
    public void decreaseByOne(boolean r, boolean g, boolean b){
        if(!r && !b && !g)
            return;
        int tmpScaleR=scaleR, tmpScaleG=scaleG, tmpScaleB=scaleB;

        if(r && scaleR<MAX_VAL){
            scaleR++;
            if(scaleR<0){
                scaleG=Math.max(scaleG-1,-MAX_VAL);
                scaleB=Math.max(scaleB-1,-MAX_VAL);
            }
        }
        if(g && scaleG<MAX_VAL){
            scaleG++;
            if(scaleG<0){
                scaleR = (scaleR<tmpScaleR)?scaleR:Math.max(scaleR-1,-MAX_VAL);
                scaleB = (scaleB<tmpScaleB)?scaleB:Math.max(scaleB-1,-MAX_VAL);
            }
        }
        if(b && scaleB<MAX_VAL){
            scaleB++;
            if(scaleB<0){
                scaleR = (scaleR<tmpScaleR)?scaleR:Math.max(scaleR-1,-MAX_VAL);
                scaleG = (scaleG<tmpScaleG)?scaleG:Math.max(scaleG-1,-MAX_VAL);
            }
        }
        try {
            changeColors(Math.max(scaleR, MIN_VAL), Math.max(scaleG, MIN_VAL), Math.max(scaleB, MIN_VAL));
        }catch(IllegalArgumentException ignored){}
    }
    @Override
    public void increaseByOne(boolean r, boolean g, boolean b){
        if(!r && !b && !g)
            return;
        int tmpScaleR=scaleR, tmpScaleG=scaleG, tmpScaleB=scaleB;

        if(r && scaleR>-(MAX_VAL)){
            scaleR--;
            if(scaleR<0){
                scaleG=Math.min(scaleG+1,MAX_VAL);
                scaleB=Math.min(scaleB+1,MAX_VAL);
            }
        }
        if(g && scaleG>-(MAX_VAL)){
            scaleG--;
            if(scaleG<0){
                scaleR = (scaleR>tmpScaleR)?scaleR:Math.min(scaleR+1,MAX_VAL);
                scaleB = (scaleB>tmpScaleB)?scaleB:Math.min(scaleB+1,MAX_VAL);
            }
        }
        if(b && scaleB>-(MAX_VAL)) {
            scaleB--;
            if(scaleB<0){
                scaleR = (scaleR>tmpScaleR)?scaleR:Math.min(scaleR+1,MAX_VAL);
                scaleG = (scaleG>tmpScaleG)?scaleG:Math.min(scaleG+1,MAX_VAL);
            }
        }
        try {
            changeColors(Math.max(scaleR, MIN_VAL), Math.max(scaleG, MIN_VAL), Math.max(scaleB, MIN_VAL));
        }catch(IllegalArgumentException ignored){}
    }

    /** Resets everything */
    @Override
    public void reset(){
        scaleR=MIN_VAL;
        scaleB=MIN_VAL;
        scaleG=MIN_VAL;
        rCoeff=1;
        gCoeff=1;
        bCoeff=1;
    }

    @Override
    public String toStringCoeff(){
        return "[R"+ rCoeff +",G"+ gCoeff +",B"+ bCoeff +"]";
    }

    @Override
    public BufferedImage alterImage(BufferedImage inputImage){
        if (this.inputImage != inputImage) {
            this.inputImage = inputImage;
            inputRaster = ((DataBufferInt) inputImage.getRaster().getDataBuffer()).getData();
        }
        int[] alteredImg = imageColorsChanger(inputRaster);
        return getBufferedImage(alteredImg, this.inputImage.getWidth(), this.inputImage.getHeight());
    }

}