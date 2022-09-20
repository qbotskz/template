package qbots.mektep.model.standart;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import qbots.mektep.enums.Language;
import qbots.mektep.enums.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    User user;

    @ManyToOne
    Classroom currentClassroom;

    String phone;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "teacher")
//    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Classroom> myClasses;



}