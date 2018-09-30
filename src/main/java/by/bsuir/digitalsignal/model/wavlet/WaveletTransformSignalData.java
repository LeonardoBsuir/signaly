package by.bsuir.digitalsignal.model.wavlet;

import by.bsuir.digitalsignal.model.GraphSignalData;

public class WaveletTransformSignalData extends GraphSignalData {
    
    private int idx;
    private float[] srcData;
    private GraphSignalData wavelet;

    public WaveletTransformSignalData(GraphSignalData wavelet, GraphSignalData signal) {
        super(signal);
        this.wavelet = wavelet;
        this.arrayCopy(signal);
        this.srcData = this.data;
        this.data = new float[this.totalDataLength];

        for (int i = 0; i < wavelet.getTotalLength(); ++i) {
            float p = 0;

            for (int j = 0; j < i; ++j) {
                p += this.srcData[j] * wavelet.getData()[wavelet.getTotalLength() - i + j];
            }

            this.data[i] = p;
        }
    }

    @Override
    public boolean calculateNextPart() {
        if (this.idx >= this.data.length - this.wavelet.getTotalLength()) {
            this.srcData = null;
            this.wavelet = null;
            super.initData();
            return true;
        } else {
            float p = 0;
            for (int i = 0; i < this.wavelet.getTotalLength(); ++i) {
                p += this.srcData[this.idx + i] * this.wavelet.getData()[i];
            }
            this.data[this.idx + this.wavelet.getTotalLength()] = p;
            
            ++this.idx;
            return false;
        }
    }

    @Override
    public int getPercent() {
        return 100 * this.idx / (this.data.length - this.wavelet.getTotalLength());
    }

    @Override
    public WaveletTransformSignalData clone() throws CloneNotSupportedException {
        return (WaveletTransformSignalData) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + ". Вейвлет преобразование";
    }
}
