package qbots.mektep.repository.studentTest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.Test;

import java.util.List;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Long> {

//    @Query("select distinct" +
//            " ZZ.question" +
//            " from StudentsAnswer  cc" +
//            " LEFT JOIN Variant ZZ ON cc.question.id <> ZZ.question.id" +
//            " where cc.student.id = 5")
//    List<Question> getQues();


//
//    @Query("select Distinct " +
//            "Question " +
//            "from Variant cc " +
//            "where cc.question.id not in (select StudentsAnswer.question.id  from StudentsAnswer  where StudentsAnswer.student.id=5 )")
//    List<Question> getLastQuest();



    @Query("select Distinct " +
            "min(cc.question) as question_id " +
            "from Variant cc " +
            "where cc.question.id  not in (select  sa.question.id from StudentsAnswer sa where sa.student.id = :id ) and cc.question.test.id = :testId")
    Question    getLastQuest(@Param("id") long id, @Param("testId") long testId);

//    @Query("select Distinct " +
//            "min(cc.question.test) as question_id " +
//            "from Variant cc " +
//            "where cc.question.id  not in (select  sa.question.id from StudentsAnswer sa where sa.student.id = :id ) ")
//    List<Test> getLastQuest2();

//    @Query("select Distinct " +
//            "min(cc.question.id) as question_id " +
//            "from Variant cc " +
//            "where cc.question.id  not in (select  sa.question.id from StudentsAnswer sa where sa.student.id = 5 )")
//    Question getLastQuest();


    List<Question> findAllByTest(Test test);
    Question findById(long id);

}
