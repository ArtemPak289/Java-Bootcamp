package school21.day02.exercise4;

import java.util.concurrent.TimeUnit;

public class Dog extends Animal {
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public double goToWalk() throws InterruptedException {
        double walkTime = getAge() * 0.5;
        TimeUnit.SECONDS.sleep((long) walkTime);
        return walkTime;
    }

    @Override
    public String toString() {
        return "Dog name = " + getName() + ", age = " + getAge();
    }
}
