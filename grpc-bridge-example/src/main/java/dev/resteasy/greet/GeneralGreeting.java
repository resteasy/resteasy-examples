package dev.resteasy.greet;

public class GeneralGreeting extends Greeting {
    private String salute;

    public GeneralGreeting() {
    }

    public GeneralGreeting(String salute, String s) {
        super(s);
        this.salute = salute;
    }

    public String getSalute() {
        return salute;
    }

    public void setSalute(String salute) {
        this.salute = salute;
    }

    public String toString() {
        return salute + ", " + getS();
    }
}
