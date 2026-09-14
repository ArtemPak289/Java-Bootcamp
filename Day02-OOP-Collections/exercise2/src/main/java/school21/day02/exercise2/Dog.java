package school21.day02.exercise2;

public class Dog extends Animal implements Omnivore {
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public String hunt() {
        return "I can hunt for robbers";
    }

    @Override
    public String toString() {
        return "Dog name = " + getName() + ", age = " + getAge() + ". " + hunt();
    }
}
