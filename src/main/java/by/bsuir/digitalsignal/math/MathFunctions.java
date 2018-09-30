package by.bsuir.digitalsignal.math;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MathFunctions {

    private static final float[] SIN_TABLE = prepareSinTable();
    private static final int SIN_TABLE_SIZE = 32768;

    private static float[] prepareSinTable() {
        float[] table = new float[SIN_TABLE_SIZE];
        float twoPiDivideTableSize = (float) (2 * Math.PI / SIN_TABLE_SIZE);

        for (int q = 0; q < SIN_TABLE_SIZE; ++q) {
            table[q] = (float) Math.sin(twoPiDivideTableSize * q);
        }

        return table;
    }

    public static float[] amplitudeSpectrum(float[] signalData) {
        int signalDataSize = signalData.length;
        float[] arrayImagine = new float[signalDataSize];
        float[] arrayReal = new float[signalDataSize];
        System.arraycopy(signalData, 0, arrayReal, 0, signalDataSize);

        fft(arrayReal, arrayImagine);
        int spectrumSize = signalDataSize / 2;

        float[] spectrum = new float[spectrumSize];
        for (int i = 1; i < spectrumSize; ++i) {
            spectrum[i] = (float) (Math.sqrt(arrayReal[i] * arrayReal[i] + arrayImagine[i] * arrayImagine[i]) * 2);
        }

        return spectrum;
    }

    private static void fft(float[] arrayReal, float[] arrayImagine) {
        int dataSize = arrayReal.length;
        int scale = SIN_TABLE_SIZE / dataSize;
        int numberOfBits = (int) (Math.log10(dataSize) / Math.log10(2));

        int i;
        int positionInGroup;
        int groupIndex;
        for (i = 1; i < dataSize - 2; ++i) {
            positionInGroup = i;
            groupIndex = 0;

            for (int counter = 0; counter < numberOfBits; ++counter) {
                int isNotOdd = positionInGroup & 1;
                positionInGroup >>= 1;
                groupIndex <<= 1;
                groupIndex |= isNotOdd;
            }

            if (i < groupIndex) {
                float temp = arrayReal[groupIndex];
                arrayReal[groupIndex] = arrayReal[i];
                arrayReal[i] = temp;
                temp = arrayImagine[groupIndex];
                arrayImagine[groupIndex] = arrayImagine[i];
                arrayImagine[i] = temp;
            }
        }

        int groupsCount = dataSize >> 1;
        int firstInGroupLeap = 2;
        int leap = 1;
        int quarterOfDataSize = SIN_TABLE_SIZE / 4;

        for (int v = 0; v < numberOfBits; ++v) {
            for (int counter = 0; counter < dataSize; counter += firstInGroupLeap) {
                int rotation = 0;
                i = counter;

                for (positionInGroup = 0; positionInGroup < leap; ++positionInGroup) {
                    groupIndex = i + leap;
                    int sinAngle = scale * rotation;
                    int cosAngle = (sinAngle + quarterOfDataSize) % SIN_TABLE_SIZE;

                    float realRoot = arrayReal[groupIndex] * SIN_TABLE[cosAngle] - arrayImagine[groupIndex] * SIN_TABLE[sinAngle];
                    float imagineRoot = arrayImagine[groupIndex] * SIN_TABLE[cosAngle] + arrayReal[groupIndex] * SIN_TABLE[sinAngle];

                    arrayReal[groupIndex] = arrayReal[i] - realRoot;
                    arrayReal[i] += realRoot;

                    arrayImagine[groupIndex] = arrayImagine[i] - imagineRoot;
                    arrayImagine[i] += imagineRoot;

                    ++i;
                    rotation += groupsCount;
                }
            }

            firstInGroupLeap <<= 1;
            groupsCount >>= 1;
            leap <<= 1;
        }

        for (int counter = 0; counter < dataSize; ++counter) {
            arrayReal[counter] /= dataSize;
            arrayImagine[counter] /= dataSize;
        }
    }

    private static void ifft(float[] arrayReal, float[] arrayImagine) {
        int dataSize = arrayReal.length;
        int scale = SIN_TABLE_SIZE / dataSize;
        int numberOfBits = (int) (Math.log10(dataSize) / Math.log10(2));

        int i;
        int positionInGroup;
        int groupIndex;
        for (i = 1; i < dataSize - 2; ++i) {
            positionInGroup = i;
            groupIndex = 0;

            for (int counter = 0; counter < numberOfBits; ++counter) {
                int i1 = positionInGroup & 1;
                positionInGroup >>= 1;
                groupIndex <<= 1;
                groupIndex |= i1;
            }

            if (i < groupIndex) {
                float ip = arrayReal[groupIndex];
                arrayReal[groupIndex] = arrayReal[i];
                arrayReal[i] = ip;
                ip = arrayImagine[groupIndex];
                arrayImagine[groupIndex] = arrayImagine[i];
                arrayImagine[i] = ip;
            }
        }

        int groupsCount = dataSize >> 1;
        int firstInGroupLeap = 2;
        int leap = 1;
        int quarterOfDataSize = SIN_TABLE_SIZE / 4;

        for (int v = 0; v < numberOfBits; ++v) {
            for (int counter = 0; counter < dataSize; counter += firstInGroupLeap) {
                int rotation = 0;
                i = counter;

                for (positionInGroup = 0; positionInGroup < leap; ++positionInGroup) {
                    groupIndex = i + leap;
                    int sinAngle = scale * rotation;
                    int cosAngle = (sinAngle + quarterOfDataSize) % SIN_TABLE_SIZE;

                    float realRoot = arrayReal[groupIndex] * SIN_TABLE[cosAngle] + arrayImagine[groupIndex] * SIN_TABLE[sinAngle];
                    float imagineRoot = arrayImagine[groupIndex] * SIN_TABLE[cosAngle] - arrayReal[groupIndex] * SIN_TABLE[sinAngle];

                    arrayReal[groupIndex] = arrayReal[i] - realRoot;
                    arrayReal[i] += realRoot;

                    arrayImagine[groupIndex] = arrayImagine[i] - imagineRoot;
                    arrayImagine[i] += imagineRoot;
                    ++i;
                    rotation += groupsCount;
                }
            }

            firstInGroupLeap <<= 1;
            groupsCount >>= 1;
            leap <<= 1;
        }
    }

    public static float[] filterSignalByFrequency(float[] signal, Integer minFrequency, Integer maxFrequency) {
        int signalSize = signal.length;
        float[] arrayReal = new float[signalSize];
        float[] arrayImagine = new float[signalSize];
        System.arraycopy(signal, 0, arrayReal, 0, signalSize);
        fft(arrayReal, arrayImagine);

        for (int i = 1; i < signalSize >> 1; ++i) {
            if (shouldBeFiltered(i, minFrequency, maxFrequency)) {
                arrayReal[i] = 0;
                arrayImagine[i] = 0;
                arrayReal[signalSize - i] = 0;
                arrayImagine[signalSize - i] = 0;
            }
        }

        arrayReal[0] = 0;
        arrayImagine[0] = 0;
        arrayReal[signalSize >> 1] = 0;
        arrayImagine[signalSize >> 1] = 0;
        ifft(arrayReal, arrayImagine);
        return arrayReal;
    }

    private static boolean shouldBeFiltered(int i, Integer minFrequency, Integer maxFrequency) {
        if (minFrequency != null && maxFrequency != null) {
            return i < minFrequency || i > maxFrequency;
        } else if (minFrequency != null) {
            return i < minFrequency;
        } else {
            return i > maxFrequency;
        }
    }

    public static float rootMeanSquare(float[] inputData) {
        int dataSize = inputData.length;
        float sumOfSquares = 0;
        float sumOfConst = 0;

        for (float value : inputData) {
            sumOfSquares += value * value;
            sumOfConst += value;
        }

        float squaresSig = sumOfSquares / dataSize;
        float constSig = sumOfConst / dataSize;
        float squaresConstSig = constSig * constSig;
        float res = squaresSig - squaresConstSig;
        return (float) Math.sqrt(res);
    }

    public static float getAmplitude(float[] inputData) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for (float value : inputData) {
            if (min > value) {
                min = value;
            }
            if (max < value) {
                max = value;
            }
        }

        return Math.abs(max) + Math.abs(min);
    }

    public static void calculateRmsForLinearFrequencies(float[] values, float[] linearFrequencies, float horizontalPitchRatio) {
        int firstGroupSize = Math.round(linearFrequencies[0] / horizontalPitchRatio);
        for (int i = 0; i < firstGroupSize; ++i) {
            values[i] = 0;
        }

        int j1 = 0;
        for (int i = 0; i < linearFrequencies.length - 1; ++i) {
            float p = 0;
            j1 = firstGroupSize;

            while (firstGroupSize * horizontalPitchRatio < linearFrequencies[i + 1]) {
                p += values[firstGroupSize] * values[firstGroupSize];
                ++firstGroupSize;
                if (firstGroupSize >= values.length) {
                    break;
                }
            }

            for (p = (float) Math.sqrt(p); j1 < firstGroupSize; ++j1) {
                values[j1] = (float) (p / Math.sqrt(2));
            }

            if (firstGroupSize >= values.length) {
                break;
            }
        }

        while (j1 < values.length) {
            values[j1++] = 0;
        }
    }

    private static void deleteConstantComponent(float[] data) {
        float sum = 0;
        for (float value : data) {
            sum += value;
        }

        float meanValue = data.length > 0 ? sum / data.length : 0;
        for (int i = 0; i < data.length; ++i) {
            data[i] -= meanValue;
        }
    }

    public static void integrate(float[] data, float deltaT) {
        deleteConstantComponent(data);
        float currentValue = data[0];

        for (int i = 1; i < data.length; ++i) {
            float previousValue = currentValue;
            currentValue = data[i];
            data[i] = data[i - 1] + (currentValue + previousValue) / 2;
        }

        deleteConstantComponent(data);
        float sr_p = 1000 * deltaT;
        for (int i = 0; i < data.length; ++i) {
            data[i] *= sr_p;
        }
    }

    private static double calculateOpredelitel3Degree(double[][] x) {
        return x[0][0] * x[1][1] * x[2][2]
                + x[1][0] * x[2][1] * x[0][2]
                + x[0][1] * x[1][2] * x[2][0]
                - x[0][2] * x[1][1] * x[2][0]
                - x[0][0] * x[1][2] * x[2][1]
                - x[1][0] * x[0][1] * x[2][2];
    }

    public static int deleteTrend(float[] data, int trendDegree) {
        if (trendDegree <= 0) {
            return -1;
        } else {
            int dataSize = data.length;
            double s00 = dataSize;
            double s10 = dataSize / 2.0 * (dataSize - 1);
            double s11 = dataSize / 6.0 * (dataSize - 1) * (2 * dataSize - 1);

            double r0 = 0;
            double r1 = 0;
            for (int i = 0; i < dataSize; ++i) {
                r0 += data[i];
                r1 += data[i] * i;
            }

            double c0;
            double c1;
            if (trendDegree == 1) {
                double p = s10 * s10 - s00 * s11;
                if (p != 0) {
                    c0 = (r1 * s10 - r0 * s11) / p;
                    c1 = (r0 * s10 - r1 * s00) / p;
                } else {
                    c0 = 0;
                    c1 = 0;
                }

                for (int i = 0; i < dataSize; ++i) {
                    data[i] -= c0 + c1 * i;
                }

                return 1;
            } else if (trendDegree == 2) {
                double c2;
                double s12 = Math.pow(dataSize, 2) * Math.pow(dataSize - 1, 2) / 6;
                double s22 = dataSize / 30.0F * (dataSize - 1) * (2 * dataSize - 1) * (3 * Math.pow(dataSize - 1, 2) + 3 * (dataSize - 1) - 1);

                double r2 = 0;
                for (int i = 0; i < dataSize; ++i) {
                    r2 += data[i] * Math.pow(i, 2);
                }

                double[][] x = new double[3][3];
                x[0][0] = s00;
                x[0][1] = s10;
                x[0][2] = s11;
                x[1][0] = s10;
                x[1][1] = s11;
                x[1][2] = s12;
                x[2][0] = s11;
                x[2][1] = s12;
                x[2][2] = s22;
                double d = calculateOpredelitel3Degree(x);
                if (d != 0) {
                    x[0][0] = r0;
                    x[1][0] = r1;
                    x[2][0] = r2;
                    double d0 = calculateOpredelitel3Degree(x);
                    x[0][0] = s00;
                    x[1][0] = s10;
                    x[2][0] = s11;
                    x[0][1] = r0;
                    x[1][1] = r1;
                    x[2][1] = r2;
                    double d1 = calculateOpredelitel3Degree(x);
                    x[0][1] = s10;
                    x[1][1] = s11;
                    x[2][1] = s12;
                    x[0][2] = r0;
                    x[1][2] = r1;
                    x[2][2] = r2;
                    double d2 = calculateOpredelitel3Degree(x);
                    c0 = d0 / d;
                    c1 = d1 / d;
                    c2 = d2 / d;
                } else {
                    c0 = 0;
                    c1 = 0;
                    c2 = 0;
                }

                if (!Double.isNaN(c0) && !Double.isNaN(c1) && !Double.isNaN(c2)) {
                    for (int i = 0; i < dataSize; ++i) {
                        data[i] -= c0 + c1 * i + c2 * Math.pow(i, 2);
                    }

                    return 2;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }
    }

    public static void calculateHistogramData(float[] data, float[] histogramData, float[] amplitudeBounds) {
        int dataLength = data.length;
        int amplitudeBoundsLength = amplitudeBounds.length - 1;

        for (int i = 0; i < amplitudeBoundsLength; ++i) {
            histogramData[i] = 0;
        }

        int j = 0;
        while (j < dataLength) {
            float p = data[j];

            for (int i = 1; i <= amplitudeBoundsLength; ++i) {
                if (p <= amplitudeBounds[i]) {
                    ++histogramData[i - 1];
                    break;
                }
            }
            ++j;
        }

        for (int i = 0; i < amplitudeBoundsLength; ++i) {
            histogramData[i] /= dataLength;
        }
    }

    public static float calculateЭксцесс(float[] data, float[] histogramData, float[] amplitudeBounds) {
        return calculateЭксцессИлиАссиметрию(data, histogramData, amplitudeBounds, 4) - 3;
    }

    public static float calculateКоэфАссиметрии(float[] data, float[] histogramData, float[] amplitudeBounds) {
        return calculateЭксцессИлиАссиметрию(data, histogramData, amplitudeBounds, 3);
    }

    private static float calculateЭксцессИлиАссиметрию(float[] data, float[] histogramData, float[] amplitudeBounds, float pow) {
        int dataLength = data.length;
        List<Float> inputData = new ArrayList<>(dataLength);
        for (float value : histogramData) {
            inputData.add(value);
        }

        List<Float> realData = inputData.stream()
                .map(value -> value * dataLength)
                .collect(Collectors.toList());
        List<Float> meanBound = new ArrayList<>(realData.size());
        for (int i = 0; i < realData.size(); i++) {
            meanBound.add((amplitudeBounds[i] + amplitudeBounds[i + 1]) / 2);
        }

        float meanArithmetic = 0;
        for (int i = 0; i < realData.size(); i++) {
            meanArithmetic += realData.get(i) * meanBound.get(i);
        }
        meanArithmetic /= dataLength;

        float centerMomentOf4Degree = calculateCenterMoment(realData, meanBound, meanArithmetic, (int) pow) / dataLength;
        float centerMomentOf2Degree = calculateCenterMoment(realData, meanBound, meanArithmetic, 2) / dataLength;

        return (float) (centerMomentOf4Degree / Math.pow(centerMomentOf2Degree, pow / 2));
    }

    private static float calculateCenterMoment(List<Float> realData, List<Float> meanBound, float meanArithmetic, int pow) {
        float centerMoment = 0;
        for (int i = 0; i < meanBound.size(); i++) {
            centerMoment += (float) Math.pow(meanBound.get(i) - meanArithmetic, pow) * realData.get(i);
        }
        return centerMoment;
    }
}
