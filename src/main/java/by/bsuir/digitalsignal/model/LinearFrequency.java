package by.bsuir.digitalsignal.model;

public class LinearFrequency {

    private static float[] arrayFreq = new float[]{0.0F, 100.0F, 200.0F, 300.0F, 400.0F, 500.0F, 600.0F, 700.0F, 800.0F, 900.0F, 1000.0F};

    public static float[] getArrayFreq() {
        return arrayFreq.clone();
    }

    public static void setArrayFreq(float[] arrayFreq) {
        LinearFrequency.arrayFreq = arrayFreq;
    }
}
