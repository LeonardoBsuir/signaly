package by.bsuir.digitalsignal.model.integrating;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class SpeedPartSignalData extends GraphSignalData {
    
    private final GraphSignalData srcSignal;

    public SpeedPartSignalData(GraphSignalData signal) {
        super(signal);
        this.srcSignal = signal;
        this.data = signal.getDataCopy();
        this.readData(0, this.getTimeFieldSize());
        super.initData();
    }

    @Override
    public float[] readData(int dataIndex, int dataLength) {
        float[] data = this.srcSignal.readData(dataIndex, dataLength);
        float tDiskr = this.getDiscrPeriod();
        MathFunctions.integrate(data, tDiskr);
        
        float max = Float.MIN_VALUE;
        for (float value : data) {
            if (max < value * 1.15) {
                max = value * 1.15F;
            }
        }
        this.setLimit(max);

        return data;
    }

    @Override
    public boolean isTransformedCapacity() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + ". Виброскорость";
    }
}
