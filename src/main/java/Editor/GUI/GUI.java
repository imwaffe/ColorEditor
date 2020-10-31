package Editor.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class GUI extends JFrame{
    private final JFrame frame = new JFrame();
    private final JPanel imgPanel = new JPanel();

    private final ColorButton rBtn = new ColorButton("R",new Color(150,0,0));
    private final ColorButton gBtn = new ColorButton("G",new Color(0,150,0));
    private final ColorButton bBtn = new ColorButton("B",new Color(0,0,150));

    private final JMenuItem menuFileOpen = new JMenuItem("Open");
    private final JMenuItem menuFileSave = new JMenuItem("Save");

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

        frame.add(imgPanel, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        btnPanel.add(rBtn);
        btnPanel.add(gBtn);
        btnPanel.add(bBtn);

        JMenuBar bar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuFilter = new JMenu("Settings");

        menuFile.add(menuFileOpen);
        menuFile.add(menuFileSave);

        bar.add(menuFile);
        bar.add(menuFilter);

        frame.getContentPane().add(BorderLayout.NORTH, bar);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setSize(screenSize);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /** Display a new image in window */
    public void setImage(BufferedImage img){
        imgPanel.removeAll();
        imgPanel.add(new JLabel(new ImageIcon(img)));
        frame.revalidate();
        frame.repaint();
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
}
