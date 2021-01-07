/**
 * This class allows the creation of a "complex" image.
 * Its state consists of:
 *      + the original FULL SIZE BufferedImage image (from now on "image" means "BufferedImage image")
 *      + the MAXIMUM SIZE the image should have in order to be DISPLAYED properly on screen
 *      + the SCALED SIZE image, scaled to fit the given MAXIMUM SIZE
 *      + the SCALED CROPPED image, which is a portion of the image, cropped from the SCALED SIZE image
 *      + the SCALE FACTOR, used to keep track of the resize factor between the full scale and scaled images
 *      + a SCALED SELECTION, representing the coordinates and dimensions of the selected area on the SCALED image
 *          from which the SCALED CROPPED image is obtained
 *      + a FULL SIZE SELECTION, just like the SCALED SELECTION, but scaled to fit the original FULL SIZE image
 *
 * The ImageController constructor takes as input the original input BufferedImage image and a Dimension representing
 * the maximum size of the image used for display.
 *
 * The ColorEditor application allows the user to display an image, alter its colors, alter the colors of just a
 * selection of the image (a cropped image) and save the final image. An ImageController object provides the scaled
 * image used for display, allows the selection of a portion of the image to alter just that, and keeping track of
 * the crop selection, the scale factor allows the application to save the full size image (not only the scaled one).
 *
 */

package Editor;

import ImageTools.ImageScaler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageController {
    private BufferedImage fullSizeInputImg, scaledInputImg, scaledCroppedImg;
    private Rectangle selection, fullSizeSelection;
    private class ScaleFactor {
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
    private ScaleFactor scaleFactor = new ScaleFactor();

    public ImageController(BufferedImage input, Dimension maxSize){
        fullSizeInputImg = input;
        scaledInputImg = ImageScaler.resizeImage(fullSizeInputImg, maxSize);
        scaleFactor.setSize(
                ((double)fullSizeInputImg.getWidth())/(double)scaledInputImg.getWidth(),
                ((double)fullSizeInputImg.getHeight())/(double)scaledInputImg.getHeight());
    }

    /** Create a cropped subimage of the scaled size image, given a selection of type Rectangle */
    public void cropImage(Rectangle selection){
        this.selection = selection;
        BufferedImage subimage = scaledInputImg.getSubimage((int)selection.getX(), (int)selection.getY(), (int)selection.getWidth(), (int)selection.getHeight());
        scaledCroppedImg = new BufferedImage(scaledInputImg.getColorModel(), scaledInputImg.getRaster().createCompatibleWritableRaster(subimage.getWidth(), subimage.getHeight()), scaledInputImg.isAlphaPremultiplied(), null);
        subimage.copyData(scaledCroppedImg.getRaster());

        fullSizeSelection = new Rectangle(
                (int)(selection.getX()* scaleFactor.getWidth()),
                (int)(selection.getY()* scaleFactor.getHeight()),
                (int)(selection.getWidth()* scaleFactor.getWidth()),
                (int)(selection.getHeight()* scaleFactor.getHeight())
        );
    }

    /** Returns the full size original image */
    public BufferedImage getFullSizeInputImg() {
        return fullSizeInputImg;
    }

    /** Returns the scaled size image */
    public BufferedImage getScaledInputImg() {
        return scaledInputImg;
    }

    /** Returns the selected crop of the full size image, using the coordinates stored in fullSizeSelection attribute */
    public BufferedImage getFullSizeCroppedImg() {
        BufferedImage fullSizeCroppedImg;
        BufferedImage subimage = fullSizeInputImg.getSubimage((int)fullSizeSelection.getX(), (int)fullSizeSelection.getY(), (int)fullSizeSelection.getWidth(), (int)fullSizeSelection.getHeight());
        fullSizeCroppedImg = new BufferedImage(fullSizeInputImg.getColorModel(), fullSizeInputImg.getRaster().createCompatibleWritableRaster(subimage.getWidth(), subimage.getHeight()), fullSizeInputImg.isAlphaPremultiplied(), null);
        subimage.copyData(fullSizeCroppedImg.getRaster());
        return fullSizeCroppedImg;
    }

    /** Returns the selected crop of the scaled size image */
    public BufferedImage getScaledCroppedImg() {
        return scaledCroppedImg;
    }

    /** Returns the scaled selection coordinates, used to display the cropped image in the right position */
    public Rectangle getSelection(){
        return selection;
    }

    /** Returns the full size selection coordinates, used to position the cropped image in the correct spot in order
     * to save the image
     */
    public Rectangle getFullSizeSelection() { return fullSizeSelection; }

    /** Returns a string containing the full size selection coordinates
     *  The format is as follows:
     *      [xSELECTIONSTART,ySELECTIONSTART,wSELECTIONWIDTH,hSELECTIONHEIGHT]
     *  the selection coordinates starts from the top left corner of the image, being 0,0
     * */
    public String toStringSelection(){
        return "[x"+fullSizeSelection.getX()+",y"+fullSizeSelection.getY()+",W"+fullSizeSelection.getWidth()+",H"+fullSizeSelection.getHeight()+"]";
    }
}
