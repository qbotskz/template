package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Classroom;

import java.util.List;

@Repository
public interface ClassroomRepo extends CrudRepository<Classroom, Long> {

//    List<Classroom> findAllByTeacherChatId(long chatId);
    List<Classroom> findAllByTeacherUserChatId(long chatId);
    List<Classroom> findAll();
    List<Classroom> findAllByNumber(int num);
    Classroom findById(long id);
    Classroom findByNumberAndLetter(int  number, String letter);
}
