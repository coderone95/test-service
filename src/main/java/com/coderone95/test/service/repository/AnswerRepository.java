package com.coderone95.test.service.repository;

import com.coderone95.test.service.entity.Answer;
import com.coderone95.test.service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findByQuestion(Question q);
}
