package ImageTools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ClipRaster {

    public static BufferedImage clip(BufferedImage input, Rectangle clipArea){
        BufferedImage output = input.getSubimage(
                (int)clipArea.getX(),
                (int)clipArea.getY(),
                (int)clipArea.getWidth(),
                (int)clipArea.getHeight()
        );

        return output;
    }

}
