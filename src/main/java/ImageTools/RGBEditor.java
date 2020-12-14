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

package ImageTools;

import Editor.ImageController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RGBEditor{
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    private final static int MIN_VAL = 0;   //min value of each color channel
    private final static int MAX_VAL = 255; //max value of each color channel


    /**
     * originalRgbRaster is the RGB raster of the original image, this will be used to write the altered image.
     * displayOriginalRgbRaster is the original image scaled to fit the dimensions set by displaySize and
     * displayOutputRgbRaster is the mapped image scaled to fit the dimensions set by displaySize, both are used to
     * preview in real time the altered image.
     * */
    private int[] originalRgbRaster, alteredRgbRaster;
    private int imgWidth, imgHeight;

    private int scaleR=MIN_VAL, scaleG=MIN_VAL, scaleB=MIN_VAL;

    /** R, G and B channels coefficients, for every pixel the value of each channel is multiplied
     * by its relative coefficient in order to alter colors (eg: rCoeff=0.5 will cause the RED channel value
     * of each pixel to be halved) */
    private float rCoeff=1, gCoeff=1, bCoeff=1;

    public RGBEditor(){}

    /** Set the actual image */
    public void setImage(BufferedImage img){
        originalRgbRaster = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        imgWidth = img.getWidth();
        imgHeight = img.getHeight();
        alteredRgbRaster = imageColorsChanger(originalRgbRaster);
    }

    public void setImage(ImageController img){
        originalRgbRaster = ((DataBufferInt) img.getScaledInputImg().getRaster().getDataBuffer()).getData();
        imgWidth = img.getScaledInputImg().getWidth();
        imgHeight = img.getScaledInputImg().getHeight();
        alteredRgbRaster = imageColorsChanger(originalRgbRaster);
    }

    /** Returns a BufferedImage given an int[] RGB raster. If no size is given, the size of displayed image is used. */
    public BufferedImage getBufferedImage(int[] rgbRaster){
        return getBufferedImage(rgbRaster, imgWidth, imgHeight);
    }
    public static BufferedImage getBufferedImage(int[] rgbRaster, int width, int height){
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.setRGB(0,0, width, height,rgbRaster,0, width);
        return buffImg;
    }

    /** This function accept values between -255 and +255 for each channel and calculates coefficients.
     * eg: changeColors(255,0,0) will leave only the RED channel, while changeColors(-255,0,0) will
     *     leave only GREEN and BLUE channels */
    private void changeColors(int rR, int rG, int rB) throws IllegalArgumentException{
        if(rR<MIN_VAL || rR>MAX_VAL || rG<MIN_VAL || rG>MAX_VAL || rB<MIN_VAL || rB>MAX_VAL)
            throw new IllegalArgumentException("Rate must be between "+MIN_VAL+" and "+MAX_VAL);

        rCoeff=(float)(MAX_VAL-rR)/(float)MAX_VAL;
        gCoeff=(float)(MAX_VAL-rG)/(float)MAX_VAL;
        bCoeff=(float)(MAX_VAL-rB)/(float)MAX_VAL;

        this.alteredRgbRaster = imageColorsChanger(originalRgbRaster);
    }

    /** Returns an RGB raster by modifying the one passed as parameter according to rCoeff, gCoeff and bCoeff
     * coefficients values */
    private int[] imageColorsChanger(int[] originalRgbRaster){
        int[] outputRgbRaster = new int[originalRgbRaster.length];
        System.arraycopy(originalRgbRaster,0,outputRgbRaster,0,originalRgbRaster.length);

        for(int i=0; i<outputRgbRaster.length; i++) {
            int rComp = (outputRgbRaster[i]>>RED) & 0xFF;
            int gComp = (outputRgbRaster[i]>>GREEN) & 0xFF;
            int bComp = (outputRgbRaster[i]>>BLUE) & 0xFF;

            rComp= (int) (rComp*rCoeff)<<RED;
            gComp= (int) (gComp*gCoeff)<<GREEN;
            bComp= (int) (bComp*bCoeff)<<BLUE;

            outputRgbRaster[i] = rComp+gComp+bComp;
        }
        return outputRgbRaster;
    }

    /** The following functions decrease or increase the coefficient of each channels.
     * Both require three boolean parameters, representing the channel(s) (R, G or B) that will be emphasized
     * or attenuated */
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
    public void reset(){
        scaleR=MIN_VAL;
        scaleB=MIN_VAL;
        scaleG=MIN_VAL;
        alteredRgbRaster = Arrays.copyOf(originalRgbRaster,originalRgbRaster.length);
    }

    /** Returns the BufferedImage of the modified raster (the resized one used as a preview).
     * It's used to display in real time the alterations to the image */
    public BufferedImage getAlteredImage(){
        return getBufferedImage(alteredRgbRaster);
    }

    /** Returns the BufferedImage of the original raster (the resized one used as a preview). */
    public BufferedImage getOriginalImage(){
        return getBufferedImage(originalRgbRaster);
    }

    /** Saves the full screen altered image using the same path as the original image. */
    public void saveFile(File imageFile) throws IOException {
        BufferedImage outputImg = getBufferedImage(imageColorsChanger(originalRgbRaster), imgWidth, imgHeight);
        String tokens[] = imageFile.getAbsolutePath().split("\\.(?=[^\\.]+$)");
        String coeffs = "_[R"+ rCoeff +"-G"+ gCoeff +"-B"+ bCoeff +"]";
        File outputFile = new File(tokens[0]+coeffs+"."+tokens[1]);
        ImageIO.write(outputImg,tokens[1],outputFile);
    }

    public BufferedImage alterImage(BufferedImage input){
        int[] alteredImg = imageColorsChanger(((DataBufferInt) input.getRaster().getDataBuffer()).getData());
        return getBufferedImage(alteredImg, input.getWidth(), input.getHeight());
    }
}