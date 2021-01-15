package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.GUI.Modal;
import Editor.ImageControllers.ImageProxy;
import Editor.ImageControllers.ImageController;
import ImageTools.AlterColor.AlterColor;
import ImageTools.ImagesList;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class FileChooserController extends FileController{

    public FileChooserController(GUI gui, ImageProxy imageProxy) {
        super(gui, imageProxy);
    }

    protected void setOpenFileListener(){
        inputFile.set(gui.fileLoader("Select an image to open..."));
        try {
            imageProxy.setInputImage(
                    new ImageController(ImagesList.toBuffImg(inputFile.get()), gui.getImagePanelSize())
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for(Runnable action : onOpenActions)
                action.run();
        }
    }

    protected void setSaveFileListener(){
        ImageController img = imageProxy.getInputImage();
        AlterColor rgb = imageProxy.getAlterColor();

        Modal modal = new Modal(gui, "Saving...");
        modal.setMessage("Saving your image...");

        StringBuilder fileNameCoeffs = new StringBuilder("-COEFF" + rgb.toStringCoeff());
        if (imageProxy.isCropped())
            fileNameCoeffs.append("-SELECTION" + img.toStringSelection());
        try {
            String tokens[] = inputFile.get().getAbsolutePath().split("\\.(?=[^\\.]+$)");
            File outputFile = new File(tokens[0] + fileNameCoeffs + "." + tokens[1]);
            ImageIO.write(imageProxy.getFullscaleOutputImage(), tokens[1], outputFile);
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
