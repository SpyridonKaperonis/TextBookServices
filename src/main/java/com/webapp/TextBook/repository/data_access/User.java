package com.webapp.TextBook.repository.data_access;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Entity
public class User extends Person{

    /*
    This is the DA for the User table, however, a user is partially comprised of the
    User table and the Spriden table.  The definitions in here are distinct to the User
    table, but will rely on the Person (Spriden) for partial DA construction and definition.
     */


    // enumerates the instance members that are distinct to User (not Student)

    // the local id value that uniquely identifies a user with the POJO/DA class
    // required by JPA repository and Hibernate (though Hibernate is not utilized)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID _ID;

    // denotes the hierarchical role of the user (either Supervisor or StudentEmployee)
    public UserRole userRole;

    // holds the password of the logged in user
    // NOTE: the username (919 number, or id in Person, or spriden_id in Spriden)
    // is defined in Person
    public String password;

    // enumerates static members for JSON parsing
    private static final String NOMINAL_PASSWORD = "password";

    private static final String NOMINAL_USERNAME = "username";

    public User() {this._ID = UUID.randomUUID(); }

    public User(String pidm, String id, String firstName, String lastName,
                String middleName, UserRole userRole, String password) {
        super(pidm, id, firstName, lastName, middleName);
        this._ID = UUID.randomUUID();
        this.userRole = userRole;
        this.password = password;
    }

    public UUID get_ID() {
        return _ID;
    }

    public void set_ID(UUID _ID) {
        this._ID = _ID;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static @NotNull Object[] conglomerateDboArrays(@NotNull final Object[] spridenDbo, @NotNull final Object[] textbookServiceLoginDbo){

        // conglomerates the two partials into a contiguous array for DA construction
        // creates object array and set it to the copy
        Object[] congolmerateDataAccess = Arrays.copyOf(
                spridenDbo,
                spridenDbo.length + textbookServiceLoginDbo.length -1);
        // -1 (username of textbook service dbo being omitted)

        // sets latter partial values to conglomerate -- username being omitted
        // given: password is the first column in the dbo
        for(int i = 0; i < textbookServiceLoginDbo.length - 1; i++)
            congolmerateDataAccess[spridenDbo.length + i] = textbookServiceLoginDbo[i + 1];

        return congolmerateDataAccess;
    }

    public static @NotNull Optional<User> createPartialUserFromJSONObject(@NotNull final JSONObject jsonObject){


        // instantiates empty user for partial construction
        final User user = new User();

        // try-catch for JSON key errors in parsing partial user definition
        try{

            user.setPassword(jsonObject.getString(User.NOMINAL_PASSWORD));

            user.setId(jsonObject.getString(User.NOMINAL_USERNAME));

            return Optional.of(user);

        }
        catch(JSONException jsonException){
            // logs (prints now) exception
            System.out.println(jsonException.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public void updateDataAccessObject(@NotNull Object[] values) {

        // invokes super to set Person attributes
        // given: person attributes come first
        super.updateDataAccessObject(values);


        // given: order of values goes pkey (username, password), userRole
        // ["blah", "blah", "username", "password", "user-role"]
        this.password = (String)values[values.length - 2];
        this.userRole = UserRole.createUserRoleByNominalValue((String)values[values.length - 1]).orElseThrow();

    }
}
