package Editor;

import Editor.GUI.Controllers.FileController.FileChooserController;
import Editor.GUI.Controllers.FileController.FileController;
import Editor.GUI.Controllers.GUIObserver;
import Editor.GUI.Controllers.KeyboardController;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelLMS;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelRGB;
import Editor.GUI.GUIComponents.Menu.ModeMenu;
import Editor.GUI.GUIComponents.Modal.Modal;
import Editor.ImageControllers.ImageProxy;
import Histograms.HistogramController;
import ImageTools.AlterColor.AlterColor;
import ImageTools.AlterColor.AlterColorStrategy;
import ImageTools.AlterColor.AlterLMS.AlterLMS;
import ImageTools.AlterColor.AlterLMS.AlterLMSWhite;
import ImageTools.AlterColor.AlterRGB;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class ColorEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        AlterColorStrategy.getInstance(new AlterLMS());
        ImageProxy imageProxy = new ImageProxy(AlterColorStrategy.getInstance());

        GUIObserver gui = new GUIObserver(imageProxy);
        gui.setButtonsPanel(new ButtonsPanelLMS());
        ModeMenu modeMenu = ModeMenu.getInstance();
        gui.getMenus().add(modeMenu);
        HistogramController histogram = new HistogramController(gui.getRightPanel(),imageProxy,true);

        FileController fileController = new FileChooserController(gui, imageProxy);
        KeyboardController keyboardController = new KeyboardController(KeyboardFocusManager.getCurrentKeyboardFocusManager());

        fileController.addObserver(gui);
        AlterColorStrategy.getInstance().addObserver(gui);

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
                AlterColorStrategy.getInstance().deleteObserver(histogram);
            }
            else{
                fileController.addObserver(histogram);
                AlterColorStrategy.getInstance().addObserver(histogram);
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
            AlterColorStrategy.getInstance().decreaseByOne(
                    gui.getButtonsPanel().getSelectedChannel(0),
                    gui.getButtonsPanel().getSelectedChannel(1),
                    gui.getButtonsPanel().getSelectedChannel(2)
        ));
        keyboardController.increaseAction(() ->
            AlterColorStrategy.getInstance().increaseByOne(
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
            AlterColorStrategy.getInstance().setStrategy(new AlterRGB());
            gui.setButtonsPanel(new ButtonsPanelRGB());
            gui.revalidate();
            gui.repaint();
            imageProxy.reset();
            histogram.update(new Observable(),null);
        });
        modeMenu.setLMSaction(() -> {
            AlterColorStrategy.getInstance().setStrategy(new AlterLMS());
            gui.setButtonsPanel(new ButtonsPanelLMS());
            gui.revalidate();
            gui.repaint();
            imageProxy.reset();
            histogram.update(new Observable(),null);
        });
        modeMenu.setLMSgwaction(() -> {
            imageProxy.reset();
            AlterColorStrategy.getInstance().setStrategy(new AlterLMSWhite(imageProxy));
            gui.setButtonsPanel(new ButtonsPanelLMS());
            gui.revalidate();
            gui.repaint();
            histogram.update(new Observable(),null);
        });
    }
}