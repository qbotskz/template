package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsAchievements;

import java.util.List;

@Repository
public interface StudentsAchievementsRepo extends CrudRepository<StudentsAchievements, Long> {

    List<StudentsAchievements> findAllByStudent(Student student);

}
