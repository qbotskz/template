package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.ReferencesStudents;

import java.util.List;

@Repository
public interface ReferencesStudentsRepo extends CrudRepository<ReferencesStudents, Long> {

}
