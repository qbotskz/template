package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.RegistrationTeacher;

import java.util.List;

@Repository
public interface RegistrationTeacherRepo extends CrudRepository<RegistrationTeacher, Long> {

    RegistrationTeacher findByUserChatId(long charId);

}
