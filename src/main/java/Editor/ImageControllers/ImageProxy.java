package Editor.ImageControllers;

import ImageTools.AlterColor.AlterColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageProxy {
    private AlterColor alterColor;
    private ImageController inputImage;
    private boolean isCropped = false;

    public ImageProxy(AlterColor alterColor){
        this.alterColor = alterColor;
    }

    public void setInputImage(ImageController inputImage){
        this.inputImage = inputImage;
    }

    public BufferedImage getFullscaleOutputImage(){
        if(isCropped)
            return overlayImages(inputImage.getFullSizeInputImg(), alterColor.alterImage(inputImage.getFullSizeCroppedImg()), inputImage.getFullSizeSelection());
        return alterColor.alterImage(inputImage.getFullSizeInputImg());
    }
    public BufferedImage getScaledOutputImage(){
        if(isCropped)
            return overlayImages(inputImage.getScaledInputImg(), alterColor.alterImage(inputImage.getScaledCroppedImg()), inputImage.getSelection());
        return alterColor.alterImage(inputImage.getScaledInputImg());
    }
    public BufferedImage getFullscaleOriginalImage(){
        return inputImage.getFullSizeInputImg();
    }
    public BufferedImage getScaledOriginalImage(){
        return inputImage.getScaledInputImg();
    }

    public void cropImage(Rectangle selection){
        inputImage.cropImage(selection);
        setCropped(true);
    }

    public void setCropped(boolean cropped) {
        isCropped = cropped;
    }
    public boolean isCropped(){
        return isCropped;
    }

    public void reset(){
        setCropped(false);
        alterColor.reset();
    }

    public AlterColor getAlterColor() {
        return alterColor;
    }

    public ImageController getInputImage() {
        return inputImage;
    }

    private BufferedImage overlayImages(BufferedImage bg, BufferedImage fg, Rectangle selection){
        BufferedImage outputImg = new BufferedImage(
                bg.getWidth(),
                bg.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = outputImg.getGraphics();
        g.drawImage(bg,0,0,null);
        g.drawImage(
                fg,
                (int) selection.getX(),
                (int) selection.getY(),
                null);
        return outputImg;
    }
}