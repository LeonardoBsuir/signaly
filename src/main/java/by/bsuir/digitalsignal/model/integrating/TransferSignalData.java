package by.bsuir.digitalsignal.model.integrating;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class TransferSignalData extends GraphSignalData {

    public TransferSignalData(SpeedSignalData speed) {
        super(speed);
        this.data = speed.getDataCopy();
        MathFunctions.integrate(this.data, this.getDiscrPeriod());

        float max = Float.MIN_VALUE;
        for (float value : data) {
            if (max < value * 1.15) {
                max = value * 1.15F;
            }
        }
        this.setLimit(max);        

        super.initData();
    }

    @Override
    public String toString() {
        return super.toString() + ". Виброперемещение";
    }
}
