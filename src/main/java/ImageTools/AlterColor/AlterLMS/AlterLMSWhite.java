package ImageTools.AlterColor.AlterLMS;

import Editor.ImageControllers.ImageProxy;

public class AlterLMSWhite extends AlterLMS{
    public AlterLMSWhite(ImageProxy image){
        double[] refWhite;
        double[] refBlue = {0.04649754622,	0.08670141862,	0.8725692246};
        if(image == null)
            refWhite = new double[]{1,1,1};
        else {
            try {
                refWhite = ReferenceWhite.greyWorldLMS(image);
            } catch (NullPointerException e){
                refWhite = new double[]{1,1,1};
            }
        }
        super.KLM = Daltonization.getFirstCoefficient(refWhite, refBlue, Daltonization.DEFICIENCY.PROTAN);
        super.KLS = Daltonization.getSecondCoefficient(refWhite, refBlue, Daltonization.DEFICIENCY.PROTAN);
        super.KML = Daltonization.getFirstCoefficient(refWhite, refBlue, Daltonization.DEFICIENCY.DEUTAN);
        super.KMS = Daltonization.getSecondCoefficient(refWhite, refBlue, Daltonization.DEFICIENCY.DEUTAN);
    }

    public AlterLMSWhite(){
        super();
    }

    @Override
    public String toStringCoeff() {
        String output = "[";
        if(super.kl!=0)
            output+="KL"+(int)(super.kl*100);
        else if(super.km != 0)
            output+="KM"+(int)(super.km*100);
        else
            output+="na";
        output+=",GREYWORLD]";
        return output;
    }
}
