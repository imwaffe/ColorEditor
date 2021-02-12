package Editor.GUI.GUIComponents.ColorButton;

import javax.swing.*;
import java.util.ArrayList;

public abstract class ButtonsPanel extends JPanel {
    protected ArrayList<ColorButton> buttons = new ArrayList<>();

    public ButtonsPanel(){
        super.setOpaque(false);
    }

    public boolean getSelectedChannel(int channel){
        if(channel<0 || channel>=buttons.size())
            return false;
        return buttons.get(channel).isSelected();
    }

    public void reset(){
        for(ColorButton button : buttons)
            button.reset();
    };
}
