package com.coderone95.test.service.controller;

import com.coderone95.test.service.entity.Answer;
import com.coderone95.test.service.entity.Question;
import com.coderone95.test.service.exceptions.EmptyAnswersForQuestion;
import com.coderone95.test.service.exceptions.GenericException;
import com.coderone95.test.service.exceptions.NoSuchQuestionException;
import com.coderone95.test.service.model.ErrorDetails;
import com.coderone95.test.service.model.ErrorResponse;
import com.coderone95.test.service.model.Status;
import com.coderone95.test.service.model.SuccessResponse;
import com.coderone95.test.service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/save")
    public ResponseEntity<Question> saveQuestion(@Valid @RequestBody Question que){
        Question question = null;
        try{
            if( que.getAnswers() == null || que.getAnswers().isEmpty() ){
                ErrorResponse err = new ErrorResponse("Answers are mandatory while creating question",
                        new Status("ERROR"));
                return new ResponseEntity(err,HttpStatus.EXPECTATION_FAILED);
            }
            List<Answer> correctAnswerList = que.getAnswers().stream().filter(obj->obj.getIsCorrect() == true).
                    collect(Collectors.toList());
            if(correctAnswerList.size() > 1 || correctAnswerList.size() == 0){
                ErrorResponse err = new ErrorResponse("Expecting only one correct answer",
                        new Status("ERROR"));
                return new ResponseEntity(err,HttpStatus.EXPECTATION_FAILED);
            }
            question = questionService.saveQuestion(que);
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity(err,HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable("id") Long id,
            @RequestBody Question question,
            @RequestParam("flag") String flag){
        try{
            if(questionService.isQuestionExists(id)){
                if(flag.equals("ADD_NEW_ANSWER")){
                    if(question.getAnswers() == null){
                        throw new EmptyAnswersForQuestion("Answers are mandatory");
                    }else{
                        List<Answer> answrs = question.getAnswers().stream().filter(obj->obj.getIsCorrect() == true)
                                .collect(Collectors.toList());
                        if(answrs.size() > 1){
                            throw new GenericException("Correct Answers cannot be more than one");
                        }
                    }
                }else if(flag.equals("UPDATE_QUESTION_ADD_NEW_ANSWER") && question.getAnswers() == null ){
                    throw new EmptyAnswersForQuestion("Answers are mandatory");
                }
                questionService.updateQuestion(id,question,flag);
            }else{
                throw new NoSuchQuestionException("Invalid question");
            }
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
        }
        SuccessResponse successResponse = new SuccessResponse("Question is updated",new Status("SUCCESS"));
        return new ResponseEntity<>(successResponse,HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id){
        try {
            if(questionService.isQuestionExists(id)){
                questionService.deleteQuestionById(id);
            }else{
                throw new NoSuchQuestionException("Invalid question");
            }
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
        }
        SuccessResponse successResponse = new SuccessResponse("Question is deleted",new Status("SUCCESS"));
        return new ResponseEntity<>(successResponse,HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllQuestions(){
        List<Question> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") Long id){
        Question question = new Question();
        try{
            if(questionService.isQuestionExists(id)){
                question = questionService.getQuestionById(id);
            }else{
                throw new NoSuchQuestionException("Invalid question");
            }
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity(err,HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(question,HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam("ids") List<Long> ids){
        List<Question> questions = new ArrayList<>();
        try{
            questions = questionService.getQuestionsByIds(ids);
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity(err,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }



}
