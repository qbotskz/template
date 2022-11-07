package qbots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qbots.project.enums.Role;
import qbots.project.model.standart.User;

import java.util.List;

@Repository
public interface UsersRepo extends JpaRepository<User, Long> {
    User        getByChatId(long chatId);
    Integer     countUserByChatId(long chatId);
    List<User> findAll();
    List<User> findAllByRolesContains(Role role);
//    List<User> findAllByRolesContaining(List<Role> roles);
    User findFirstByChatId(long chatId);
    User findByPhone(String phone);
    User findByChatId(long chatId);
    User findById(long id);

}
