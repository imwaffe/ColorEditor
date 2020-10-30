package GUI;

import javax.swing.*;
import java.awt.*;

public class Modal extends JDialog {
    private final JDialog dialog;
    private final JLabel message = new JLabel();

    public Modal(Frame owner, String title){
        dialog = new JDialog(owner,title,false);
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setSize(200,100);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        Container cp = dialog.getContentPane();
        cp.add(message);
    }

    public void setMessage(String text){
        message.setText(text);
        dialog.validate();
        dialog.repaint();
        dialog.setVisible(true);
    }

    public void enableClose(){
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void disableClose(){
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
