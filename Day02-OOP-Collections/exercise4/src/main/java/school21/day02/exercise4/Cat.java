package school21.day02.exercise4;

import java.util.concurrent.TimeUnit;

public class Cat extends Animal {
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public double goToWalk() throws InterruptedException {
        double walkTime = getAge() * 0.25;
        TimeUnit.SECONDS.sleep((long) walkTime);
        return walkTime;
    }

    @Override
    public String toString() {
        return "Cat name = " + getName() + ", age = " + getAge();
    }
}
