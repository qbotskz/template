package qbots.mektep.model.standart;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SocPassport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    Student student;

    String fullName;
    String birthDate;
    String iin;
    String performance;
    String behaviour;
    String nationality;
    String fatherFullName;
    String fathersJobPlace;
    String motherFullName;
    String mothersJobPlace;
    String address;
    String microarea;
    String socStatus;
    String othersInFamily;
    String clubs;
//    String socPassport;
    String email;
    String phone;




}