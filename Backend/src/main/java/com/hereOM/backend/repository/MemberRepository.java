package com.hereOM.backend.repository;

import com.hereOM.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByUsername(String username); // select * from user where username= username;
}