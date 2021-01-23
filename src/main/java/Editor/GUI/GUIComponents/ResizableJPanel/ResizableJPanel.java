package Editor.GUI.GUIComponents.ResizableJPanel;

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

    private final int borderWidth = 3;
    private final int borderDragClearance = 5;
    private final ResizableSide resizableSide;

    private ArrayList<ActionListener> resizeListeners = new ArrayList<>();

    public ResizableJPanel(ResizableSide resizableSide){
        super();
        this.resizableSide = resizableSide;
        setBorder();
        drag(this);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setBorder();
    }

    public ResizableJPanel(){
        this(ResizableSide.LEFT);
    }

    public void addResizeListener(ActionListener a){
        resizeListeners.add(a);
    }

    private void setBorder(){
        super.setBorder(BorderFactory.createMatteBorder(
                (resizableSide==ResizableSide.TOP)?borderWidth:0,
                (resizableSide==ResizableSide.LEFT)?borderWidth:0,
                0,
                0,
                this.getBackground().darker())
        );
    }

    private void notifyResizeListeners(int id){
        for(ActionListener a : resizeListeners)
            a.actionPerformed(new ResizeEvent(this,id,null));
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
                    notifyResizeListeners(0);
                    panel.repaint();
                    panel.revalidate();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(resizableSide==ResizableSide.LEFT && e.getX()>=0-borderDragClearance && e.getX()<borderWidth+borderDragClearance){
                    panel.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }
                else if(resizableSide==ResizableSide.TOP && e.getY()>=0-borderDragClearance && e.getY()<borderWidth+borderDragClearance){
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
                if(resizableSide==ResizableSide.LEFT && e.getX()>=0-borderDragClearance && e.getX()<borderWidth+borderDragClearance)
                    isPressed.set(true);
                else if(resizableSide==ResizableSide.TOP && e.getY()>=0-borderDragClearance && e.getY()<borderWidth+borderDragClearance)
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

