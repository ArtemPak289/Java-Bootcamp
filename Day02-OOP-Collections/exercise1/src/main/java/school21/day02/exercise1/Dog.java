package school21.day02.exercise1;

public class Dog extends Animal {
    public Dog(String name, int age, double weight) {
        super(name, age, weight);
    }

    @Override
    public double getFeedInfoKg() {
        return getWeight() * 0.3;
    }

    @Override
    public String toString() {
        return String.format("Dog name = %s, age = %d, mass = %.2f, feed = %.2f",
                getName(), getAge(), getWeight(), getFeedInfoKg());
    }
}
