/** Implements a list of BufferedImages as a ListIterator.
 * An ImagesList object is created by giving a File[] array of image files.
 * nextBuffImg() and prevBuffImg() return an element already casted to BufferedImage.
 *
 *                ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
 * */

package ImageTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ListIterator;

public class ImagesList implements ListIterator {
    private ListIterator<File> files;

    public ImagesList(File[] ff){
        files = Arrays.asList(ff).listIterator();
    }

    @Override
    public boolean hasNext() {
        return files.hasNext();
    }

    @Override
    public Object next() {
        try {
            return toBuffImg(files.next());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean hasPrevious() {
        return files.hasPrevious();
    }

    @Override
    public Object previous() {
        try {
            return toBuffImg(files.previous());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int nextIndex() {
        return files.nextIndex();
    }

    @Override
    public int previousIndex() {
        return files.nextIndex();
    }

    @Override
    public void remove() {
        files.remove();
    }


    @Override
    public void set(Object o) {
        files.set((File)o);
    }

    @Override
    public void add(Object o) {
        files.add((File)o);
    }

    public BufferedImage nextBuffImg(){
        return (BufferedImage) this.next();
    }

    public BufferedImage prevBuffImg(){
        return (BufferedImage) this.previous();
    }

    public static BufferedImage toBuffImg(File ff) throws IOException {
        BufferedImage loadedImg;
        BufferedImage in = ImageIO.read(ff);
        loadedImg = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = loadedImg.createGraphics();
        g.drawImage(in, 0, 0, in.getWidth(), in.getHeight(), null);
        g.dispose();
        return loadedImg;
    }
}
