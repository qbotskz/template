package qbots.mektep.model.standart;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RequestFromParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    User parent;

    @ManyToOne
    Student student;


    boolean accepted;
    String answerFromTeacher;
}
