package by.bsuir.digitalsignal.model;

import java.util.Arrays;

import by.bsuir.digitalsignal.math.MathFunctions;

public abstract class GraphSignalData implements Cloneable {
    
    protected int numberChannels;
    protected int sampleSize;
    protected int spectreLinesCount;
    protected int cutoffFrequencies;
    protected float frequencyResolution;
    protected float dataBlockReceiveTime;
    protected int collectionTime;
    protected int countCollectedBlocks;
    protected int totalDataLength;
    protected int totalBlocksReceived;
    protected float upperDataLimit;
    protected float lowerDataLimit;
    private int timeSize;
    private float horizontalPinchRatio = 1;
    private float verticalPinchRatio;
    
    private float preferableLimit;
    private float dataLimit;
    private float spectrumLimit;
    private float lineSpectrumLimit;
    
    private float[] realDataArrays;
    private float[] pinchedData;
    protected float[] data;
    int currentChannelNumber;
    
    private CommonData commonData;

    private float maxSignal;
    private float minSignal;
    private float rms;
    private float amplitude;
    private float crestFactor;
    private Float эксцесс;
    private Float коэфАссиметрии;

    protected GraphSignalData(CommonData commonData) {
        this.commonData = commonData;
        this.currentChannelNumber = 0;
    }

    public float[] getDataCopy() {
        return Arrays.copyOf(data, data.length);
    }

    protected GraphSignalData(GraphSignalData signal) {
        this.commonData = signal.getCommonData();
        this.currentChannelNumber = signal.currentChannelNumber;
        this.numberChannels = signal.numberChannels;
        this.sampleSize = signal.sampleSize;
        this.spectreLinesCount = signal.spectreLinesCount;
        this.cutoffFrequencies = signal.cutoffFrequencies;
        this.frequencyResolution = signal.frequencyResolution;
        this.dataBlockReceiveTime = signal.dataBlockReceiveTime;
        this.collectionTime = signal.collectionTime;
        this.countCollectedBlocks = signal.countCollectedBlocks;
        this.totalDataLength = signal.totalDataLength;
        this.totalBlocksReceived = signal.totalBlocksReceived;
        this.upperDataLimit = signal.upperDataLimit;
        this.lowerDataLimit = signal.lowerDataLimit;
        this.data = signal.data;
    }

    protected float[] arrayCopy(GraphSignalData srcSignal) {
        int pos = 0;
        int size = srcSignal.getDataSize();
        this.data = new float[srcSignal.data.length];
        int left = this.data.length - size;
        float[] srcData;
        if (left == 0) {
            srcData = srcSignal.readData(pos, size);
            System.arraycopy(srcData, 0, this.data, pos, size);
            return this.data;
        } else {
            while (left > 0) {
                srcData = srcSignal.readData(pos, size);
                System.arraycopy(srcData, 0, this.data, pos, size);
                pos += size;
                left -= size;
            }

            srcData = srcSignal.readData(pos, size);
            System.arraycopy(srcData, 0, this.data, pos, size + left);
            return this.data;
        }
    }

    protected void initData() {
        float dataLimits;
        if (Math.abs(this.upperDataLimit) < Math.abs(this.lowerDataLimit)) {
            dataLimits = Math.abs(this.lowerDataLimit);
        } else {
            dataLimits = Math.abs(this.upperDataLimit);
        }

        if (dataLimits > this.commonData.maxDataLimit) {
            this.commonData.maxDataLimit = dataLimits;
        }

        int dataLength = this.totalDataLength;
        if (dataLength > this.commonData.maxDataLength) {
            this.commonData.maxDataLength = dataLength;
        }
    }

    public boolean calculateNextPart() {
        return true;
    }

    public int getPercent() {
        return 100;
    }

    public int getTimeFieldSize() {
        return this.timeSize == 0 ? this.sampleSize : this.timeSize;
    }

    public float getLimit() {
        if (this.commonData.areaType == AreaType.SPECTRE) {
            return this.getSpectrumLimit();
        } else if (this.commonData.areaType == AreaType.LINEAR_SPECTRE) {
            return this.getLineSpectrumLimit();
        } else {
            return this.getDataLimit();
        }
    }

    public void setPreferableLimit(float preferableLimit) {
        this.preferableLimit = preferableLimit;
    }

    protected void setLimit(float dataLimit) {
        switch (this.commonData.areaType) {
            case SPECTRE:
                this.setSpectrumLimit(dataLimit);
                break;
            case LINEAR_SPECTRE:
                this.setLineSpectrumLimit(dataLimit);
                break;
            default:
                this.setDataLimit(dataLimit);
        }
    }

