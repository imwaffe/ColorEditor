package Editor.GUI.GUIComponents.ColorButton;

import java.awt.*;

public class ButtonsPanelRGB extends ButtonsPanel{

    public ButtonsPanelRGB() {
        super.buttons.add(new ColorButton("R",new Color(150,0,0)));
        super.buttons.add(new ColorButton("G",new Color(0,150,0)));
        super.buttons.add(new ColorButton("B",new Color(0,0,150)));

        for (ColorButton button : super.buttons)
            this.add(button);
    }

}
