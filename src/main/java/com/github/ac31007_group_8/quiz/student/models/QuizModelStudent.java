/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.student.models;

import com.github.ac31007_group_8.quiz.student.stores.QuizInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Vlad
 */
public class QuizModelStudent {
    
    
    
    
    public QuizModelStudent(){
        
        
        
    }
    
    public ArrayList<QuizInfo> getRelevantQuizzes(int studentId, Connection conn) throws SQLException{
        
        
        PreparedStatement stmt = conn.prepareStatement("SELECT quiz.quiz_id, quiz.time_limit, quiz.Title, module.module_name, module.module_id" +
                                        " FROM quiz" +
                                        " INNER JOIN module" +
                                        " ON quiz.module_id = module.module_id" +
                                        " INNER JOIN student_to_module" +
                                        " ON student_to_module.module_id = module.module_id " +
                                        " INNER JOIN student" +
                                        " ON student.student_id = student_to_module.student_id" +
                                        " WHERE publish_status=1 AND student.student_id=?;");
        stmt.setInt(1,studentId);
        
        
        ResultSet rs = stmt.executeQuery();   
        ArrayList<QuizInfo> allRelevantQuizInfo = new ArrayList<QuizInfo>();
        
        while (rs.next()){
            
                QuizInfo next = new QuizInfo();
                next.setQuizId(rs.getInt(1));
                next.setTimeLimit(rs.getInt(2)); 
                next.setTitle(rs.getString(3)); 
                next.setModuleName(rs.getString(4)); 
                next.setModuleCode(rs.getString(5));
                        
                allRelevantQuizInfo.add(next);       
        }
        
        return allRelevantQuizInfo;
    }
    
    
}
