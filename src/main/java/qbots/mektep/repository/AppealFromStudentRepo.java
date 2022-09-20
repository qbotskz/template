package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.enums.Role;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;

import java.util.List;

@Repository
public interface AppealFromStudentRepo extends CrudRepository<AppealToPsych, Long> {

    List<AppealToPsych> findAllByRoleAndAnswerIsNull(Role role);
    AppealToPsych findById(long id);

    List<AppealToPsych> findAllByStudent(Student student);
    List<AppealToPsych> findAllByTeacher(Teacher teacher);


}
