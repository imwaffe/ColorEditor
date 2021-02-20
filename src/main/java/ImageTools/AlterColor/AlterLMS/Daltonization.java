package ImageTools.AlterColor.AlterLMS;

public class Daltonization {
    public enum DEFICIENCY{DEUTAN, PROTAN};

    public static double[] getCoeff(double[] rW, double[] rB, DEFICIENCY deficiency){
        return new double[]{getFirstCoefficient(rW,rB,deficiency), getSecondCoefficient(rW,rB,deficiency)};
    }

    public static double getFirstCoefficient(double[] rW, double[] rB, DEFICIENCY deficiency){
        switch (deficiency){
            case PROTAN:
                return (rB[0]*rW[2]-rW[0]*rB[2])/(rB[1]*rW[2]-rW[1]*rB[2]);
            case DEUTAN:
                return (rB[1]*rW[2]-rW[1]*rB[2])/(rB[0]*rW[2]-rW[0]*rB[2]);
            default:
                throw new IllegalArgumentException("Deficiency type should have of this values: "+DEFICIENCY.values());
        }
    }

    public static double getSecondCoefficient(double[] rW, double[] rB, DEFICIENCY deficiency){
        switch (deficiency){
            case PROTAN:
                return (rB[0]*rW[1]-rW[0]*rB[1])/(rB[2]*rW[1]-rW[2]*rB[1]);
            case DEUTAN:
                return (rB[1]*rW[0]-rW[1]*rB[0])/(rB[2]*rW[0]-rW[2]*rB[0]);
            default:
                throw new IllegalArgumentException("Deficiency type should have of this values: "+DEFICIENCY.values());
        }
    }
}
