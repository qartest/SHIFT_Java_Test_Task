package qartest.work;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class Main {
    public static Path path = null;
    public static String prefix = "";

    public static boolean fullStatic = false;
    public static boolean minimalStatic = false;
    public static boolean append = false;

    public static final String INTEGER_FILE_NAME = "integers.txt";
    public static final String FLOAT_FILE_NAME = "floats.txt";
    public static final String STRING_FILE_NAME = "strings.txt";

    public static File INTEGER_FILE;
    public static File FLOAT_FILE;
    public static File STRING_FILE;

    public static BufferedWriter INTEGER_OUTPUT = null;
    public static BufferedWriter FLOAT_OUTPUT = null;
    public static BufferedWriter STRING_OUTPUT = null;

    public static ArrayList<String> fileNames = new ArrayList<>();

    public static void main(String[] args) {

        readArgs(args);
        Statistics statistics = new Statistics(fullStatic, minimalStatic);
        createFile();

        String nowString;

        for(String name : fileNames){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)))) {
                Double FLOAT_DIGIT;
                Long INT_DIGIT;

                while ((nowString = reader.readLine()) != null){
                    FLOAT_DIGIT = null;
                    INT_DIGIT = null;

                    try{
                        INT_DIGIT = Long.valueOf(nowString);
                        if(INTEGER_OUTPUT == null){
                            INTEGER_OUTPUT = openBuffer(INTEGER_FILE);
                        }
                        INTEGER_OUTPUT.write(nowString + "\n");
                        statistics.createStats(INT_DIGIT);

                    }catch (NumberFormatException ignore ){}

                    if(INT_DIGIT == null){
                        try{
                            FLOAT_DIGIT = Double.valueOf(nowString);
                            if(FLOAT_OUTPUT == null){
                                FLOAT_OUTPUT = openBuffer(FLOAT_FILE);
                            }
                            FLOAT_OUTPUT.write(nowString + "\n");
                            statistics.createStats(FLOAT_DIGIT);

                        }catch (NumberFormatException ignore){}
                    }

                    if(FLOAT_DIGIT == null && INT_DIGIT == null){
                        if(STRING_OUTPUT == null){
                            STRING_OUTPUT = openBuffer(STRING_FILE);
                        }
                        STRING_OUTPUT.write(nowString + "\n");
                        statistics.createStats(nowString);
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println("Файл не открыт. Имя файла - " + name);
            } catch (IOException e){
                System.out.println("Ошибка в прочтении строки файла. Имя файла - " + name);
            }
        }

        statistics.printStats();
        closeFiles();

    }

    public static void readArgs(String[] args){
        Iterator<String> iter = Arrays.stream(args).iterator();
        String nowString;

        while (iter.hasNext()){
            nowString = iter.next();

            switch (nowString) {
                case "-p" -> prefix = iter.hasNext() ? iter.next() : "";
                case "-o" -> {
                    if (!iter.hasNext()) {
                        System.out.println("Для опции -o требуется путь");
                        continue;
                    }
                    Path maybePath = Paths.get(iter.next());

                    if (!Files.exists(maybePath)) {
                        System.out.println("Путь не существует: " + maybePath);
                        continue;
                    }
                    if (!Files.isDirectory(maybePath)) {
                        System.out.println("Указанный путь не является директорией: " + maybePath);
                        continue;
                    }
                    if (!Files.isWritable(maybePath)) {
                        System.out.println("Нет прав на запись в директорию: " + maybePath);
                        continue;
                    }
                    path = maybePath;
                }
                case "-a" -> append = true;
                case "-s" -> minimalStatic = true;
                case "-f" -> fullStatic = true;
                default -> fileNames.add(nowString);
            }
        }
    }


    public static BufferedWriter openBuffer(File file){
        try{
            if(!file.exists()){
                file.createNewFile();
                if(append){
                    System.out.println("Проблема с добавлением данных в файл. Файла с именем - " + file.getAbsolutePath() + " не существует. Создал новый файл");
                }
            }
            return new BufferedWriter(new FileWriter(file, append));
        } catch (IOException e){
            System.out.println("Не удалось создать или открыть файл: " + file.getAbsolutePath());
            throw new RuntimeException();
        }
    }

    private static void closeFiles(){
        if(INTEGER_OUTPUT != null){
            try {
                INTEGER_OUTPUT.close();
            } catch (IOException e) {
                System.out.println("Не удалось закрыть файл вывода: " + INTEGER_FILE.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }

        if(FLOAT_OUTPUT != null){
            try{
                FLOAT_OUTPUT.close();
            }catch (IOException e) {
                System.out.println("Не удалось закрыть файл вывода: " + FLOAT_FILE.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }

        if(STRING_OUTPUT != null){
            try {
                STRING_OUTPUT.close();
            }catch (IOException e) {
                System.out.println("Не удалось закрыть файл вывода: " + STRING_FILE.getAbsolutePath());
                throw new RuntimeException(e);
            }
        }

    }

    private static void createFile(){
        if(path == null){
            path = Paths.get(new File(".").getAbsolutePath());
        }

        INTEGER_FILE = path.resolve(prefix + INTEGER_FILE_NAME).toFile();
        FLOAT_FILE = path.resolve( prefix + FLOAT_FILE_NAME).toFile();
        STRING_FILE = path.resolve(prefix + STRING_FILE_NAME ).toFile();
    }

}