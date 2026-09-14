package school21.day02.exercise1;

public class Cat extends Animal {
    public Cat(String name, int age, double weight) {
        super(name, age, weight);
    }

    @Override
    public double getFeedInfoKg() {
        return getWeight() * 0.1;
    }

    @Override
    public String toString() {
        return String.format("Cat name = %s, age = %d, mass = %.2f, feed = %.2f",
                getName(), getAge(), getWeight(), getFeedInfoKg());
    }
}
