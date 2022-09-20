package qbots.mektep.model.standart;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int number;
    private String letter;
    private String chatLinkChildren;
    private String chatLinkParents;

    @ManyToOne
    Teacher teacher;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Student> students;

    public String getName(){
        return number+letter;
    }


}
