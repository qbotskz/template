package qbots.project.model.standart;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import qbots.project.enums.Language;
import qbots.project.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    private long    chatId;
    private String  phone;
    private String  fullName;
    private String  iin;

    @Column(length = 500)
    private String  username;

    @Enumerated
    Language language;


    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Enumerated
    List<Role> roles;

    @Enumerated
    Role chosenRole;


    public void addRole(Role roleParent) {
        if (roles == null){
            roles = new ArrayList<>();
        }
        if (!roles.contains(roleParent)){
            roles.add(roleParent);
        }
    }

    public void deleteRole(Role role) {
        roles.remove(role);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

}