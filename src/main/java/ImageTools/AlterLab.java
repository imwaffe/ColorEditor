package ImageTools;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class AlterLab implements AlterColor{
    private final static int RED    = 16;   //trailing zeroes of 0xFF0000 (red channel bit mask)
    private final static int GREEN  = 8;    //trailing zeroes of 0x00FF00 (green channel bit mask)
    private final static int BLUE   = 0;    //trailing zeroes of 0x0000FF (blue channel bit mask)

    private final static int MIN_VAL = 0;
    private final static int MAX_VAL = 255;

    private final static RGB2LAB.ColorSpace COLOR_SPACE = RGB2LAB.ColorSpace.sRGB;
    private final static RGB2LAB.White REF_WHITE = RGB2LAB.White.D65;

    private final double[] originalRgbRaster;
    private double[] outputRgbRaster;
    private final int width, height;

    private int scaleR=MIN_VAL, scaleG=MIN_VAL, scaleB=MIN_VAL;

    public AlterLab(BufferedImage img){
        originalRgbRaster = RGB2LAB.rgb2lab(((DataBufferInt) img.getRaster().getDataBuffer()).getData(), COLOR_SPACE, REF_WHITE);
        outputRgbRaster = Arrays.copyOf(originalRgbRaster,originalRgbRaster.length);
        width = img.getWidth();
        height = img.getHeight();
    }

    private BufferedImage getBuffImg(double[] outputRgbRaster){
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.setRGB(0,0,width,height,RGB2LAB.lab2rgb(outputRgbRaster, COLOR_SPACE, REF_WHITE),0,width);
        return buffImg;
    }

    @Override
    public void increaseByOne(boolean ch1, boolean ch2, boolean ch3) {

    }

    @Override
    public void decreaseByOne(boolean ch1, boolean ch2, boolean ch3) {

    }

    @Override
    public BufferedImage alterImage(BufferedImage input) {
        return null;
    }

    @Override
    public String toStringCoeff() {
        return null;
    }

   /* private void scale(int rR, int rG, int rB) throws IllegalArgumentException{
        double[] outputRgbRaster = new double[originalRgbRaster.length];
        System.arraycopy(originalRgbRaster,0,outputRgbRaster,0,originalRgbRaster.length);
        if(rR<MIN_VAL || rR>MAX_VAL || rG<MIN_VAL || rG>MAX_VAL || rB<MIN_VAL || rB>MAX_VAL)
            throw new IllegalArgumentException("Rate must be between "+MIN_VAL+" and "+MAX_VAL);

        float rCoeff=(float)(MAX_VAL-rR)/(float)MAX_VAL;
        float gCoeff=(float)(MAX_VAL-rG)/(float)MAX_VAL;
        float bCoeff=(float)(MAX_VAL-rB)/(float)MAX_VAL;

        for(int i=0; i<outputRgbRaster.length; i++) {
            int rComp = (outputRgbRaster[i]>>RED) & 0xFF;
            int gComp = (outputRgbRaster[i]>>GREEN) & 0xFF;
            int bComp = (outputRgbRaster[i]>>BLUE) & 0xFF;

            rComp= (int) (rComp*rCoeff)<<RED;
            gComp= (int) (gComp*gCoeff)<<GREEN;
            bComp= (int) (bComp*bCoeff)<<BLUE;

            outputRgbRaster[i] = rComp+gComp+bComp;
        }
        this.outputRgbRaster=outputRgbRaster;
    }

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
        };
        if(b && scaleB<MAX_VAL){
            scaleB++;
            if(scaleB<0){
                scaleR = (scaleR<tmpScaleR)?scaleR:Math.max(scaleR-1,-MAX_VAL);
                scaleG = (scaleG<tmpScaleG)?scaleG:Math.max(scaleG-1,-MAX_VAL);
            }
        };
        try {
            scale(Math.max(scaleR, MIN_VAL), Math.max(scaleG, MIN_VAL), Math.max(scaleB, MIN_VAL));
        }catch(IllegalArgumentException ignored){};
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
            scale(Math.max(scaleR, MIN_VAL), Math.max(scaleG, MIN_VAL), Math.max(scaleB, MIN_VAL));
        }catch(IllegalArgumentException ignored){};
    }

    public void reset(){
        scaleR=MIN_VAL;
        scaleB=MIN_VAL;
        scaleG=MIN_VAL;
        outputRgbRaster = Arrays.copyOf(originalRgbRaster,originalRgbRaster.length);
    }

    public BufferedImage getScaled(){
        return getBuffImg(outputRgbRaster);
    }

    public BufferedImage getOriginal(){
        return getBuffImg(originalRgbRaster);
    }*/
}