package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.GUI.GUIComponents.Modal.Modal;
import Editor.ImageControllers.ImageController;
import Editor.ImageControllers.ImageProxy;
import ImageTools.AlterColor.AlterColor;
import ImageTools.ImagesList;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

public abstract class FileController extends Observable {
    protected final GUI gui;
    protected File inputFile;
    protected final ImageProxy imageProxy;

    public FileController(GUI gui, ImageProxy imageProxy) {
        this.imageProxy = imageProxy;
        this.gui = gui;
        gui.openFileListener(a -> {
            this.imageProxy.setCropped(false);
            inputFile = openFileStrategy(this.gui);
            try {
                this.imageProxy.setInputImage(
                    new ImageController(ImagesList.toBuffImg(inputFile), this.gui.getImagePanelSize())
                );
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                setChanged();
                notifyObservers();
            }
        });
        gui.saveFileListener(a -> {
            saveFileStrategy(
                    this.imageProxy,
                    inputFile.getAbsolutePath().split("\\.(?=[^\\.]+$)"),
                    new Modal(this.gui, "Saving")
                    );
        });
    }
    abstract File openFileStrategy(GUI gui);

    protected void saveFileStrategy(ImageProxy imageProxy, String filePath[], Modal modal){
        ImageController img = imageProxy.getInputImage();
        AlterColor rgb = imageProxy.getAlterColor();

        modal.setMessage("Saving your image...");

        StringBuilder fileNameCoeffs = new StringBuilder("-COEFF" + rgb.toStringCoeff());
        if (imageProxy.isCropped())
            fileNameCoeffs.append("-SELECTION" + img.toStringSelection());
        try {
            File outputFile = new File(filePath[0] + fileNameCoeffs + "." + filePath[1]);
            ImageIO.write(imageProxy.getFullscaleOutputImage(), filePath[1], outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            modal.enableClose();
            modal.setMessage("An error occured.");
        } finally {
            modal.enableClose();
            modal.setMessage("Image saved!");
        }
    }
}
