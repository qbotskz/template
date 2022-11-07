package qbots.project.model.standart;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Message {

    @Id
    @Column(unique = false)
    private Integer id;
    private String  name;
//    private String  photo;
//    private Integer keyboardId;
//    private String  file;
//    private String  fileType;
    private Integer languageId;

//    public void setFile(String file, FileType fileType) {
//        this.file       = file;
//        this.fileType   = fileType.name();
//    }
}
