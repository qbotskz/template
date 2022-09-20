package qbots.mektep.model.standart.StudentsTest;

import lombok.Data;
import qbots.mektep.model.standart.Student;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class StudentsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    Student student;

    @ManyToOne
    Question question;

    @ManyToOne
    Variant variant;

}