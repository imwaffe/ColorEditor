/**              Scale BufferedImage images to the smallest dimension (x or y) keeping aspect ratio.
 *
 *               ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
 * */

package ImageTools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScaler {

    public static BufferedImage resizeImage(BufferedImage originalImage, Dimension maxDimension) {
        Dimension imageDimensions = new Dimension(originalImage.getWidth(), originalImage.getHeight());
        Dimension scaledDimensions = getScaledDimension(imageDimensions, maxDimension);

        BufferedImage resizedImage = new BufferedImage(scaledDimensions.width, scaledDimensions.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledDimensions.width, scaledDimensions.height, Color.WHITE, null);
        g.dispose();

        return resizedImage;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension maxDimension) {
        int newWidth = imgSize.width;
        int newHeight = imgSize.height;

        if (imgSize.width > maxDimension.getWidth()) {
            newWidth = maxDimension.width;
            newHeight = (newWidth * imgSize.height) / imgSize.width;
        }

        if (newHeight > maxDimension.height) {
            newHeight = maxDimension.height;
            newWidth = (newHeight * imgSize.width) / imgSize.height;
        }

        return new Dimension(newWidth, newHeight);
    }

}