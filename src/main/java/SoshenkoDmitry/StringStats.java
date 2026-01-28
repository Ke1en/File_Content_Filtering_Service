package SoshenkoDmitry;

public class StringStats {

    private long count = 0;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = 0;

    public void update(String str) {

        count++;

        int length = str.length();

        if (length < minLength) {
            minLength = length;
        }

        if (length > maxLength) {
            maxLength = length;
        }

    }

    public void print() {

        System.out.println("Общее число строк: " + count);

        if (count > 0) {

            System.out.println("Минимальная длина строки: " + minLength);
            System.out.println("Максимальная длина строки: " + maxLength);

        }

    }

}
