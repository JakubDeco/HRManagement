package sk.kosickaakademia.company.entity;

public class Statistic {
    private int count;
    private int male;
    private int female;
    private int other;
    private double age;
    private int min;
    private int max;

    public Statistic(int count, int male, int female, int other, double age, int min, int max) {
        this.count = count;
        this.male = male;
        this.female = female;
        this.other = other;
        this.age = age;
        this.min = min;
        this.max = max;
    }

    public int getCount() {
        return count;
    }

    public int getMale() {
        return male;
    }

    public int getFemale() {
        return female;
    }

    public int getOther() {
        return other;
    }

    public double getAge() {
        return age;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
