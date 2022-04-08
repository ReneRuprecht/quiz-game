package main.model;

/**
 * Stellt die aktuellen Kontrahenten dar.
 * @author Leonard Strauß
 */
public class Opponents {

    public String user1;
    public String user2;
    public int points1;
    public int points2;

    public Opponents(String user1, String user2, int points1, int points2) {
        this.user1 = user1;
        this.user2 = user2;
        this.points1 = points1;
        this.points2 = points2;
    }
}
