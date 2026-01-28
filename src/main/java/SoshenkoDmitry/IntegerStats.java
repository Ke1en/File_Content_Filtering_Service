package SoshenkoDmitry;

import java.math.BigDecimal;
import java.math.MathContext;

public class IntegerStats {

    private long count = 0;
    private long minValue = Long.MAX_VALUE;
    private long maxValue = Long.MIN_VALUE;
    private BigDecimal sumValue = BigDecimal.ZERO;

    public void update(long value) {

        count++;

        if (value < minValue) {
            minValue = value;
        }

        if (value > maxValue) {
            maxValue = value;
        }

        sumValue = sumValue.add(BigDecimal.valueOf(value));

    }

    public double getAverage() {

        if (count == 0) {
            return 0;
        }

        BigDecimal average = sumValue.divide(BigDecimal.valueOf(count), MathContext.DECIMAL64);

        return average.doubleValue();

    }

    public void print() {

        System.out.println("Количество: " + count);
        System.out.println("Минимальное значение: " + minValue);
        System.out.println("Максимальное значение: " + maxValue);
        System.out.println("Сумма: " + sumValue.toPlainString());
        System.out.println("Среднее значение: " + String.format("%.5f", getAverage()));

    }

}
