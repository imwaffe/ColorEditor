package Editor.GUI.GUIComponents.ResizableJPanel;

import java.awt.event.ActionEvent;

public class ResizeJPanelEvent extends ActionEvent {
    public ResizeJPanelEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