    private float getDataLimit() {
        if (this.preferableLimit == 0) {
            return this.dataLimit == 0 ? this.commonData.getDataLimit() : this.dataLimit;
        } else {
            return preferableLimit;
        }
    }

    private void setDataLimit(float dataLimit) {
        this.dataLimit = dataLimit;
    }

    private float getSpectrumLimit() {
        return this.commonData.getSpectreLimit() == 0 ? this.spectrumLimit : this.commonData.getSpectreLimit();
    }

    private void setSpectrumLimit(float spectrumLimit) {
        this.spectrumLimit = spectrumLimit;
    }

    private float getLineSpectrumLimit() {
        return this.commonData.getLineSpectreLimit() == 0 ? this.lineSpectrumLimit : this.commonData.getLineSpectreLimit();
    }

    private void setLineSpectrumLimit(float lineSpectrumLimit) {
        this.lineSpectrumLimit = lineSpectrumLimit;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
        this.initData();
    }

    public CommonData getCommonData() {
        return this.commonData;
    }

    public int getDisplacement() {
        return this.commonData.dataDisplacement;
    }

    public AreaType getAreaType() {
        return this.commonData.areaType;
    }

    public String toString() {
        return Integer.toString(this.currentChannelNumber);
    }

    public String getInfo() {
        String baseInfo = String.format("Макс. значение: %.4f     Мин. значение: %.4f     Размах: %.4f     СКЗ: %.4f     " +
                "Пик-фактор: %.4f", this.maxSignal, this.minSignal, this.amplitude, this.rms, this.crestFactor);
        if (this.эксцесс != null && this.коэфАссиметрии != null) {
            baseInfo += String.format("     Эксцесс: %.4f     Коэф.ассиметрии: %.4f", this.эксцесс, this.коэфАссиметрии);
        }
        return baseInfo;
    }

    public float getVerticalPinchRatio() {
        return this.verticalPinchRatio;
    }

    public float getHorizontalPinchRatio() {
        return this.horizontalPinchRatio;
    }

    private int getDataSize() {
        if (this.timeSize != 0) {
            return this.timeSize;
        } else {
            return this.commonData.getTimeFieldSize() != 0 ? this.commonData.getTimeFieldSize() : this.sampleSize;
        }
    }

    protected float getDiscrPeriod() {
        return 1 / (this.frequencyResolution * this.sampleSize);
    }

    public float getFrequencyResolution() {
        return this.frequencyResolution * this.sampleSize / this.getDataSize();
    }

    public void setWindowSize(int width, int height) {
        int size = this.getDataSize();
        this.realDataArrays = this.readData(this.commonData.dataDisplacement, size);

        this.maxSignal = Float.MIN_VALUE;
        this.minSignal = Float.MAX_VALUE;
        for (float realData : realDataArrays) {
            if (realData > this.maxSignal) {
                this.maxSignal = realData;
            }
            if (realData < this.minSignal) {
                this.minSignal = realData;
            }
        }

        this.rms = MathFunctions.rootMeanSquare(this.realDataArrays);
        this.amplitude = MathFunctions.getAmplitude(this.realDataArrays);
        this.crestFactor = this.amplitude / this.rms;
        this.setPinchedData(width, height);
    }

