package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;

import java.util.List;

@Repository
public interface StudentRepo extends CrudRepository<Student, Long> {

    Student findByPhoneAndFullName(String phone, String fullName);

    Student findById(long id);

    List<Student> findByParent(User user);
    List<Student> findByParentChatId(long chatId);
    List<Student> findByPhone(String phone);

}
