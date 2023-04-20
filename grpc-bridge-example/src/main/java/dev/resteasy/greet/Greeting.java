package dev.resteasy.greet;

public class Greeting {
    private String s;

    public Greeting() {
    }

    public Greeting(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String toString() {
        return "Hello, " + s;
    }
}
