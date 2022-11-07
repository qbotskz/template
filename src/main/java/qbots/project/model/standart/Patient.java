package qbots.project.model.standart;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long     id;

    @Column
    private String  iin;

    @Column
    private String  fullName;

    @Column
    private String  imt;

}
