package Editor.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class GUI extends JFrame{
    private final JFrame frame = new JFrame();
    private final JPanel imgPanel = new JPanel();
    private final JPanel selectionOverlayPanel = new JPanel();
    private final JPanel overlayImgPanel = new JPanel();
    private final JMenuBar bar = new JMenuBar();

    private final ColorButton rBtn = new ColorButton("R",new Color(150,0,0));
    private final ColorButton gBtn = new ColorButton("G",new Color(0,150,0));
    private final ColorButton bBtn = new ColorButton("B",new Color(0,0,150));

    private final JMenuItem menuFileOpen = new JMenuItem("Open");
    private final JMenuItem menuFileSave = new JMenuItem("Save");

    private Consumer<Rectangle> selectedAction;
    private Dimension imgAbsolutePosition = new Dimension(0,0);

    public enum Channel {RED, GREEN, BLUE}

    public GUI(String title) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        frame.setLayout(new BorderLayout());
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgPanel.setBackground(new Color(0,0,0));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(screenSize.width,150));
        btnPanel.setBackground(new Color(78, 78, 78));

        //overlayPanel.setOpaque(false);
        selectionOverlayPanel.setBackground(new Color(255,255,255,60));
        selectionOverlayPanel.setSize(0,0);

        //frame.add(new MyCanvas());
        frame.add(selectionOverlayPanel, BorderLayout.NORTH);
        frame.add(overlayImgPanel, BorderLayout.NORTH);
        frame.add(imgPanel, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        btnPanel.add(rBtn);
        btnPanel.add(gBtn);
        btnPanel.add(bBtn);

        JMenu menuFile = new JMenu("File");
        JMenu menuFilter = new JMenu("Settings");

        menuFile.add(menuFileOpen);
        menuFile.add(menuFileSave);

        bar.add(menuFile);
        bar.add(menuFilter);

        selectArea(imgPanel);

        frame.getContentPane().add(BorderLayout.NORTH, bar);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setSize(screenSize);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /** Display a new image in window */
    public void setImage(BufferedImage img){
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgPanel.removeAll();
        imgPanel.add(imgLabel);
        frame.revalidate();
        frame.repaint();
        imgAbsolutePosition.setSize(imgLabel.getX(),imgLabel.getY());
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

    /** Shows a graphical file chooser and returns the selected File */
    public File fileLoader(String title){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        FileFilter filesFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(filesFilter);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.showOpenDialog(GUI.this);
        return fileChooser.getSelectedFile();
    }

    public void setSelectionAction(Consumer<Rectangle> selectedAction) {
        this.selectedAction = selectedAction;
    }
    private void selectArea(JPanel parent){
        Rectangle selection = new Rectangle();

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
                y1+=bar.getHeight();
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
                        (int)(selectionOverlayPanel.getX()-imgAbsolutePosition.getWidth()),
                        (int)(selectionOverlayPanel.getY()-imgAbsolutePosition.getHeight()-bar.getHeight())
                );
                selection.setSize(selectionOverlayPanel.getWidth(), selectionOverlayPanel.getHeight());
                selectionOverlayPanel.setSize(0,0);
                selectedAction.accept(selection);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public void setOverlayImage(BufferedImage img, int x, int y){
        ImageIcon imgIcn = new ImageIcon(img);
        JLabel imgLabel = new JLabel(imgIcn);
        overlayImgPanel.removeAll();
        overlayImgPanel.setBorder(new EmptyBorder(-5,0,0,0));
        overlayImgPanel.add(imgLabel);
        overlayImgPanel.setBounds(
                (int)imgAbsolutePosition.getWidth()+x,
                (int)imgAbsolutePosition.getHeight()+y+bar.getHeight(),
                img.getWidth(), img.getHeight());
        overlayImgPanel.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }
    public void resetOverlay(){
        overlayImgPanel.setVisible(false);
        frame.revalidate();
        frame.repaint();
    }
}
