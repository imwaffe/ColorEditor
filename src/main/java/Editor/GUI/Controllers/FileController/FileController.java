package Editor.GUI.Controllers.FileController;

import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

public abstract class FileController extends Observable {
    protected final GUI gui;
    protected AtomicReference<File> inputFile = new AtomicReference<>();
    protected final ImageProxy imageProxy;
    //protected final AlterColor alterColor;
    protected ArrayList<Runnable> onOpenActions = new ArrayList<>();

    public FileController(GUI gui, ImageProxy imageProxy) {
        this.imageProxy = imageProxy;
        this.gui = gui;
        gui.openFileListener(e -> {
            setOpenFileListener();
            setChanged();
            notifyObservers();
        });
        gui.saveFileListener(e -> setSaveFileListener());
    }

    public void addActionOnOpen(Runnable action){
        onOpenActions.add(action);
    }

    abstract void setOpenFileListener();
    abstract void setSaveFileListener();
}
