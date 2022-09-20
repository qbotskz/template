package qbots.mektep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qbots.mektep.model.standart.Onay;

@Repository
public interface OnayRepo extends JpaRepository<Onay,Long> {
}
