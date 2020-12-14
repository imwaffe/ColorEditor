package Editor;

import Editor.GUI.GUI;
import Editor.GUI.Modal;
import ImageTools.ImagesList;
import ImageTools.RGBEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ColorEditor {
    private static final int LEFT_KEY = 37;
    private static final int UP_KEY = 38;
    private static final int RIGHT_KEY = 39;
    private static final int DOWN_KEY = 40;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUI gui = new GUI("Color editor");
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        GUIController controller = new GUIController(manager);
        RGBEditor rgb = new RGBEditor();
        AtomicReference<File> inputFile = new AtomicReference<>();
        AtomicReference<ImageController> img = new AtomicReference<>();
        AtomicBoolean isCropped = new AtomicBoolean(false);

        gui.openFileListener(a -> {
            inputFile.set(gui.fileLoader("Select an image to open..."));
            try {
                img.set(new ImageController(ImagesList.toBuffImg(inputFile.get()), gui.getImagePanelSize()));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                rgb.setImage(img.get().getScaledInputImg());
                gui.setImage(rgb.getAlteredImage());
            }
        });

        gui.saveFileListener(a -> {
            Modal modal = new Modal(gui, "Saving...");
            modal.setMessage("Saving your image...");
            BufferedImage outputImg = new BufferedImage(
                    img.get().getFullSizeInputImg().getWidth(),
                    img.get().getFullSizeInputImg().getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = outputImg.getGraphics();
            if(isCropped.get()){
                g.drawImage(img.get().getFullSizeInputImg(),0,0,null);
                g.drawImage(
                        rgb.alterImage(img.get().getFullSizeCroppedImg()),
                        (int) img.get().getFullSizeSelection().getX(),
                        (int) img.get().getFullSizeSelection().getY(),
                        null);
            }else{
                g.drawImage(rgb.alterImage(img.get().getFullSizeInputImg()),0,0,null );
            }
            try {
                String tokens[] = inputFile.get().getAbsolutePath().split("\\.(?=[^\\.]+$)");
                String coeffs = "_altered";
                File outputFile = new File(tokens[0]+coeffs+"."+tokens[1]);
                ImageIO.write(outputImg,tokens[1],outputFile);
            } catch (IOException e) {
                e.printStackTrace();
                modal.enableClose();
                modal.setMessage("An error occured.");
            } finally {
                modal.enableClose();
                modal.setMessage("Image saved!");
            }
        });

        gui.setSelectionAction(selection -> {
            img.get().clipImage(selection);
            rgb.setImage(img.get().getScaledCroppedImg());
            gui.setImage(img.get().getScaledInputImg());
            gui.setOverlayImage(rgb.getAlteredImage(), (int)selection.getX(), (int)selection.getY());
            isCropped.set(true);
        });

        controller.decreaseAction(() -> {
            rgb.decreaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
            if(isCropped.get())
                gui.setOverlayImage(rgb.getAlteredImage(), (int)img.get().getSelection().getX(), (int)img.get().getSelection().getY());
            else
                gui.setImage(rgb.getAlteredImage());
        });     //decrease by one
        controller.increaseAction(() -> {
            rgb.increaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
            if(isCropped.get())
                gui.setOverlayImage(rgb.getAlteredImage(), (int)img.get().getSelection().getX(), (int)img.get().getSelection().getY());
            else
                gui.setImage(rgb.getAlteredImage());
        });     //increase by one

        manager.addKeyEventDispatcher(e -> {
            boolean isPressed = false;
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X') {
                    if (!isPressed){
                        if(isCropped.get()){
                            gui.setOverlayImage(img.get().getScaledCroppedImg(), (int)img.get().getSelection().getX(), (int)img.get().getSelection().getY());
                        }else{
                            gui.setImage(img.get().getScaledInputImg());
                        }
                    }
                    isPressed = true;
                } else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
                    rgb.reset();
                    gui.resetOverlay();
                    rgb.setImage(img.get().getScaledInputImg());
                    isCropped.set(false);
                    gui.setImage(img.get().getScaledInputImg());
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X') {
                    if(isCropped.get()){
                        gui.setOverlayImage(rgb.getAlteredImage(), (int)img.get().getSelection().getX(), (int)img.get().getSelection().getY());
                    }else{
                        gui.setImage(rgb.getAlteredImage());
                    }
                    isPressed = false;
                }
            }
            return false;
        });
    }
}