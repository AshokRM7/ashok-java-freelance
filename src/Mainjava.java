import java.util.Scanner;

public class Mainjava {
    public static void main(String[] args) {
//        printer printe = new printer(50, true);
//        System.out.println("Initial page count = " + printe.getPagesprinted());
//
//        int pagesPrinted = printe.printPages(5);
//        System.out.printf("Current Job  Pages: %d, Printer Total: %d %n", pagesPrinted, printe.getPagesprinted());
//
//        pagesPrinted = printe.printPages(10);
//        System.out.printf("Current Job  Pages: %d, Printer Total: %d %n", pagesPrinted, printe.getPagesprinted());
//
//        Movie movie = new Adventure("Starwars");
//        Movie comedymovie = new Comedy("golmaal");
//        Movie scifi = new ScienceFiction("Oblivion");
//        movie.watchMovie();
//        comedymovie.watchMovie();
//        scifi.watchMovie();
//        Movie themovie = Movie.getMovie("Adventure", "The Goat");
//        themovie.watchMovie();

        Scanner s = new Scanner(System.in);
        while(true){
            System.out.print("Enter Type (A for Adventure, C for Comedy, S for ScienceFiction, or Q to quit: ");
            String type = s.nextLine();
            if ("Qq".contains(type)){
                break;
            }
            System.out.println("Enter Movie Title: ");
            String title = s.nextLine();
            Movie movie = Movie.getMovie(type, title);
            movie.watchMovie();
        }

    }
}
