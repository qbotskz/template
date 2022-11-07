package qbots.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.project.model.standart.Properties;

@Repository
public interface PropertiesRepo extends CrudRepository<Properties, Integer> {
Properties findFirstById(int id);
}
