package qbots.mektep.repository.studentTest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.StudentsTest.Test;

import java.util.List;

@Repository
public interface TestRepo extends CrudRepository<Test, Long> {

    List<Test> findAllByClassroomsContains(Classroom classroom);

    Test findById(long id);

    List<Test> findAllBy();

    boolean existsTestByClassroomsContains(Classroom classroom);

//    @Query("select distinct " +
//            "tc.test_id " +
//            "from test_classrooms tc left join ( select Distinct " +
//            "cc.question_id ,zz.question_id ,5 as id_class from variant as cc " +
//            "left JOIN students_answer ZZ ON cc.question_id=ZZ.question_id " +
//            "where zz.question_id is null ) tt on tc.classrooms_id=tt.id_class " +
//            "where tt.id_class is not null")
//
//    List<Test> getTests();

}
