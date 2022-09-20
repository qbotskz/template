package qbots.mektep.model.standart.StudentsTest;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import qbots.mektep.enums.FileType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;


    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Question> questions;

    String name;

    @ManyToMany
    List<Classroom> classrooms;
}