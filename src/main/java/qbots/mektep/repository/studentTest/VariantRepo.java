package qbots.mektep.repository.studentTest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.Variant;

import java.util.List;

@Repository
public interface VariantRepo extends CrudRepository<Variant, Long> {



    Variant findById(long id);

}
