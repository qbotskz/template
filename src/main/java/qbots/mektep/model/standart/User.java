package qbots.mektep.model.standart;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import qbots.mektep.enums.Language;
import qbots.mektep.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    private long    chatId;
    private String  phone;
    private String  fullName;

    @Column(length = 500)
    private String  username;

    @Enumerated
    Language language;


    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Enumerated
    List<Role> roles;


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

    @ManyToOne
    Student currentStudent;
}