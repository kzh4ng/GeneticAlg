import java.util.Random;

/**
 * Created by Kevin on 11/7/2015.
 */
public class GA {

    final static double startX = 0;                                 //stating coordinates
    final static double startY = 0;

    final static int populationSize = 10;
    static Point points[] = new Point[populationSize];
    static Point pointsForCrossover[] = new Point[populationSize];
    static double numberOfPointsForCrossover = 0;

    final static double stepSize1 = 1;
    final static double stepSize2 = -1*stepSize1;

    final static int pctChanceOfMutation = 50;
    final static int pctChanceOfCrossover = 40;

    static int iterationLimit = 1;

    public static void main(String args[]){
        for(int i = 0; i < points.length; i++){                 //initializes all points to starting point and default mutation/crossover setting
            points[i] = new Point();
            points[i].setX(startX);
            points[i].setY(startY);
            points[i].setMutate(false);
            points[i].setCrossover(false);
        }
        int j = 0;
        Random r = new Random();
        int mutation;
        while(j < iterationLimit){
            for(int k = 0; k<points.length; k++){                        //determine mutation for all points
                mutation = r.nextInt(101);
                if(mutation <= pctChanceOfMutation) mutate(points[k]);
                points[k].setLocation(k);
                printPoint(points[k]);
            }
            for(int k = 0; k < points.length; k++){                     //mark some points for crossover
                int crossover = r.nextInt(101);
                if(crossover <= pctChanceOfCrossover){
                    points[k].setCrossover(true);
                    numberOfPointsForCrossover++;                                            //count is the size of the crossover array
                }
            }
            int index = 0;                                              //count of items in array of crossover points
            for(int k = 0; k < points.length; k++){                     //put all marked points into crossover array
                if(points[k].isCrossover()){
                    pointsForCrossover[index] = points[k];
                    index++;
                }
            }
            crossover();
            for(int l = 0; l < numberOfPointsForCrossover; l++){
                points[pointsForCrossover[l].getLocation()] = pointsForCrossover[l];        //put back all the points that underwent crossover
            }
            System.out.println("Points after crossover:");
            for(int i = 0; i < points.length; i++){
                printPoint(points[i]);
            }
            for(int i = 0; i < points.length; i++){
                double fitness = function(points[i].getX(),points[i].getY());               //set each point's fitness to its function output
                points[i].setFitness(fitness);
                System.out.println("Point " +i+" fitness:" + points[i].getFitness());
            }
            insertionSort(points);                                                          //sort the array by fitness
            for(int i = 0; i < points.length; i++){

            }
            j++;
        }
    }

    static double function(double x, double y){                      //returns the result of sin(4*(x^2+y^2))/4;
        double z = Math.sin(4*(Math.pow(x,2)+Math.pow(y,2)))/4;
        return z;
    }
    static Point mutate(Point p){
        Random r = new Random();
        int random = r.nextInt(11);                                 //generate a random number from one to ten
        int determineVariable = r.nextInt(2);
        double range = stepSize1 - stepSize2;                               // is the range of the mutations
        double increment = range / 10;                              //this allows for the mutation to be one of ten evenly spaced values within the range
        double mutation = random * increment;
        if(determineVariable == 0){                                             //the following if statements only allow either x or y to be mutated
            p.setX(p.getX()+mutation+stepSize2);
        }
        else{
            p.setY(p.getY()+mutation+stepSize2);
        }
        return p;
    }

    static void printPoint(Point p){
        System.out.println("Point "+p.getLocation()+" ("+p.getX()+","+p.getY()+")");
    }

    static void crossover(){
        double numberOfPairs = Math.floor(numberOfPointsForCrossover/2);
        int number = (int) numberOfPointsForCrossover;                                      //number of entries in crossover array
        for(int i = 0; i < numberOfPairs; i++){                                             //only do this for the number of pairs
            double temp = pointsForCrossover[i].getY();
            pointsForCrossover[i].setY(pointsForCrossover[number-1-i].getY());              //swap Y values
            pointsForCrossover[number-1-i].setY(temp);
        }
    }
    static void insertionSort(Point array[]) {                              //insertion sort puts minimum at front
        for (int j = 1; j < array.length; j++) {
            Point key = array[j];
            int i = j-1;
            while ( (i > -1) && ( array[i].getFitness() > key.getFitness() ) ) {
                array[i+1] = array[i];
                i--;
            }
            array[i+1] = key;
        }
    }
}
