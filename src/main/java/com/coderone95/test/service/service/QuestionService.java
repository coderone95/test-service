package com.coderone95.test.service.service;

import com.coderone95.test.service.entity.Answer;
import com.coderone95.test.service.entity.Question;
import com.coderone95.test.service.entity.User;
import com.coderone95.test.service.entity.UserLoginData;
import com.coderone95.test.service.model.Teams;
import com.coderone95.test.service.repository.AnswerRepository;
import com.coderone95.test.service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("questionService")
@Component
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    public Question saveQuestion(Question que) {

        List<Answer> answersList = que.getAnswers().stream().map(obj->{
                        obj.setQuestion(que);
                        obj.setCreatedOn(new Date());
                        obj.setUpdatedOn(new Date());
                        if(que.getCreatedBy() != null && !que.getCreatedBy().isEmpty()){
                            obj.setUpdatedBy(que.getCreatedBy());
                            obj.setCreatedBy(que.getCreatedBy());
                        }
                        return obj;
                    }).collect(Collectors.toList());
        que.setUpdatedOn(new Date());
        que.setCreatedOn(new Date());
        que.setDisabled(false);
        que.setDeleted(false);
        if(que.getCreatedBy() != null && !que.getCreatedBy().isEmpty()){
            que.setUpdatedBy(que.getCreatedBy());
        }
        Question question = questionRepository.save(que);
        return question;
    }

    public boolean isQuestionExists(Long id) {
        return questionRepository.existsById(id);
    }

    //flag:- ADD_NEW_ANSWER, UPDATE_QUESTION, UPDATE_QUESTION_ADD_NEW_ANSWER
    public void updateQuestion(Long id, Question question, String flag) {
        Question data = questionRepository.findById(id).get();
        if(flag.equals("UPDATE_QUESTION")){
            if(question.getQuestion() != null && !question.getQuestion().isEmpty()){
                data.setQuestion(question.getQuestion());
            }
            if(question.getIsDeleted() != null){
                data.setDeleted(question.getIsDeleted());
            }
            if(question.getIsDisabled() != null){
                data.setDisabled(question.getIsDisabled());
            }
            if(question.getUpdatedBy() !=null && !question.getUpdatedBy().isEmpty()){
                data.setUpdatedBy(question.getUpdatedBy());
            }
        }else if(flag.equals("ADD_NEW_ANSWER")){
            addNewAnswers(question,data);
        }else if(flag.equals("UPDATE_QUESTION_ADD_NEW_ANSWER")){
            if(question.getQuestion() != null && !question.getQuestion().isEmpty()){
                data.setQuestion(question.getQuestion());
            }
            if(question.getIsDeleted() != null){
                data.setDeleted(question.getIsDeleted());
            }
            if(question.getIsDisabled() != null){
                data.setDisabled(question.getIsDisabled());
            }
            if(question.getUpdatedBy() !=null && !question.getUpdatedBy().isEmpty()){
                data.setUpdatedBy(question.getUpdatedBy());
            }
            addNewAnswers(question,data);
        }

        data.setUpdatedOn(new Date());
        questionRepository.save(data);
    }

    public void updateAnswer(List<Answer> answers){
        answerRepository.saveAll(answers);
    }

    public void updateAnswer(Long id, Answer ans){
        Answer data = answerRepository.findById(id).get();
        if(ans.getAnswer() !=null && !ans.getAnswer().isEmpty()){
            data.setAnswer(ans.getAnswer());
        }
        if(ans.getUpdatedBy() !=null && !ans.getUpdatedBy().isEmpty()){
            data.setUpdatedBy(ans.getUpdatedBy());
        }
        if(ans.getIsCorrect() !=null){
            if(ans.getIsCorrect()){
                List<Answer> answers = questionRepository.findById(data.getQuestion().getQuestionId()).get().getAnswers();
                if(answers.size() > 0){
                    List<Answer> newList = answers.stream().map(obj->{
                        if(obj.getIsCorrect()){
                            obj.setCorrect(false);
                        }
                        return obj;
                    }).collect(Collectors.toList());
                    updateAnswer(newList);
                    data.setCorrect(true);
                }
            }else{
                data.setCorrect(false);
            }

        }else{
            data.setCorrect(false);
        }
        data.setUpdatedOn(new Date());
        answerRepository.save(data);
    }

    public void addNewAnswers(Question question, Question data){
        List<Answer> answers = question.getAnswers().stream().map(obj->{
            obj.setQuestion(data);
            return obj;
        }).collect(Collectors.toList());
        List<Answer> correctList = answers.stream().filter(obj-> obj.getIsCorrect() == true).collect(Collectors.toList());
        if(correctList.size() == 0){
            updateAnswer(answers);
        }else {
            Answer answer = correctList.get(0);
            answer.setQuestion(data);
            answers = data.getAnswers().stream().map(obj -> {
                if (obj.getIsCorrect()) {
                    obj.setCorrect(false);
                }
                return obj;
            }).collect(Collectors.toList());

            answers.add(answer);
            updateAnswer(answers);
        }
    }

    public boolean isAnswerExists(Long id) {
        return answerRepository.existsById(id);
    }

    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return  questionRepository.findById(id).get();
    }

    public List<Question> getQuestionsByIds(List<Long> ids) {
        return questionRepository.findAllById(ids);
    }

    public boolean isCorrectAnswer(Long id) {
        Answer answer = answerRepository.findById(id).get();
        return answer.getIsCorrect();
    }

    public void deleteAnswerById(Long id) {
        answerRepository.deleteById(id);
    }

    public List<Answer> getAllAnswersByQuestionId(Long questionId) {
        Question q = questionRepository.findById(questionId).get();
        List<Answer> answers = answerRepository.findByQuestion(q);
        return answers;
    }

    public boolean isUserExistsByLoginId(String loginId) {
        String path = env.getProperty("user.service.home");
        ResponseEntity<UserLoginData> responseEntity = restTemplate.getForEntity(path+"/users/get?loginId="+loginId, UserLoginData.class);
        UserLoginData loginData = responseEntity.getBody();
        if(loginData != null){
            System.out.println(loginData.toString());
            return true;
        }
        return  false;
    }

    public List<Question> getQuestionsByLoginId(String loginId) {
        List<Question> list = questionRepository.findByCreatedBy(loginId);
        return list;
    }
}
