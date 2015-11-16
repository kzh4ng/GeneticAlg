import java.util.Random;
import java.util.Scanner;

/**
 * Created by Kevin on 11/7/2015.
 */
public class GA {

    final static double startX = 0;                                 //stating coordinates
    final static double startY = 0;

    final static int populationSize = 10;
    final static int numberOfSurvivors = 5;
    final static double probabilityConstant = .4;
    static Point points[] = new Point[populationSize];

    final static double stepSize1 = 1;
    final static double stepSize2 = -1*stepSize1;

    final static int pctChanceOfMutation = 80;
    final static int pctChanceOfCrossover = 80;

    static int iterationLimit = 10;

    public static void main(String args[]){
        for(int i = 0; i < points.length; i++){                 //initializes all points to starting point and default mutation/crossover setting
            points[i] = new Point();
            points[i].setX(startX);
            points[i].setY(startY);
            points[i].setMutate(false);
            points[i].setCrossover(false);
        }
        int iteration = 0;
        Random r = new Random();
        int mutation;
        while(iteration < iterationLimit){
            for(int k = 0; k<points.length; k++){                        //determine mutation for all points
                mutation = r.nextInt(101);
                if(mutation <= pctChanceOfMutation) mutate(points[k]);
                points[k].setLocation(k);
                printPoint(points[k]);
            }
            double numberOfPointsForCrossover = 0;
            Point pointsForCrossover[] = new Point[populationSize];
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
            pointsForCrossover = crossover(numberOfPointsForCrossover, pointsForCrossover);
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
            }
            insertionSort();                                                          //sort the array by fitness (most fit at the end of the array
            System.out.println("Points after sort:");
            for(int i = 0; i < points.length; i++){
                System.out.print("[" + points[i].getX() + "," + points[i].getY() + "," + points[i].getFitness() + "],");
            }
            determineProbabilities();
            determineSurvivors();


            Point newPoints[] = new Point[populationSize];
            int newPointCount = 0;
            for (int i = 0; newPointCount<numberOfSurvivors;){              //put all the survivors in a new array
                while(!points[i].isSurvive()) i++;
                newPoints[newPointCount] = points[i];
                i++;
                newPointCount++;
            }
            Point p = averagePoint(newPoints);
            for(int i = numberOfSurvivors; i < populationSize; i++){
                newPoints[i] = p;
            }
            System.out.println("Iteration finished, press enter to continue to next iteration");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            points = newPoints;
            iteration++;
        }
    }

    static double function(double x, double y){                      //returns the result of sin(4*(x^2+y^2))/4;
        double z = Math.sin(4*(Math.pow(x,2)+Math.pow(y,2)))/4;
        return z;
    }
    static Point mutate(Point p){
        Random r = new Random();
        double random = r.nextDouble();                                 //generate a random number from zero to one
        double random2 = r.nextDouble();
        int determineVariable = r.nextInt(3);
        double range = stepSize1 - stepSize2;                               // is the range of the mutations
        double mutation = random * range + stepSize2;
        double mutation2 = random2 * range + stepSize2;
        if(determineVariable == 0){                                             //the following if statements only allow either x or y to be mutated
            p.setX(p.getX()+mutation);
        }
        else if(determineVariable == 1){
            p.setY(p.getY()+mutation);
        }
        else{
            p.setX(p.getX()+mutation);
            p.setY(p.getY()+mutation2);
        }
        return p;
    }

    static void printPoint(Point p){
        System.out.println("["+p.getX()+","+p.getY()+","+p.getFitness()+"]");
    }

    static Point[] crossover(double crossoverPoints, Point points[]){
        double numberOfPairs = Math.floor(crossoverPoints/2);
        int number = (int) crossoverPoints;                                      //number of entries in crossover array
        for(int i = 0; i < numberOfPairs; i++){                                             //only do this for the number of pairs
            double temp = points[i].getY();
            points[i].setY(points[number-1-i].getY());              //swap Y values
            points[number-1-i].setY(temp);
        }
        return points;
    }
    static void insertionSort() {                              //insertion sort puts minimum at front
        for (int j = 1; j < populationSize; j++) {
            Point key = points[j];
            int i = j-1;
            while ( (i > -1) && ( points[i].getFitness() > key.getFitness() ) ) {
                points[i+1] = points[i];
                i--;
            }
            points[i+1] = key;
        }
    }
    static Point averagePoint(Point p[]){               //returns a point with the average X and Y value of an array of points
        double sumX = 0;
        double sumY = 0;
        for(int i = 0; i < numberOfSurvivors; i++){
            sumX = sumX + p[i].getX();
            sumY = sumY + p[i].getY();
        }
        Point point = new Point();
        point.setX(sumX/numberOfSurvivors);
        point.setY(sumY/numberOfSurvivors);
        return point;
    }
    static void determineProbabilities(){
        for(int i = 0; i < points.length; i++){
            if(i==0){
                points[i].setProbabiliyOfSurvival(1-probabilityConstant);               //first rank item has 1-p probability of survival
            }
            else{
                points[i].setProbabiliyOfSurvival(probabilityConstant*Math.pow(1-probabilityConstant,i)); //remaining have p(1-p)^i probability
            }
        }
    }

    static void determineSurvivors(){
        int survivorCount = 0;
        int index = 0;
        Random r = new Random();
        while(survivorCount != numberOfSurvivors){
            double random = r.nextDouble();                                                             //generates a random number from 0-1
            if(random < points[index].getProbabiliyOfSurvival()&& !points[index].isSurvive()){              //if the number is less than the probability and the point hasn't been marked as true yet
                points[index].setSurvive(true);
                survivorCount++;
            }
            index++;
            index = index%populationSize;                               //keeps the indexes within the population size
        }
    }
}
