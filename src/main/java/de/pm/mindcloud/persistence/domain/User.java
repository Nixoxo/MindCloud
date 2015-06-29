package de.pm.mindcloud.persistence.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static org.apache.commons.lang.builder.ToStringBuilder.*;

/**
 * Created by samuel on 29.06.15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = User.NAME, query = "from User u where u.name like :username")
})
public class User {
    public static final String NAME = "User.NAME";
    public static final String USERNAME = "username";

    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    private String id;

    private String name;

    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name.toLowerCase();
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}