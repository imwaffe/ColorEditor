package Editor;

import Editor.GUI.Controllers.FileController.FileChooserController;
import Editor.GUI.Controllers.FileController.MockFileController;
import Editor.GUI.Controllers.GuiObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUI gui = new GUI();
        AlterRGB rgb = new AlterRGB();
        ImageProxy imageProxy = new ImageProxy(rgb);
        FileChooserController fileController = new MockFileController(gui, imageProxy);
        KeyboardController controller = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());
        GuiObserver guiObserver = new GuiObserver(gui, imageProxy);

        rgb.addObserver(guiObserver);
        fileController.addObserver(guiObserver);

        gui.setTitle("Color Editor");

        gui.setSelectionAction(selection -> {
            imageProxy.cropImage(selection);
            gui.setImage(imageProxy.getScaledOutputImage());
        });

        controller.decreaseAction(() -> {
            rgb.decreaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
        });
        controller.increaseAction(() -> {
            rgb.increaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
        });
        controller.resetAction(() -> {
            imageProxy.setCropped(false);
            gui.setImage(imageProxy.getScaledOriginalImage());
            rgb.reset();
        });
        controller.previewAction(
            // PRESSED preview key action
            () -> {
                gui.setImage(imageProxy.getScaledOriginalImage());
                },
            // RELEASED preview key action
            () -> {
                gui.setImage(imageProxy.getScaledOutputImage());
        });
    }
}