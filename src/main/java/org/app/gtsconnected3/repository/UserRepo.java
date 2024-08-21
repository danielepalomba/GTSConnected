package org.app.gtsconnected3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.app.gtsconnected3.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    public User findByEmail(String email);
    public User findByVerificationToken(String token);
    public User findByResetPasswordToken(String token);
    List<User> findAllByNewsletterIsTrue();
}
