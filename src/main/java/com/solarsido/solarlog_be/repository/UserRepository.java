package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // 이 인터페이스가 Repository 역할을 하는 컴포넌트임을 나타냅니다.
public interface UserRepository extends JpaRepository<User, Long> {

  // 아이디 중복 확인을 위한 메소드
  boolean existsByUserId(String userId);
  Optional<User> findByUserId(String userId);

}