    private void setPinchedData(int width, int height) {
        this.эксцесс = null;
        this.коэфАссиметрии = null;
        if (this.commonData.areaType == AreaType.SPECTRE) {
            this.horizontalPinchRatio = this.getFrequencyResolution();
            float[] spectre = MathFunctions.amplitudeSpectrum(this.realDataArrays);
            if (width < spectre.length) {
                this.realDataArrays = new float[width];
                System.arraycopy(spectre, 0, this.realDataArrays, 0, this.realDataArrays.length);
            } else {
                this.realDataArrays = spectre;
            }

            for (float f : spectre) {
                if (this.spectrumLimit < Math.abs(f) * 1.05) {
                    this.spectrumLimit = Math.abs(f) * 1.05F;
                }
            }

            this.verticalPinchRatio = this.getSpectrumLimit() / height;
            this.pinchedData = new float[this.realDataArrays.length];

            for (int i = 0; i < this.realDataArrays.length; ++i) {
                this.pinchedData[i] = this.realDataArrays[i] / this.verticalPinchRatio;
            }
        } else if (this.commonData.areaType == AreaType.LINEAR_SPECTRE) {
            this.horizontalPinchRatio = this.getFrequencyResolution();
            float[] spectre = MathFunctions.amplitudeSpectrum(this.realDataArrays);
            if (width < spectre.length) {
                this.realDataArrays = new float[width];
                System.arraycopy(spectre, 0, this.realDataArrays, 0, this.realDataArrays.length);
            } else {
                this.realDataArrays = spectre;
            }

            MathFunctions.calculateRmsForLinearFrequencies(this.realDataArrays, LinearFrequency.getArrayFreq(), this.horizontalPinchRatio);

            for (float f : this.realDataArrays) {
                if (this.lineSpectrumLimit < Math.abs(f) * 1.05F) {
                    this.lineSpectrumLimit = Math.abs(f) * 1.05F;
                }
            }

            this.verticalPinchRatio = this.getLineSpectrumLimit() / height;
            this.pinchedData = new float[this.realDataArrays.length];

            for (int i = 0; i < this.realDataArrays.length; ++i) {
                this.pinchedData[i] = this.realDataArrays[i] / this.verticalPinchRatio;
            }
        } else if (this.commonData.areaType == AreaType.HISTOGRAM) {
            int step = this.commonData.getHorizontalGridSpacing();
            int numberOfRanges = this.commonData.getHistogramNumberOfRanges();
            this.horizontalPinchRatio = this.getDataLimit() * 2 / (numberOfRanges * step);
            
            float[] amplitudeBounds = new float[numberOfRanges + 1];
            amplitudeBounds[0] = -this.getDataLimit();
            float boundStep = 2 * this.getDataLimit() / numberOfRanges;

            for (int i = 1; i <= numberOfRanges; ++i) {
                amplitudeBounds[i] = amplitudeBounds[i - 1] + boundStep;
            }

            float[] histogramData = new float[numberOfRanges];
            MathFunctions.calculateHistogramData(this.realDataArrays, histogramData, amplitudeBounds);
            this.эксцесс = MathFunctions.calculateЭксцесс(this.realDataArrays, histogramData, amplitudeBounds);
            this.коэфАссиметрии = MathFunctions.calculateКоэфАссиметрии(this.realDataArrays, histogramData, amplitudeBounds);

            this.realDataArrays = new float[numberOfRanges * step];

            int j = -1;
            for (int i = 0; i < this.realDataArrays.length; ++i) {
                if (i % step == 0) {
                    ++j;
                }
                this.realDataArrays[i] = histogramData[j];
            }

            float maxValue = 0;
            for (float value : this.realDataArrays) {
                if (maxValue < Math.abs(value) * 1.05) {
                    maxValue = Math.abs(value) * 1.05F;
                }
            }

            this.verticalPinchRatio = maxValue / height;
            this.pinchedData = new float[this.realDataArrays.length];

            for (int i = 0; i < this.realDataArrays.length; ++i) {
                this.pinchedData[i] = this.realDataArrays[i] / this.verticalPinchRatio;
            }
        } else {
            this.horizontalPinchRatio = this.getDiscrPeriod();
            this.verticalPinchRatio = this.getDataLimit() * 2 / height;
            this.pinchedData = new float[this.realDataArrays.length];

            for (int step = 0; step < this.realDataArrays.length; ++step) {
                this.pinchedData[step] = this.realDataArrays[step] / this.verticalPinchRatio;
            }
        }
    }

    public float[] getPinchedData() {
        return this.pinchedData;
    }

    public float getRealData(int index) {
        return index < this.realDataArrays.length ? this.realDataArrays[index] : 0.0F;
    }

    public float[] readData(int dataIndex, int dataLength) {
        float[] data = new float[dataLength];
        for (int i = 0; i < dataLength && dataIndex + i < this.data.length; ++i) {
            data[i] = this.data[dataIndex + i];
        }
        return data;
    }

    public boolean isTransformedCapacity() {
        return true;
    }

    public int getTotalLength() {
        return this.data.length;
    }

    @Override
    public GraphSignalData clone() throws CloneNotSupportedException {
        return (GraphSignalData) super.clone();
    }

    public int getNumberChannels() {
        return numberChannels;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public int getSpectreLinesCount() {
        return spectreLinesCount;
    }

    public int getCutoffFrequencies() {
        return cutoffFrequencies;
    }

    public float getDataBlockReceiveTime() {
        return dataBlockReceiveTime;
    }

    public int getCollectionTime() {
        return collectionTime;
    }

    public int getCountCollectedBlocks() {
        return countCollectedBlocks;
    }

    public float[] getData() {
        return data;
    }
}
