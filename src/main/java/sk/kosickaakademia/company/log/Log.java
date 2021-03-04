package sk.kosickaakademia.company.log;

public abstract class Log {
    public static void error(String str){
        System.out.println("[ERROR] - "+str);
    }

    public static void print(String str){
        System.out.println("[OK] - "+str);
    }

    public static void info(String str){
        System.out.println("[INFO] - "+str);
    }
}
