package robot.qa.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import robot.qa.bean.Answer;

public interface AnswerRepository extends MongoRepository<Answer, ObjectId> {
    List<Answer> findByKeyword(String keyword);
}
