package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import java.io.File;

public class MockFileController extends FileChooserController{
    public MockFileController(GUI gui, ImageProxy imageProxy) {
        super(gui, imageProxy);
    }

    @Override
    protected File openFileStrategy(GUI gui){
        return new File("D:\\users\\lucaa\\Pictures\\Mona_Lisa-restored.jpg");
    }
}
