package by.bsuir.digitalsignal.model;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSignalData extends GraphSignalData {
    private static final String SIGN = "TMB1";

    public FileSignalData(File f, CommonData commonData) throws IOException {
        super(commonData);
        this.currentChannelNumber = ++commonData.channelNumber;
        int i = 0;

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            StringBuilder sign = new StringBuilder();
            for (int j = 0; j < 4; ++j) {
                sign.append((char) in.readByte());
            }
            if (!sign.toString().equals(SIGN)) {
                throw new IOException("'" + f.getPath() + "' file signature error: SIGN = " + sign);
            } else {
                this.numberChannels = Integer.reverseBytes(in.readInt());
                this.sampleSize = Integer.reverseBytes(in.readInt());
                this.spectreLinesCount = Integer.reverseBytes(in.readInt());
                this.cutoffFrequencies = Integer.reverseBytes(in.readInt());
                this.frequencyResolution = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                this.dataBlockReceiveTime = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                this.collectionTime = Integer.reverseBytes(in.readInt());
                this.countCollectedBlocks = Integer.reverseBytes(in.readInt());
                this.totalDataLength = Integer.reverseBytes(in.readInt());
                this.totalBlocksReceived = Integer.reverseBytes(in.readInt());
                this.upperDataLimit = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                this.lowerDataLimit = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                this.data = new float[this.totalDataLength];

                for (i = 0; i < this.totalDataLength; ++i) {
                    this.data[i] = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                }

                super.initData();
            }
        } catch (EOFException e) {
            throw new IOException("'" + f.getPath() + "' file length error: realDataLength = " + Integer.toString(i));
        }
    }

    public FileSignalData clone() throws CloneNotSupportedException {
        return (FileSignalData) super.clone();
    }
}
