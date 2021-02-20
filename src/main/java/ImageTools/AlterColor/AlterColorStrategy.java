package ImageTools.AlterColor;

import java.awt.image.BufferedImage;

public class AlterColorStrategy extends AlterColor{
    private AlterColor strategy;
    private static AlterColorStrategy instance = null;

    public static AlterColorStrategy getInstance(AlterColor strategy){
        if(instance == null)
            instance = new AlterColorStrategy(strategy);
        else
            instance.setStrategy(strategy);
        return instance;
    }

    public static AlterColorStrategy getInstance() {
        if(instance == null)
            throw new IllegalStateException("You should initialize instance using AlterColorStrategy.getInstance(AlterColor strategy)");
        return instance;
    }

    private AlterColorStrategy(AlterColor strategy){
        this.strategy = strategy;
    }

    public void setStrategy(AlterColor strategy){
        instance.strategy = strategy;
    }

    @Override
    public void increaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        strategy.increaseByOne(ch1, ch2, ch3);
        setChanged();
        notifyObservers();
    }

    @Override
    public void decreaseByOne(boolean ch1, boolean ch2, boolean ch3) {
        strategy.decreaseByOne(ch1, ch2, ch3);
        setChanged();
        notifyObservers();
    }

    @Override
    public BufferedImage alterImage(BufferedImage input) {
        return strategy.alterImage(input);
    }

    @Override
    public String toStringCoeff() {
        return strategy.toStringCoeff();
    }

    @Override
    public void reset() {
        strategy.reset();
        setChanged();
        notifyObservers();
    }
}
