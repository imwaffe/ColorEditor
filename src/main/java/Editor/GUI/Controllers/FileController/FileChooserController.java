package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooserController extends FileController{
    public FileChooserController(GUI gui, ImageProxy imageProxy) {
        super(gui, imageProxy);
    }

    @Override
    protected File openFileStrategy(GUI gui){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image to open...");
        FileFilter filesFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(filesFilter);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.showOpenDialog(gui);
        return fileChooser.getSelectedFile();
    }
}
