package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.Language;
import qbots.mektep.enums.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    private String  fullName;
    private String  phone;


    private String  fluorographyFileid;
    private Date fluorographyDate;



    private String  achievementFileid;
    private String achievementText;


    @ManyToOne()
    Classroom classroom;

    @ManyToOne
    User user;


    @ManyToOne
    User parent;

}