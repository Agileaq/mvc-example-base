package personal.arc.mvc.bean;

/**
 * Created by Arc on 18/8/2016.
 */
public class Person {

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }
}
