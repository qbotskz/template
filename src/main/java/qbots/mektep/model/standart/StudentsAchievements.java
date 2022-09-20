package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.FileType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class StudentsAchievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    Student student;

    private Date sentDate;

    private String  place;
    private String  subject;
    private String nameEvent;
    private String  levelEvent;
    private String  photo;


}