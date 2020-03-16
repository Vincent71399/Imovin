package sg.edu.nus.imovin2.Objects;

public class ChallengeStar {
    public enum starColor {Gold, Silver, Bronze}

    private int points;
    private int steps;
    private starColor color;

    public ChallengeStar(int points, int steps, starColor color) {
        this.points = points;
        this.steps = steps;
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public starColor getColor() {
        return color;
    }

    public void setColor(starColor color) {
        this.color = color;
    }
}
