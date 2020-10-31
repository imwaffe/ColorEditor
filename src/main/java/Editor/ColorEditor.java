package Editor;

import Editor.GUI.*;
import ImageTools.RGBEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class ColorEditor {
    private static final int LEFT_KEY = 37;
    private static final int UP_KEY = 38;
    private static final int RIGHT_KEY = 39;
    private static final int DOWN_KEY = 40;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUI gui = new GUI("Color editor");
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        GUIController controller = new GUIController(manager);
        RGBEditor rgb = new RGBEditor(gui.getImagePanelSize());

        gui.openFileListener(a -> {
            File ff = gui.fileLoader("Select an image to open...");
            try {
                rgb.setImage(ff);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.setImage(rgb.getOriginalImage());
            });
        gui.saveFileListener(a -> {
            Modal modal = new Modal(gui,"Saving...");
            modal.setMessage("Saving your image...");
            try {
                rgb.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
                modal.enableClose();
                modal.setMessage("An error occured.");
            } finally {
                modal.enableClose();
                modal.setMessage("Image saved!");
            }
        });

        controller.decreaseAction(() -> {
            rgb.decreaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
            gui.setImage(rgb.getModifiedImage());
        });     //decrease by one
        controller.increaseAction(() -> {
            rgb.increaseByOne(
                    gui.getSelectedChannel(GUI.Channel.RED),
                    gui.getSelectedChannel(GUI.Channel.GREEN),
                    gui.getSelectedChannel(GUI.Channel.BLUE));
            gui.setImage(rgb.getModifiedImage());
        });     //increase by one

        manager.addKeyEventDispatcher(e -> {
            boolean isPressed=false;
            if(e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyChar()=='x' || e.getKeyChar()=='X'){
                    if(!isPressed)
                        gui.setImage(rgb.getOriginalImage());
                    isPressed=true;
                }
                else if(e.getKeyChar()=='r' || e.getKeyChar()=='R'){
                    rgb.reset();
                    gui.setImage(rgb.getOriginalImage());
                }
            }
            else if(e.getID() == KeyEvent.KEY_RELEASED){
                if(e.getKeyChar()=='x' || e.getKeyChar()=='X'){
                    gui.setImage(rgb.getModifiedImage());
                    isPressed=false;
                }
            }
            return false;
        });     //reset and show original
    }
}