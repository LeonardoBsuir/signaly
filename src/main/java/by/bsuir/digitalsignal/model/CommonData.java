package by.bsuir.digitalsignal.model;

public class CommonData {

    protected int channelNumber;
    protected int dataDisplacement;
    private float curDataLimit;
    protected float maxDataLimit;
    private float curSpectreLimit;
    private float maxSpectreLimit;
    private float curLineSpectreLimit;
    private float maxLineSpectreLimit;
    private int iTimeFieldSize = 0;
    public int maxDataLength = 0;
    public AreaType areaType;

    public CommonData() {
        this.areaType = AreaType.TIME_SIGNAL;
    }

    public void setDisplacement(int offset) {
        this.dataDisplacement = offset;
    }

    public int getDisplacement() {
        return this.dataDisplacement;
    }

    public void setAreaType(AreaType type) {
        this.areaType = type;
    }

    public float getLimit() {
        if (this.areaType == AreaType.SPECTRE) {
            return this.getSpectreLimit();
        } else if (this.areaType == AreaType.LINEAR_SPECTRE) {
            return this.getLineSpectreLimit();
        } else {
            return this.areaType == AreaType.HISTOGRAM ? this.getDataLimit() : this.getDataLimit();
        }
    }

    public void setLimit(float dataLimit) {
        if (this.areaType == AreaType.SPECTRE) {
            this.setSpectreLimit(dataLimit);
        } else if (this.areaType == AreaType.LINEAR_SPECTRE) {
            this.setLineSpectreLimit(dataLimit);
        } else if (this.areaType == AreaType.HISTOGRAM) {
            this.setDataLimit(dataLimit);
        } else {
            this.setDataLimit(dataLimit);
        }
    }

    public float getDataLimit() {
        return this.curDataLimit == 0 ? this.maxDataLimit : this.curDataLimit;
    }

    private void setDataLimit(float maxDataLimit) {
        this.curDataLimit = maxDataLimit;
    }

    public float getSpectreLimit() {
        return this.curSpectreLimit == 0.0F ? this.maxSpectreLimit : this.curSpectreLimit;
    }

    private void setSpectreLimit(float maxSpectreLimit) {
        this.curSpectreLimit = maxSpectreLimit;
    }

    public float getLineSpectreLimit() {
        return this.curLineSpectreLimit == 0 ? this.maxLineSpectreLimit : this.curLineSpectreLimit;
    }

    private void setLineSpectreLimit(float maxLineSpectreLimit) {
        this.curLineSpectreLimit = maxLineSpectreLimit;
    }

    public int getTimeFieldSize() {
        return this.iTimeFieldSize;
    }

    public void setTimeFieldSize(int iTimeFieldSize) {
        this.iTimeFieldSize = iTimeFieldSize;
    }

    public int getVerticalGridSpacing() {
        return 25;
    }

    public int getHorizontalGridSpacing() {
        return 25;
    }

    public int getHistogramNumberOfRanges() {
        return 20;
    }
}
