public class printer {

    private int tonerlevel;
    private int pagesprinted;
    private boolean duplex;

    public printer(int tonerlevel, boolean duplex) {
        this.pagesprinted = 0;
        this.tonerlevel = (tonerlevel >= 0 && tonerlevel <=100) ? tonerlevel : -1;
        this.duplex = duplex;
    }

    public int addToner(int tonerAmount){
        int tempAmount = tonerlevel + tonerAmount;
        if(tempAmount>100 || tempAmount < 0){
            return -1;
        }
        tonerlevel += tonerAmount;
        return tonerlevel;
    }

    public int printPages(int pages){
        int jobPages = (duplex) ? (pages/2) + (pages%2) : pages;
        pagesprinted += jobPages;
        return jobPages;
    }

    public int getPagesprinted() {
        return pagesprinted;
    }
}
