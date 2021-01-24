package Editor.GUI.GUIComponents.Menu;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MenuToggle extends JMenuItem {

    private AtomicBoolean isFlagged = new AtomicBoolean(false);

    public MenuToggle(String text){
        super(text);
        super.addActionListener(a -> {
            isFlagged.set(!isFlagged.get());
        });
    }

}
