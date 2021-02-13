package Editor.GUI;

import Editor.GUI.GUIComponents.ColorButton.ButtonsPanel;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelLMS;
import Editor.GUI.GUIComponents.ColorButton.ButtonsPanelRGB;
import Editor.GUI.GUIComponents.Menu.ModeMenu;
import Editor.GUI.GUIComponents.ResizableJPanel.ResizableJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends JFrame{
    private final JPanel imgPanel = new JPanel();
    private final JLabel imgLabel = new JLabel();
    private final ResizableJPanel rightPanel = new ResizableJPanel();

    private final Color selectionOverlayColor = new Color(255,255,255,60);

    private final JMenuBar bar = new JMenuBar();

    private final JMenuItem menuFileOpen = new JMenuItem("Open");
    private final JMenuItem menuFileSave = new JMenuItem("Save");

    private final JCheckBoxMenuItem menuSettingsHistograms = new JCheckBoxMenuItem("Show histograms...");
    private final JMenuItem menuSettingsHistogramsHiRes = new JMenuItem("High resolution histogram (one time)");

    JPanel buttonsPanelContainer = new JPanel();
    private ButtonsPanel btnPanel = null;

    private final ArrayList<Consumer<Rectangle>> selectedActions = new ArrayList<>();

    public GUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgPanel.setBackground(new Color(68, 68, 68));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        buttonsPanelContainer.setPreferredSize(new Dimension(screenSize.width,70));
        buttonsPanelContainer.setBackground(new Color(38, 38, 38));

        JPanel overlayImgPanel = new JPanel();
        super.add(imgPanel, BorderLayout.CENTER);
        super.add(overlayImgPanel, BorderLayout.NORTH);
        super.add(rightPanel, BorderLayout.EAST);
        super.add(buttonsPanelContainer, BorderLayout.SOUTH);

        JMenu menuFile = new JMenu("File");
        JMenu menuSettings = new JMenu("Settings");

        menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        menuFile.add(menuFileOpen);

        menuFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        menuFile.add(menuFileSave);

        menuSettingsHistograms.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        menuSettings.add(menuSettingsHistograms);
        menuSettingsHistogramsHiRes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        menuSettings.add(menuSettingsHistogramsHiRes);

        bar.add(menuFile);
        bar.add(menuSettings);

        selectArea(imgLabel);

        super.getContentPane().add(BorderLayout.NORTH, bar);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        super.setSize(screenSize);

        rightPanel.setVisible(false);
        rightPanel.setBackground(new Color(38, 38, 38));
        rightPanel.setPreferredSize(new Dimension(300, (int) (super.getHeight()-buttonsPanelContainer.getPreferredSize().getHeight())));

        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.setResizable(false);
        super.setVisible(true);
    }

    public void setButtonsPanel(ButtonsPanel btnPanel){
        this.btnPanel = btnPanel;
        buttonsPanelContainer.removeAll();
        buttonsPanelContainer.add(this.btnPanel);
    }

    public ButtonsPanel getButtonsPanel(){
        return btnPanel;
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

    /** Listeners for "Open" and "Save" actions in "File" menu */
    public void openFileListener(ActionListener action){
        menuFileOpen.addActionListener(action);
    }
    public void saveFileListener(ActionListener action){
        menuFileSave.addActionListener(action);
    }

    public void menuSettingsHistogramsListener(Consumer<Boolean> state) {
        menuSettingsHistograms.addActionListener(a -> {
            state.accept(menuSettingsHistograms.getState());
        });
    }
    public void menuSettingsHistogramsHiResListener(ActionListener action){
        menuSettingsHistogramsHiRes.addActionListener(action);
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

    public JMenuBar getMenus(){
        return this.bar;
    }
}
