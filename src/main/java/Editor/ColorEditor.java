package Editor;

import Editor.GUI.Controllers.FileController.FileChooserController;
import Editor.GUI.Controllers.FileController.FileController;
import Editor.GUI.Controllers.FileController.MockFileController;
import Editor.GUI.Controllers.GUIObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelLMS;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelRGB;
import Editor.GUI.GUIComponents.Menu.ModeMenu;
import Editor.GUI.GUIComponents.Modal.Modal;
import Editor.ImageControllers.ImageProxy;
import Histograms.HistogramController;
import ImageTools.AlterColor.AlterColorStrategy;
import ImageTools.AlterColor.AlterLMS;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        AlterColorStrategy alterColor = new AlterColorStrategy(new AlterLMS());
        ImageProxy imageProxy = new ImageProxy(alterColor);

        GUIObserver gui = new GUIObserver(imageProxy);
        gui.setButtonsPanel(new ButtonsPanelLMS());
        ModeMenu modeMenu = new ModeMenu();
        gui.getMenus().add(modeMenu);
        HistogramController histogram = new HistogramController(gui.getRightPanel(),imageProxy,true);

        FileController fileController = new MockFileController(gui, imageProxy);
        KeyboardController keyboardController = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());

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
        gui.menuSettingsHistogramsHiResListener(a -> {
            Modal calculating = new Modal(gui);
            calculating.disableClose();
            calculating.setSize(new Dimension(500,100));
            calculating.setTitle("Calculating high resolution histogram...");
            calculating.setMessage("Please wait...\nThis may take several seconds");
            histogram.calculateHighResolutionHistogram();
            calculating.setMessage("Completed!");
            calculating.enableClose();
        });

        keyboardController.decreaseAction(() ->
            alterColor.decreaseByOne(
                    gui.getButtonsPanel().getSelectedChannel(0),
                    gui.getButtonsPanel().getSelectedChannel(1),
                    gui.getButtonsPanel().getSelectedChannel(2)
        ));
        keyboardController.increaseAction(() ->
            alterColor.increaseByOne(
                    gui.getButtonsPanel().getSelectedChannel(0),
                    gui.getButtonsPanel().getSelectedChannel(1),
                    gui.getButtonsPanel().getSelectedChannel(2)
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

        modeMenu.setRGBaction(() -> {
            alterColor.setStrategy(new AlterRGB());
            gui.setButtonsPanel(new ButtonsPanelRGB());
            gui.revalidate();
            gui.repaint();
            imageProxy.reset();
            histogram.update(new Observable(),null);
        });
        modeMenu.setLMSaction(() -> {
            alterColor.setStrategy(new AlterLMS());
            gui.setButtonsPanel(new ButtonsPanelLMS());
            gui.revalidate();
            gui.repaint();
            imageProxy.reset();
            histogram.update(new Observable(),null);
        });
    }
}