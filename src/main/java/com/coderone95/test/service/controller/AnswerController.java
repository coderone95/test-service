package com.coderone95.test.service.controller;

import com.coderone95.test.service.entity.Answer;
import com.coderone95.test.service.model.ErrorResponse;
import com.coderone95.test.service.model.Status;
import com.coderone95.test.service.model.SuccessResponse;
import com.coderone95.test.service.repository.AnswerRepository;
import com.coderone95.test.service.service.QuestionService;
import com.sun.tools.internal.ws.processor.generator.GeneratorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateAnswer(@PathVariable("id") Long id, @RequestBody Answer ans){
        try{
            if(questionService.isAnswerExists(id)){
                questionService.updateAnswer(id,ans);
            }
        }catch (Exception e){
            ErrorResponse err = new ErrorResponse(e.getMessage(),
                    new Status("ERROR"));
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
        SuccessResponse successResponse = new SuccessResponse("Answer is updated",new Status("SUCCESS"));
        return new ResponseEntity<>(successResponse,HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("id") Long id){
        if(questionService.isAnswerExists(id)){
            if(!questionService.isCorrectAnswer(id)){
                questionService.deleteAnswerById(id);
            }else{
                throw new GeneratorException("Right answer cannot be deleted directly");
            }
        }else{
            throw new GeneratorException("No such answer id");
        }
        SuccessResponse successResponse = new SuccessResponse("Answer is deleted",new Status("SUCCESS"));
        return new ResponseEntity<>(successResponse,HttpStatus.OK);
    }

    @GetMapping(value = "/getAll/{id}")
    public ResponseEntity<?> getAllAnswersByQuestionId(@PathVariable("id") Long questionId){
        List<Answer> answers = new ArrayList<>();
        if(questionService.isQuestionExists(questionId)){
            answers = questionService.getAllAnswersByQuestionId(questionId);
        }else{
            throw new GeneratorException("Invalid Question Id");
        }
        return new ResponseEntity<>(answers,HttpStatus.OK);
    }
}
