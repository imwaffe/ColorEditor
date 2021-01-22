package Editor;

import Editor.GUI.Controllers.FileController.*;
import Editor.GUI.Controllers.GUIObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;
import Histograms.HistogramController;
import ImageTools.AlterColor.AlterColor;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        AlterColor alterColor = new AlterRGB();
        ImageProxy imageProxy = new ImageProxy(alterColor);
        GUIObserver gui = new GUIObserver(imageProxy);
        FileController fileController = new MockFileController(gui, imageProxy);
        KeyboardController keyboardController = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());
        HistogramController histogram = new HistogramController(gui.getRightPanel(),imageProxy);

        fileController.addObserver(gui);
        fileController.addObserver(histogram);
        alterColor.addObserver(gui);
        alterColor.addObserver(histogram);

        gui.setTitle("Color Editor");

        gui.addSelectionAction(selection -> {
            imageProxy.cropImage(selection);
            gui.setImage(imageProxy.getScaledOutputImage());
        });

        keyboardController.decreaseAction(() -> {
            alterColor.decreaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
        });
        keyboardController.increaseAction(() -> {
            alterColor.increaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
        });
        keyboardController.resetAction(() -> {
            imageProxy.reset();
            gui.setImage(imageProxy.getScaledOriginalImage());
        });
        keyboardController.previewAction(
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