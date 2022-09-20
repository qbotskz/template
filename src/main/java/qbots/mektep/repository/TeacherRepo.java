package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Teacher;

import java.util.List;

@Repository
public interface TeacherRepo extends CrudRepository<Teacher, Long> {
    Teacher findByUserChatId(long chatId);
    Teacher findByPhone(String phone);
}
