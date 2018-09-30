package by.bsuir.digitalsignal.model.drift;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class DriftSecondDegreeSignalData extends GraphSignalData {
    
    private final GraphSignalData srcSignal;

    public DriftSecondDegreeSignalData(GraphSignalData signal) {
        super(signal);
        this.srcSignal = signal;
        this.data = signal.getDataCopy();
        super.initData();
    }

    @Override
    public float[] readData(int dataIndex, int dataLength) {
        float[] data = this.srcSignal.readData(dataIndex, dataLength);
        MathFunctions.deleteTrend(data, 2);
        return data;
    }

    @Override
    public String toString() {
        return super.toString() + ". Удаленный НЧ Дрейф";
    }
}
