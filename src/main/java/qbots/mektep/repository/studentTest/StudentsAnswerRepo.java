package qbots.mektep.repository.studentTest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.StudentsAnswer;
import qbots.mektep.model.standart.StudentsTest.Test;

import java.util.List;

@Repository
public interface StudentsAnswerRepo extends CrudRepository<StudentsAnswer, Long> {

    List<StudentsAnswer> findAllByStudentAndQuestionTest(Student student, Test test);
    int countByStudentAndQuestionTest(Student student, Test test);

}
