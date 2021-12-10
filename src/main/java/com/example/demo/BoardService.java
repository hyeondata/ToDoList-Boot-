package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    
    @Autowired
    private BoardMapper boardMapper;

    public void Submit(String id){
        int status = boardMapper.SelectId(id);
        if (status < 3) {
			status++;
		}
        boardMapper.Button(status, id);
    } 

    public void Cancel(String id){
        int status = boardMapper.SelectId(id);
        if (status > 0) {
			status--;
		}
        boardMapper.Button(status, id);
    }
}
