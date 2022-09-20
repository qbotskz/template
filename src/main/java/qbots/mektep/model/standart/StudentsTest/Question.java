package qbots.mektep.model.standart.StudentsTest;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Variant> variants;

    String questionText;

    @ManyToOne
    Test test;

}