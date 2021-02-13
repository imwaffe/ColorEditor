package Editor.GUI.GUIComponents.Menu;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class ModeMenu extends JMenu {

    private final ButtonGroup modeGroup = new ButtonGroup();
    private final JMenuItem RGB = new JRadioButtonMenuItem("RGB",null,false);
    private final JMenuItem LMS = new JRadioButtonMenuItem("LMS",null,true);
    private final JMenuItem Lab = new JRadioButtonMenuItem("Lab");

    private Runnable RGBaction = null;
    private Runnable LMSaction = null;
    private Runnable LabAction = null;

    public ModeMenu(){
        super("Mode");

        modeGroup.add(RGB);
        modeGroup.add(LMS);
        modeGroup.add(Lab);

        super.add(RGB);
        super.add(LMS);
        super.add(Lab);

        RGB.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                RGBaction.run();
        });
        LMS.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED)
                LMSaction.run();
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

    public void setLabAction(Runnable labAction) {
        this.LabAction = labAction;
    }
}
