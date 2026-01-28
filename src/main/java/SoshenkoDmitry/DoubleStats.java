package SoshenkoDmitry;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleStats {

    private long count = 0;
    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;
    private BigDecimal sumValue = BigDecimal.ZERO;

    public void update(BigDecimal value) {

        count++;

        if (minValue == null || value.compareTo(minValue) < 0) {
            minValue = value;
        }

        if (maxValue == null || value.compareTo(maxValue) > 0) {
            maxValue = value;
        }

        sumValue = sumValue.add(value);

    }

    public double getAverage() {

        if (count == 0) {
            return 0;
        }

        return sumValue.doubleValue() / count;

    }

    public void print() {

        System.out.println("Количество: " + count);
        System.out.println("Минимальное значение: " + minValue.setScale(5, RoundingMode.HALF_UP));
        System.out.println("Максимальное значение: " + maxValue.setScale(5, RoundingMode.HALF_UP));
        System.out.println("Сумма: " + sumValue.setScale(5, RoundingMode.HALF_UP));
        System.out.println("Среднее значение: " + String.format("%.5f", getAverage()));

    }

}
