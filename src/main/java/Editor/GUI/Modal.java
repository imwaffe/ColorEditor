package Editor.GUI;

import javax.swing.*;
import java.awt.*;

public class Modal extends JDialog {
    private final JLabel message = new JLabel();

    public Modal(Frame owner){
        this(owner,null);
    }
    public Modal(Frame owner, String title){
        super(owner,title,false);
        disableClose();
        setSize(200,100);
        setResizable(false);
        setLocationRelativeTo(null);
        Container cp = getContentPane();
        cp.add(message);
    }

    public void setMessage(String text){
        message.setText(text);
        validate();
        repaint();
        setVisible(true);
    }

    public void enableClose(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void disableClose(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
