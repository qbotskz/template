package qbots.mektep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Onay;
import qbots.mektep.model.standart.RequestFromParent;

@Repository
public interface RequestFromParentRepo extends JpaRepository<RequestFromParent,Long> {

    RequestFromParent findById(long id);

}
