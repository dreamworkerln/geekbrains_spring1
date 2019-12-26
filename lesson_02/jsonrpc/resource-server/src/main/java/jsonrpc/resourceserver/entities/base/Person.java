package jsonrpc.resourceserver.entities.base;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@MappedSuperclass
public class Person extends AbstractEntity {

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String login;

    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
    private String email;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {return login;}

    public void setLogin(String login) {this.login = login;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
