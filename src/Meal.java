public class Meal {

    private hamburger hamburger;
    private drink drink;
    private sideitem sideitem;

    public Meal() {
        System.out.println("Default meal with 1 regular burger, 1 small drink and 1 french fries ordered");
    }
}

class hamburger{
    private String type;
    private int toppings;
    private int baseprice;

    public hamburger(String type, int baseprice) {
        this.type = type;
        this.baseprice = baseprice;
    }
}

class drink{
    private String type;
    private String size;
    private int price;
}
class sideitem{
    private String type;
    private int price;
}
