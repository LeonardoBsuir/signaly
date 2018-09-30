package by.bsuir.digitalsignal.model.wavlet;

import by.bsuir.digitalsignal.model.CommonData;
import by.bsuir.digitalsignal.model.GraphSignalData;

public class Gaus4pSignalData extends GraphSignalData {

    private final int N;
    private final int SDVIG = 0;

    public Gaus4pSignalData(GraphSignalData signalData, float fw, CommonData commonData) {
        super(commonData);
        this.numberChannels = signalData.getNumberChannels();
        this.sampleSize = signalData.getSampleSize();
        this.spectreLinesCount = signalData.getSpectreLinesCount();
        this.cutoffFrequencies = signalData.getCutoffFrequencies();
        this.frequencyResolution = signalData.getFrequencyResolution();
        this.dataBlockReceiveTime = signalData.getDataBlockReceiveTime();
        this.collectionTime = signalData.getCollectionTime();
        this.countCollectedBlocks = signalData.getCountCollectedBlocks();
        this.N = Math.round(2.55F * this.frequencyResolution * (float) this.sampleSize / fw);
        this.totalDataLength = 32768;
        this.totalBlocksReceived = 1;
        this.upperDataLimit = 0;
        this.lowerDataLimit = 0;
        this.data = new float[this.totalDataLength];

        for (int i = 0; i < this.data.length; ++i) {
            float p = (8.0F * (i - this.SDVIG) - 4 * this.N) / this.N;
            this.data[i] = (float) ((p * p * p * p - 6 * p * p + 3) * Math.exp(-(p * p) / 2));
        }

        float p = 0;
        for (float value : this.data) {
            p += Math.abs(value);
        }
        p /= 1.287F;

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

    public int getTotalLength() {
        return this.N + this.SDVIG;
    }

    public boolean isTransformedCapacity() {
        return false;
    }

    public Gaus4pSignalData clone() throws CloneNotSupportedException {
        return (Gaus4pSignalData) super.clone();
    }

    public String toString() {
        return "Вейвлет";
    }
}
