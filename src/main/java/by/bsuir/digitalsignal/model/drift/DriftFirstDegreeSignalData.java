package by.bsuir.digitalsignal.model.drift;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class DriftFirstDegreeSignalData extends GraphSignalData {
    
    private final GraphSignalData srcSignal;

    public DriftFirstDegreeSignalData(GraphSignalData signal) {
        super(signal);
        this.srcSignal = signal;
        this.data = signal.getDataCopy();
        super.initData();
    }

    @Override
    public float[] readData(int dataIndex, int dataLength) {
        float[] data = this.srcSignal.readData(dataIndex, dataLength);
        MathFunctions.deleteTrend(data, 1);
        return data;
    }

    @Override
    public String toString() {
        return super.toString() + ". Удаленный НЧ Дрейф";
    }
}
