package Editor.ImageControllers;

import ImageTools.AlterColor;
import ImageTools.AlterRGB;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageController {
    private final AlterColor alterColor;
    private final InputImageController inputImage;
    private boolean isCropped = false;

    public ImageController(AlterColor alterColor, InputImageController inputImage){
        this.alterColor = alterColor;
        this.inputImage = inputImage;
    }
    public ImageController(InputImageController inputImage, AlterColor alterColor){
        this.alterColor = alterColor;
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

    public void setCropped(boolean cropped) {
        isCropped = cropped;
    }
    public boolean isCropped(){
        return isCropped;
    }

    public AlterColor getAlterColor() {
        return alterColor;
    }

    public InputImageController getInputImage() {
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