package Editor.GUI.Controllers;

import Editor.GUI.GUI;
import Editor.GUI.Modal;
import Editor.ImageControllers.ImageController;
import Editor.ImageControllers.InputImageController;
import ImageTools.AlterColor;
import ImageTools.ImagesList;
import ImageTools.AlterRGB;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

public class FileController extends Observable {
    private final GUI gui;
    private AtomicReference<File> inputFile = new AtomicReference<>();
    private AtomicReference<ImageController> imageController = new AtomicReference<>();

    public FileController(GUI gui, AlterColor alterColor) {
        this.gui = gui;

        gui.openFileListener(a -> {
            inputFile.set(gui.fileLoader("Select an image to open..."));
            try {
                imageController.set(new ImageController(
                        new InputImageController(ImagesList.toBuffImg(inputFile.get()), gui.getImagePanelSize()),
                        alterColor));
                gui.setImage(imageController.get().getScaledOutputImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gui.saveFileListener(a -> {
            InputImageController img = imageController.get().getInputImage();
            AlterRGB rgb = (AlterRGB) imageController.get().getAlterColor();

            Modal modal = new Modal(gui, "Saving...");
            modal.setMessage("Saving your image...");
            BufferedImage outputImg = new BufferedImage(
                    img.getFullSizeInputImg().getWidth(),
                    img.getFullSizeInputImg().getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = outputImg.getGraphics();
            StringBuilder fileNameCoeffs = new StringBuilder("-COEFF" + rgb.toStringCoeff());
            if (imageController.get().isCropped()) {
                fileNameCoeffs.append("-SELECTION" + img.toStringSelection());
                g.drawImage(img.getFullSizeInputImg(), 0, 0, null);
                g.drawImage(
                        rgb.alterImage(img.getFullSizeCroppedImg()),
                        (int) img.getFullSizeSelection().getX(),
                        (int) img.getFullSizeSelection().getY(),
                        null);
            } else {
                g.drawImage(rgb.alterImage(img.getFullSizeInputImg()), 0, 0, null);
            }
            try {
                String tokens[] = inputFile.get().getAbsolutePath().split("\\.(?=[^\\.]+$)");
                File outputFile = new File(tokens[0] + fileNameCoeffs + "." + tokens[1]);
                ImageIO.write(outputImg, tokens[1], outputFile);
            } catch (IOException e) {
                e.printStackTrace();
                modal.enableClose();
                modal.setMessage("An error occured.");
            } finally {
                modal.enableClose();
                modal.setMessage("Image saved!");
            }
        });
    }

    public ImageController getLoadedImage(){
        return imageController.get();
    }

}
