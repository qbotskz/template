package qbots.mektep.repository.studentTest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.enums.Role;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.IndividualTask;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;

import java.util.List;

@Repository
public interface IndividualTaskRepo extends CrudRepository<IndividualTask, Long> {

    List<IndividualTask> findAllByStudentAndTextNotNull(Student student);
    List<IndividualTask> findAllByStudentAndAnswerTextIsNull(Student student);
    IndividualTask findById(long id);
}
