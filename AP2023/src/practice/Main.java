package practice;

import java.util.ArrayList;
import java.util.List;

interface Box{
    double calculatePrice();
}

class CompositeBox implements Box{
    List<Box> boxes = new ArrayList<>();

    @Override
    public double calculatePrice() {
        return boxes.stream().mapToDouble(Box::calculatePrice).sum();
    }
}

class Product implements Box{
    String title;
    double price;

    @Override
    public double calculatePrice() {
        return this.price;
    }
}

public class Main {

}
