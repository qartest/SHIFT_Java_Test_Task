package qartest.work;

public class Statistics {
    private long INTEGER_DIGITS = 0;
    private long FLOAT_DIGITS = 0;
    private long STRINGS = 0;

    private long INTEGER_SUM = 0;
    private long INTEGER_MAX = Long.MIN_VALUE;
    private long INTEGER_MIN = Long.MAX_VALUE;

    private double FLOAT_SUM = 0;
    private double FLOAT_MAX = Double.MIN_VALUE;
    private double FLOAT_MIN = Double.MAX_VALUE;

    private long MAX_STRING_LEN = Long.MIN_VALUE;
    private long MIN_STRING_LEN = Long.MAX_VALUE;

    private final StatsMode mode;

    public Statistics(boolean fullStatic, boolean minimalStatic){
        if(fullStatic){
            mode = StatsMode.full;
        }
        else if(minimalStatic){
            mode = StatsMode.minimum;
        }
        else{
            mode = StatsMode.none;
        }
    }

    public void createStats(Long input){
        if(mode == StatsMode.none){
            return;
        }

        if(mode == StatsMode.full){
            INTEGER_SUM += input;
            INTEGER_MAX = Long.max(INTEGER_MAX, input);
            INTEGER_MIN = Long.min(INTEGER_MIN, input);
        }
        INTEGER_DIGITS++;
    }

    public void createStats(Double input){
        if(mode == StatsMode.none){
            return;
        }

        if(mode == StatsMode.full){
            FLOAT_SUM += input;
            FLOAT_MAX = Double.max(FLOAT_MAX, input);
            FLOAT_MIN = Double.min(FLOAT_MIN, input);
        }
        FLOAT_DIGITS++;
    }

    public void createStats(String input){
        if(mode == StatsMode.none){
            return;
        }

        if(mode == StatsMode.full){
            MAX_STRING_LEN = Long.max(MAX_STRING_LEN, input.length());
            MIN_STRING_LEN = Long.min(MIN_STRING_LEN, input.length());
        }
        STRINGS++;
    }

    public void printStats(){
        if(mode == StatsMode.none){
            return;
        }
        System.out.println("==================INTEGER========================");
        System.out.println("Количество целый чисел - " + INTEGER_DIGITS);
        if(mode == StatsMode.full){
            System.out.println("Минимальное целое значение - " + INTEGER_MIN);
            System.out.println("Максимальное целое значение - " + INTEGER_MAX);
            System.out.println("Сумма целых чисел - " + INTEGER_SUM);
            System.out.println("Среднее целых чисел - " + (double)INTEGER_SUM / (double)INTEGER_DIGITS );
        }

        System.out.println("==================FLOAT========================");
        System.out.println("Количество чисел с плавающей точкой - " + FLOAT_DIGITS);
        if(mode == StatsMode.full){
            System.out.println("Минимальное значение с плавающей точкой - " + FLOAT_MIN);
            System.out.println("Максимальное значение с плавающей точкой - " + FLOAT_MAX);
            System.out.println("Сумма чисел с плавающей точкой - " + FLOAT_SUM);
            System.out.println("Среднее чисел с плавающей точкой - " + FLOAT_SUM / (double)FLOAT_DIGITS );
        }

        System.out.println("==================STRING========================");
        System.out.println("Количество строк - " + STRINGS);
        if(mode == StatsMode.full){
            System.out.println("Размер длинной строки - " + MAX_STRING_LEN);
            System.out.println("Размер короткой строки - " + MIN_STRING_LEN);
        }
    }

}
