package Editor.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResizableJPanel extends JPanel {
    public enum ResizableSide {TOP, LEFT};

    private final int borderWidth = 5;
    private final ResizableSide resizableSide;

    private ArrayList<ActionListener> resizeListeners = new ArrayList<>();

    public ResizableJPanel(ResizableSide resizableSide){
        super();
        this.resizableSide = resizableSide;
        setBorder(BorderFactory.createMatteBorder(
                (resizableSide==ResizableSide.TOP)?borderWidth:0,
                (resizableSide==ResizableSide.LEFT)?borderWidth:0,
                0,
                0,
                super.getBackground().brighter())
        );
        drag(this);
    }
    public ResizableJPanel(){
        this(ResizableSide.LEFT);
    }

    public void addResizeListener(ActionListener a){
        resizeListeners.add(a);
    }

    private void notifyResizeListeners(){
        for(ActionListener a : resizeListeners)
            a.actionPerformed(new ResizeJPanelEvent(this,0,null));
    }

    private void drag(JPanel panel){
        AtomicBoolean isPressed = new AtomicBoolean(false);

        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isPressed.get()) {
                    if(resizableSide==ResizableSide.LEFT)
                        panel.setPreferredSize(new Dimension(panel.getWidth() - e.getX(), panel.getHeight()));
                    if(resizableSide==ResizableSide.TOP)
                        panel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight() - e.getY()));
                    panel.repaint();
                    panel.revalidate();
                    notifyResizeListeners();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(resizableSide==ResizableSide.LEFT && e.getX()>=0 && e.getX()<borderWidth){
                    panel.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }
                else if(resizableSide==ResizableSide.TOP && e.getY()>=0 && e.getY()<borderWidth){
                    panel.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                }
                else{
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            }
        });
        super.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(resizableSide==ResizableSide.LEFT && e.getX()>=0 && e.getX()<borderWidth)
                    isPressed.set(true);
                else if(resizableSide==ResizableSide.TOP && e.getY()>=0 && e.getY()<borderWidth)
                    isPressed.set(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed.set(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}

