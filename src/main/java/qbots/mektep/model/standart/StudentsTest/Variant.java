package qbots.mektep.model.standart.StudentsTest;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    String text;
    int point;

    @ManyToOne
    Question question;
}