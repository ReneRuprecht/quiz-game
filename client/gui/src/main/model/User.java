package main.model;

/**
 * Stellt einen Nutzer dar mit Namen und JSON Web Token.
 * @author Leonard Strauß
 */
public class User {

    public Integer id;
    public String name;
    public String jsonWebToken;


    public User(String name, String jsonWebToken) {
        this.name = name;
        this.jsonWebToken = jsonWebToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }
}
