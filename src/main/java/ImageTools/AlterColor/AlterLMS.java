package ImageTools.AlterColor;

import ImageTools.RGB2LMS;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class AlterLMS extends AlterColor{
    private final static double KLM = 1.05118294;
    private final static double KLS = -0.05116099;
    private final static double KML = 0.95133125; //0.9513092
    private final static double KMS = 0.04866874; //0.04866992
    private final static double STEPS = 0.05;
    private double k = 0;

    private BufferedImage inputImage;
    private double[] inputLMSraster;

    private final RGB2LMS rgb2lms = new RGB2LMS();

    @Override
    public void increaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        if(ch1 && !ch2 && !ch3){
            if(k < 1-STEPS) {
                k+=STEPS;
                setChanged();
                notifyObservers();
            }
        }
    }

    @Override
    public void decreaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        if(ch1 && !ch2 && !ch3){
            if(k > STEPS) {
                k-=STEPS;
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
            //outputLMSraster[i] = (1 - k) * outputLMSraster[i] + (k * KLM) * outputLMSraster[i + 1] + (k * KLS) * outputLMSraster[i + 2];
            outputLMSraster[i+1] = (k * KML) * outputLMSraster[i] + (1 - k) * outputLMSraster[i + 1] + (k * KMS) * outputLMSraster[i + 2];
        }
        return getBufferedImage(rgb2lms.lms2rgb(outputLMSraster), this.inputImage.getWidth(), this.inputImage.getHeight());
    }

    @Override
    public String toStringCoeff() {
        return "test12345";
    }

    @Override
    public void reset() {
        k=0;
    }
}
