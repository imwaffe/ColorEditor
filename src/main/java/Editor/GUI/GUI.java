package Editor.GUI;

import Editor.GUI.GUIComponents.ColorButton.ColorButton;
import Editor.GUI.GUIComponents.ResizableJPanel.ResizableJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends JFrame{
    private final JPanel imgPanel = new JPanel();
    private final JLabel imgLabel = new JLabel();
    private ResizableJPanel rightPanel = new ResizableJPanel();

    private final Color selectionOverlayColor = new Color(255,255,255,60);

    private final ColorButton rBtn = new ColorButton("R",new Color(150,0,0));
    private final ColorButton gBtn = new ColorButton("G",new Color(0,150,0));
    private final ColorButton bBtn = new ColorButton("B",new Color(0,0,150));

    private final JMenuItem menuFileOpen = new JMenuItem("Open");
    private final JMenuItem menuFileSave = new JMenuItem("Save");

    private final JMenuItem menuSettingsHistograms = new JMenuItem("Show histograms...");

    private final ArrayList<Consumer<Rectangle>> selectedActions = new ArrayList<>();

    public enum Channel {RED, GREEN, BLUE}

    public GUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgPanel.setBackground(new Color(0,0,0));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ResizableJPanel btnPanel = new ResizableJPanel(ResizableJPanel.ResizableSide.TOP);
        btnPanel.setPreferredSize(new Dimension(screenSize.width,150));
        btnPanel.setBackground(new Color(19, 19, 19));

        rightPanel.setBackground(new Color(102, 102, 102));
        rightPanel.setPreferredSize(new Dimension(300,super.getHeight()));

        super.add(rightPanel, BorderLayout.EAST);
        JPanel overlayImgPanel = new JPanel();
        super.add(overlayImgPanel, BorderLayout.NORTH);
        super.add(imgPanel, BorderLayout.CENTER);
        super.add(btnPanel, BorderLayout.SOUTH);

        btnPanel.add(rBtn);
        btnPanel.add(gBtn);
        btnPanel.add(bBtn);

        JMenu menuFile = new JMenu("File");
        JMenu menuSettings = new JMenu("Settings");

        menuFile.add(menuFileOpen);
        menuFile.add(menuFileSave);

        menuSettings.add(menuSettingsHistograms);

        JMenuBar bar = new JMenuBar();
        bar.add(menuFile);
        bar.add(menuSettings);

        selectArea(imgLabel);

        super.getContentPane().add(BorderLayout.NORTH, bar);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        super.setSize(screenSize);
        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.setResizable(false);
        super.setVisible(true);
    }

    /** Display a new image in window */
    public void setImage(BufferedImage img){
        imgLabel.setIcon(new ImageIcon(img));
        imgPanel.removeAll();
        imgPanel.add(imgLabel);
        super.revalidate();
        super.repaint();
    }

    /** Get maximum size the image should have, based on screen size */
    public Dimension getImagePanelSize(){
        return new Dimension(imgPanel.getWidth()-100, imgPanel.getHeight()-100);
    }

    /** Return true if the button corresponding to a specific channel is pressed */
    public boolean getSelectedChannel(Channel ch){
        switch(ch){
            case RED:
                return rBtn.isSelected();
            case GREEN:
                return gBtn.isSelected();
            case BLUE:
                return bBtn.isSelected();
            default:
                return false;
        }
    }

    /** Listeners for "Open" and "Save" actions in "File" menu */
    public void openFileListener(ActionListener action){
        menuFileOpen.addActionListener(action);
    }
    public void saveFileListener(ActionListener action){
        menuFileSave.addActionListener(action);
    }
    public ResizableJPanel getRightPanel() {
        return rightPanel;
    }

    public void addSelectionAction(Consumer<Rectangle> selectedAction) {
        selectedActions.add(selectedAction);
    }

    private void selectArea(JLabel parent){
        Rectangle selection = new Rectangle();
        JPanel selectionOverlayPanel = new JPanel();
        selectionOverlayPanel.setBackground(selectionOverlayColor);
        selectionOverlayPanel.setSize(0,0);
        parent.add(selectionOverlayPanel);

        parent.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x1=0,y1=0,x2=0,y2=0;
                selection.setSize(e.getX() - (int)selection.getX(), e.getY() - (int)selection.getY());

                if(selection.getWidth()>=0){
                    x1 = (int)selection.getX();
                    x2 = (int)selection.getWidth();
                }
                else if(selection.getWidth()<0){
                    x2 = (int)Math.abs(selection.getWidth());
                    x1 = e.getX();
                }
                if(selection.getHeight()>=0){
                    y1 = (int)selection.getY();
                    y2 = (int)selection.getHeight();
                }
                else if(selection.getHeight()<0){
                    y2 = (int)Math.abs(selection.getHeight());
                    y1 = e.getY();
                }
                selectionOverlayPanel.setBounds(x1,y1,x2,y2);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        parent.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                selection.setSize(0,0);
                selection.setLocation(e.getX(),e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selection.setLocation(
                        selectionOverlayPanel.getX(),
                        selectionOverlayPanel.getY()
                );
                selection.setSize(selectionOverlayPanel.getWidth(), selectionOverlayPanel.getHeight());

                selectionOverlayPanel.setSize(0,0);

                for(Consumer<Rectangle> selectedAction : selectedActions)
                    selectedAction.accept(selection);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                parent.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
