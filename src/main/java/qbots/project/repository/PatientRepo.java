package qbots.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.project.model.standart.Button;

@Repository
public interface PatientRepo extends CrudRepository<Button, Long> {

}
