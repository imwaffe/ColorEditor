/**
 * This class takes care of the keyboard inputs.
 *
 *               ######       CC-BY-SA Luca Armellin @imwaffe luca.armellin@outlook.it        ######
 * */

package Editor.GUI.Controllers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeyboardController extends Observable {
    public KeyboardFocusManager kbManager;

    private static final int LEFT_KEY = 37;
    private static final int UP_KEY = 38;
    private static final int RIGHT_KEY = 39;
    private static final int DOWN_KEY = 40;
    private static final char RESET_KEY = 'r';
    private static final char PREVIEW_KEY = 'x';

    private void addPressedAction(int key, Runnable action){
        kbManager.addKeyEventDispatcher(e -> {
            if(e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyCode() == key) {
                    action.run();
                }
            }
            return false;
        });
    }
    private void addPressedAction(char key, Runnable action){
        addPressedAction((int)(Character.toLowerCase(key)), action);
        addPressedAction((int)(Character.toUpperCase(key)), action);
    }
    private void addReleasedAction(int key, Runnable action){
        kbManager.addKeyEventDispatcher(e -> {
            if(e.getID() == KeyEvent.KEY_RELEASED) {
                if(e.getKeyCode() == key) {
                    action.run();
                }
            }
            return false;
        });
    }
    private void addReleasedAction(char key, Runnable action){
        addReleasedAction((int)(Character.toLowerCase(key)), action);
        addReleasedAction((int)(Character.toUpperCase(key)), action);
    }

    /** Allows the definition of actions that must be executed only once even if the key is held down.
     * isHeldDown flag keeps track of whether the action should be executed or not */
    private final AtomicBoolean isHeldDown = new AtomicBoolean(true);
    private void addHeldDownAction(int key, Runnable action){
        kbManager.addKeyEventDispatcher(e -> {
            if(e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyCode() == key) {
                    if(isHeldDown.compareAndSet(true, false))
                        action.run();
                }
            }
            return false;
        });
        addReleasedAction(key, () -> {
            isHeldDown.set(true);
        });
    }
    private void addHeldDownAction(char key, Runnable action){
        addHeldDownAction((int)(Character.toLowerCase(key)), action);
        addHeldDownAction((int)(Character.toUpperCase(key)), action);
    }

    public KeyboardController(KeyboardFocusManager kbManager){
        this.kbManager = kbManager;
    }

    /** Action to be executed when DOWN key is being pressed */
    public void decreaseAction(Runnable action){
        addPressedAction(DOWN_KEY,action);
    }

    /** Action to be executed when UP key is being pressed */
    public void increaseAction(Runnable action){
        addPressedAction(UP_KEY,action);
    }

    /** Action to be executed when RESET key is being pressed */
    public void resetAction(Runnable action) {
        addPressedAction(RESET_KEY, action);
    }

    /** Actions to be executed when PREVIEW key is being held down and released */
    public void previewAction(Runnable actionPressed, Runnable actionReleased){
        addHeldDownAction(PREVIEW_KEY, actionPressed);
        addReleasedAction(PREVIEW_KEY, actionReleased);
    }

}
