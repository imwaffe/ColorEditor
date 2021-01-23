package Editor.GUI.GUIComponents.ColorButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorButton extends JButton {

    private Color mainBgColor = super.getBackground();
    private Color selectedBgColor = mainBgColor.brighter();
    private final Color mainFgColor = new Color(0,0,0);
    private final Color selectedFgColor = new Color(255,255,255);
    private boolean isSelected = false;

    public ColorButton(String title){
        super(title);
        super.setPreferredSize(new Dimension(100,60));
        super.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isSelected) {
                    ColorButton.super.setBackground(mainBgColor);
                    ColorButton.super.setForeground(mainFgColor);
                }
                else {
                    ColorButton.super.setBackground(selectedBgColor);
                    ColorButton.super.setForeground(selectedFgColor);
                }
                isSelected=!isSelected;
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public ColorButton(String title, Color mainBgColor){
        this(title);
        this.setBackground(mainBgColor);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        mainBgColor = bg;
        selectedBgColor = bg.brighter();
    }

    public boolean isSelected(){
        return isSelected;
    }
}
