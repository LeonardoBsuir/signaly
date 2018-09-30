package by.bsuir.digitalsignal.model.filter;

import by.bsuir.digitalsignal.math.MathFunctions;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class FilteredSignalData extends GraphSignalData {

    private final Float minFrequency;
    private final Float maxFrequency;
    private final GraphSignalData srcSignal;

    public FilteredSignalData(GraphSignalData signal, Float minFrequency, Float maxFrequency) {
        super(signal);
        this.srcSignal = signal;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.data = signal.getDataCopy();
        super.initData();
    }

    @Override
    public float[] readData(int dataIndex, int dataLength) {
        float[] data = this.srcSignal.readData(dataIndex, dataLength);
        float frequencyResolution = this.getFrequencyResolution();

        Integer minFrequency = this.minFrequency != null ? (int) Math.ceil(this.minFrequency / frequencyResolution) : null;
        Integer maxFrequency = this.maxFrequency != null ? (int) Math.floor(this.maxFrequency / frequencyResolution) : null;
        data = MathFunctions.filterSignalByFrequency(data, minFrequency, maxFrequency);
        return data;
    }

    @Override
    public FilteredSignalData clone() throws CloneNotSupportedException {
        return (FilteredSignalData) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + ". Отфильтрованный";
    }
}
