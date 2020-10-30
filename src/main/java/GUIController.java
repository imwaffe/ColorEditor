import java.awt.*;
import java.awt.event.KeyEvent;

public class GUIController {
    public KeyboardFocusManager kbManager;

    private static final int LEFT_KEY = 37;
    private static final int UP_KEY = 38;
    private static final int RIGHT_KEY = 39;
    private static final int DOWN_KEY = 40;

    public GUIController(KeyboardFocusManager kbManager){
        this.kbManager = kbManager;
    }

    public void decreaseAction(Runnable action){
        addPressedAction(DOWN_KEY,action);
    }
    public void increaseAction(Runnable action){
        addPressedAction(UP_KEY,action);
    }

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

}
