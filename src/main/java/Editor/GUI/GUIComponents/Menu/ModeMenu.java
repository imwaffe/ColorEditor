package Editor.GUI.GUIComponents.Menu;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ModeMenu extends JMenu {
    private static ModeMenu instance = null;

    private final ButtonGroup modeGroup = new ButtonGroup();
    private final JMenuItem RGB = new JRadioButtonMenuItem("RGB",null,false);
    private final JMenuItem LMS = new JRadioButtonMenuItem("LMS",null,true);
    private final JMenuItem LMSgw = new JRadioButtonMenuItem("LMS Grey World",null,false);
    private final JMenuItem Lab = new JRadioButtonMenuItem("Lab");

    private Runnable RGBaction = null;
    private Runnable LMSaction = null;
    private Runnable LMSgwaction = null;
    private Runnable LabAction = null;

    public static enum mode {RGB, LMS, LMSgw, Lab};

    public static ModeMenu getInstance(){
        if(instance == null)
            instance = new ModeMenu();
        return instance;
    }

    private ModeMenu(){
        super("Mode");

        modeGroup.add(RGB);
        modeGroup.add(LMS);
        modeGroup.add(LMSgw);
        modeGroup.add(Lab);

        super.add(RGB);
        super.add(LMS);
        super.add(LMSgw);
        super.add(Lab);

        RGB.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                RGBaction.run();
        });
        LMS.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                LMSaction.run();
        });
        LMSgw.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                LMSgwaction.run();
        });
        Lab.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                LabAction.run();
        });

        Lab.setEnabled(false);
    }

    public void setRGBaction(Runnable RGBaction) {
        this.RGBaction = RGBaction;
    }

    public void setLMSaction(Runnable LMSaction) {
        this.LMSaction = LMSaction;
    }

    public void setLMSgwaction(Runnable LMSgwaction) {
        this.LMSgwaction = LMSgwaction;
    }

    public void setLabAction(Runnable labAction) {
        this.LabAction = labAction;
    }

    public boolean whiteRef(){
        if(instance.LMSgw.isSelected())
            return true;
        return false;
    }
}
