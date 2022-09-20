package qbots.mektep.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Properties;

@Repository
public interface PropertiesRepo extends CrudRepository<Properties, Integer> {
Properties findFirstById(int id);
}
