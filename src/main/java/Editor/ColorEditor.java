package Editor;

import Editor.GUI.Controllers.FileController.*;
import Editor.GUI.Controllers.GuiObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;
import ImageTools.AlterColor.AlterColor;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        AlterColor rgb = new AlterRGB();
        ImageProxy imageProxy = new ImageProxy(rgb);
        GuiObserver gui = new GuiObserver(imageProxy);
        FileController fileController = new MockFileController(gui, imageProxy);
        KeyboardController controller = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());

        fileController.addObserver(gui);
        rgb.addObserver(gui);

        gui.setTitle("Color Editor");

        gui.addSelectionAction(selection -> {
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