package Editor.GUI.GUIComponents.ColorButton;

import java.awt.*;

public class ButtonsPanelLMS extends ButtonsPanel{

    public ButtonsPanelLMS() {
        super.buttons.add(new ColorButton("Long", new Color(150, 0, 0)));
        super.buttons.add(new ColorButton("Medium", new Color(0, 150, 0)));

        for (ColorButton button : super.buttons) {
            this.add(button);
            button.resetContainer(this);
        }
    }
}
