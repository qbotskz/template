package qbots.mektep.model.standart;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class ReferencesStudents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private String photoId;

    Date dateStart;
    Date dateEnd;

    @ManyToOne
    Student student;

}
