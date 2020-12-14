package Editor;

import ImageTools.ImageScaler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageController {
    private BufferedImage fullSizeInputImg, scaledInputImg, scaledCroppedImg;
    private Rectangle selection, fullSizeSelection;
    private class CropFactor{
        private double w,h;
        public void setSize(double w, double h){
            this.w=w;
            this.h=h;
        }
        public double getWidth(){
            return w;
        }
        public double getHeight(){
            return h;
        }
    }
    private CropFactor cropFactor = new CropFactor();

    public ImageController(BufferedImage input, Dimension maxSize){
        fullSizeInputImg = input;
        scaledInputImg = ImageScaler.resizeImage(fullSizeInputImg, maxSize);
        cropFactor.setSize(
                ((double)fullSizeInputImg.getWidth())/(double)scaledInputImg.getWidth(),
                ((double)fullSizeInputImg.getHeight())/(double)scaledInputImg.getHeight());
    }
    public ImageController(BufferedImage input, int width, int height){
        this(input, new Dimension(width,height));
    }

    public void clipImage(Rectangle selection){
        this.selection = selection;
        BufferedImage subimage = scaledInputImg.getSubimage((int)selection.getX(), (int)selection.getY(), (int)selection.getWidth(), (int)selection.getHeight());
        scaledCroppedImg = new BufferedImage(scaledInputImg.getColorModel(), scaledInputImg.getRaster().createCompatibleWritableRaster(subimage.getWidth(), subimage.getHeight()), scaledInputImg.isAlphaPremultiplied(), null);
        subimage.copyData(scaledCroppedImg.getRaster());

        fullSizeSelection = new Rectangle(
                (int)(selection.getX()*cropFactor.getWidth()),
                (int)(selection.getY()*cropFactor.getHeight()),
                (int)(selection.getWidth()*cropFactor.getWidth()),
                (int)(selection.getHeight()*cropFactor.getHeight())
        );
    }

    public BufferedImage getFullSizeInputImg() {
        return fullSizeInputImg;
    }
    public BufferedImage getScaledInputImg() {
        return scaledInputImg;
    }
    public BufferedImage getFullSizeCroppedImg() {
        BufferedImage fullSizeCroppedImg;
        BufferedImage subimage = fullSizeInputImg.getSubimage((int)fullSizeSelection.getX(), (int)fullSizeSelection.getY(), (int)fullSizeSelection.getWidth(), (int)fullSizeSelection.getHeight());
        fullSizeCroppedImg = new BufferedImage(fullSizeInputImg.getColorModel(), fullSizeInputImg.getRaster().createCompatibleWritableRaster(subimage.getWidth(), subimage.getHeight()), fullSizeInputImg.isAlphaPremultiplied(), null);
        subimage.copyData(fullSizeCroppedImg.getRaster());
        return fullSizeCroppedImg;
    }
    public BufferedImage getScaledCroppedImg() {
        return scaledCroppedImg;
    }

    public Rectangle getSelection(){
        return selection;
    }
    public Rectangle getFullSizeSelection() { return fullSizeSelection; }

    public String toStringSelection(){
        return "[x"+fullSizeSelection.getX()+",y"+fullSizeSelection.getY()+",W"+fullSizeSelection.getWidth()+",H"+fullSizeSelection.getHeight()+"]";
    }
}
