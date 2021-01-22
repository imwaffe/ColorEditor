package Editor.GUI.GUIComponents.ResizableJPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ResizeEvent extends ActionEvent {

    public static enum ACTION{
        WIDER(1),
        NARROWER(-1),
        TALLER(2),
        SHORTER(-2);

        private final int value;

        ACTION(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }

    public ResizeEvent(Object source, int id, String command) {
        super(source, id, command);
    }

    public int getWidth(){
        return ((JPanel) source).getWidth();
    }

    public int getHeight(){
        return ((JPanel) source).getHeight();
    }
}
