package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import java.io.File;

public class FileChooserController extends FileController{

    public FileChooserController(GUI gui, ImageProxy imageProxy) {
        super(gui, imageProxy);
    }

    @Override
    protected File openFileStrategy(GUI gui){
        return gui.fileLoader("Select an image to open...");
    }
}
