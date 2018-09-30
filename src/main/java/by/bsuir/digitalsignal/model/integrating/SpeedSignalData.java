package by.bsuir.digitalsignal.model.integrating;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class SpeedSignalData extends GraphSignalData {

    public SpeedSignalData(GraphSignalData signal) {
        super(signal);
        this.data = signal.getDataCopy();
        MathFunctions.integrate(this.data, 1 / (this.frequencyResolution * this.sampleSize));

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
        return super.toString() + ". Вибросткорость";
    }
}
