package Editor;

import Editor.GUI.Controllers.FileController.FileChooserController;
import Editor.GUI.Controllers.FileController.FileController;
import Editor.GUI.Controllers.GUIObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUI;
import Editor.ImageControllers.ImageProxy;
import Histograms.HistogramController;
import ImageTools.AlterColor.AlterColor;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        AlterColor alterColor = new AlterRGB();
        ImageProxy imageProxy = new ImageProxy(alterColor);
        GUIObserver gui = new GUIObserver(imageProxy);
        FileController fileController = new FileChooserController(gui, imageProxy);
        KeyboardController keyboardController = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());
        HistogramController histogram = new HistogramController(gui.getRightPanel(),imageProxy,true);

        fileController.addObserver(gui);
        alterColor.addObserver(gui);

        gui.setTitle("Color Editor");

        gui.addSelectionAction(selection -> {
            imageProxy.cropImage(selection);
            gui.setImage(imageProxy.getScaledOutputImage());
            histogram.update(new Observable(),null);
        });

        gui.menuSettingsHistogramsListener(state -> {
            gui.getRightPanel().setVisible(state);
            if(!state){
                fileController.deleteObserver(histogram);
                alterColor.deleteObserver(histogram);
            }
            else{
                fileController.addObserver(histogram);
                alterColor.addObserver(histogram);
                histogram.update(new Observable(),null);
            }
        });

        keyboardController.decreaseAction(() ->
            alterColor.decreaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE)
        ));
        keyboardController.increaseAction(() ->
            alterColor.increaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE)
        ));
        keyboardController.resetAction(() -> {
            imageProxy.reset();
            gui.setImage(imageProxy.getScaledOriginalImage());
            histogram.update(new Observable(),null);
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