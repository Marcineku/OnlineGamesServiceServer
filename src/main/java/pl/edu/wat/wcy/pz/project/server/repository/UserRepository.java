package pl.edu.wat.wcy.pz.project.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.pz.project.server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
