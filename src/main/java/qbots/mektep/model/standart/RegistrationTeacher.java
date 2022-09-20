package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.Language;
import qbots.mektep.enums.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class RegistrationTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    User user;

    private String  fullName;
    private String  iin;
    private String  education;
    private String  diplomaNumber;
    private String  diplomaScanPhoto;
    private String  diplomaScanDoc;
    private String  subject;
    private int  experience;
    private int  yearInSchool;
    private String  category;
    private String  category2;
    private String  qualification;
    private String  achievements;
    private String  place;
    private String  level;
    private String  awards;
    private String  address;
    private String  phoneNumber;

}