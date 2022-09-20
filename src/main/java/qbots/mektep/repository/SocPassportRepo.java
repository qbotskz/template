package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//import qbots.mektep.model.standart.Admin;
import qbots.mektep.model.standart.SocPassport;
import qbots.mektep.model.standart.Student;

import java.util.List;

@Repository
public interface SocPassportRepo extends CrudRepository<SocPassport, Long> {

    SocPassport findByStudent(Student student);

}
