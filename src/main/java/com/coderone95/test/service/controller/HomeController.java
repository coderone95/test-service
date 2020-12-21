package com.coderone95.test.service.controller;

import com.coderone95.test.service.model.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/")
    public String home(){
        ResponseEntity<Teams[]> responseEntity = restTemplate.getForEntity("https://s3-ap-southeast-1.amazonaws.com/he-public-data/Leaderboard_Initial_Dataset65148c7.json", Teams[].class);
        Teams[] objects = responseEntity.getBody();
        String str = "";
        for(Teams t: objects){

            str += "\n Team Name::"+t.getTeam_name()+"\t Wins::"+t.getWins()+"\t Losses::"+t.getLosses()
                    +"\t Ties::"+t.getTies()+"\t Score::"+t.getScore()+"\n";
        }
        return str;
    }
}
