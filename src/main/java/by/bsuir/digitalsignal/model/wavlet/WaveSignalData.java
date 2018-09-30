package by.bsuir.digitalsignal.model.wavlet;

import by.bsuir.digitalsignal.model.CommonData;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class WaveSignalData extends GraphSignalData {

    private final int N;
    private final int SDVIG = 0;

    public WaveSignalData(GraphSignalData signalData, float fw, CommonData commonData) {
        super(commonData);
        this.numberChannels = signalData.getNumberChannels();
        this.sampleSize = signalData.getSampleSize();
        this.spectreLinesCount = signalData.getSpectreLinesCount();
        this.cutoffFrequencies = signalData.getCutoffFrequencies();
        this.frequencyResolution = signalData.getFrequencyResolution();
        this.dataBlockReceiveTime = signalData.getDataBlockReceiveTime();
        this.collectionTime = signalData.getCollectionTime();
        this.countCollectedBlocks = signalData.getCountCollectedBlocks();
        this.N = Math.round(1.275F * this.frequencyResolution * this.sampleSize / fw);
        this.totalDataLength = 32768;
        this.totalBlocksReceived = 1;
        this.upperDataLimit = 0;
        this.lowerDataLimit = 0;
        this.data = new float[this.totalDataLength];

        for (int i = 0; i < this.data.length; ++i) {
            float p = (8.0F * (i - this.SDVIG) - 4 * this.N) / this.N;
            this.data[i] = (float) (p * Math.exp(-(p * p) / 2));
        }

        float p = 0;
        for (float value : this.data) {
            p += Math.abs(value);
        }
        p /= 1.32F;

        for (int i = 0; i < this.data.length; ++i) {
            this.data[i] /= p;
            if (this.upperDataLimit < this.data[i] * 1.05) {
                this.upperDataLimit = this.data[i] * 1.05F;
            }

            if (this.lowerDataLimit > this.data[i] * 1.05) {
                this.lowerDataLimit = this.data[i] * 1.05F;
            }
        }

        super.initData();
    }

    @Override
    public int getTotalLength() {
        return this.N + this.SDVIG;
    }

    @Override
    public boolean isTransformedCapacity() {
        return false;
    }

    @Override
    public WaveSignalData clone() throws CloneNotSupportedException {
        return (WaveSignalData) super.clone();
    }

    @Override
    public String toString() {
        return "Вейвлет";
    }
}
