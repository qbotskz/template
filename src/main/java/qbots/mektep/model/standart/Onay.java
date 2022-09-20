package qbots.mektep.model.standart;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Onay {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String birthday;
    private String classroom;

    @ManyToOne
    private Student student;

    private String card;

    private String photo3x4;

}
