/**
 * Created by Kevin on 11/7/2015.
 */
public class Point {

    private double X;
    private double Y;
    private boolean mutate;
    private boolean crossover;
    private int location;
    private double fitness;
    private double probabiliyOfSurvival;
    private boolean survive = false;

    public Point(){}
    public double getX() {
        return X;
    }
    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public boolean isMutate() {
        return mutate;
    }

    public void setMutate(boolean mutate) {
        this.mutate = mutate;
    }

    public boolean isCrossover() {
        return crossover;
    }

    public void setCrossover(boolean crossover) {
        this.crossover = crossover;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getProbabiliyOfSurvival() {
        return probabiliyOfSurvival;
    }

    public void setProbabiliyOfSurvival(double probabiliyOfSurvival) {
        this.probabiliyOfSurvival = probabiliyOfSurvival;
    }

    public boolean isSurvive() {
        return survive;
    }

    public void setSurvive(boolean survive) {
        this.survive = survive;
    }
}
