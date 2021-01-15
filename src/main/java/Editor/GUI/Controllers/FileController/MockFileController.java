package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;
import Editor.ImageControllers.ImageController;
import ImageTools.ImagesList;

import java.io.File;
import java.io.IOException;

public class MockFileController extends FileChooserController{
    public MockFileController(GUI gui, ImageProxy imageProxy) {
        super(gui, imageProxy);
    }

    @Override
    protected void setOpenFileListener(){
        inputFile.set(new File("D:\\users\\lucaa\\Pictures\\Mona_Lisa-restored.jpg"));
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
}
