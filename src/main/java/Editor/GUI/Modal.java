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
        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        super.setSize(200,100);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        Container cp = super.getContentPane();
        cp.add(message);
    }

    public void setMessage(String text){
        message.setText(text);
        super.validate();
        super.repaint();
        super.setVisible(true);
    }

    public void enableClose(){
        super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void disableClose(){
        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
