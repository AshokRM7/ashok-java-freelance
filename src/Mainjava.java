public class Mainjava {
    public static void main(String[] args) {
        printer printe = new printer(50, true);
        System.out.println("Initial page count = " + printe.getPagesprinted());

        int pagesPrinted = printe.printPages(5);
        System.out.printf("Current Job  Pages: %d, Printer Total: %d %n", pagesPrinted, printe.getPagesprinted());

        pagesPrinted = printe.printPages(10);
        System.out.printf("Current Job  Pages: %d, Printer Total: %d %n", pagesPrinted, printe.getPagesprinted());
    }
}
