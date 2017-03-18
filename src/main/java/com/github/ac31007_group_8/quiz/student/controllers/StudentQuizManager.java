package com.github.ac31007_group_8.quiz.student.controllers;

import com.github.ac31007_group_8.quiz.staff.controllers.QuizManager;
import com.github.ac31007_group_8.quiz.student.models.StudentQuizModel;
import com.github.ac31007_group_8.quiz.util.Init;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import java.util.logging.Logger;



import com.github.ac31007_group_8.quiz.staff.store.*;
import com.github.ac31007_group_8.quiz.util.GoogleMail;


import static spark.Spark.*;

import io.github.gitbucket.markedj.*;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.slf4j.LoggerFactory;




/**
 * @author Can G, Allan M
 */
public class StudentQuizManager {

    public StudentQuizManager(){

    }
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QuizManager.class);
    
    @Init
    public static void init() {
        get("/student/takeQuiz", StudentQuizManager::serveTakeQuiz);
        post("/student/takeQuiz", "application/json", StudentQuizManager::receiveTakeQuiz);
    }

    public static Object serveTakeQuiz(Request req, Response res){
        
        
        //Sat up template engine
        TemplateEngine eng = new MustacheTemplateEngine();
        HashMap<String, Object> map = new HashMap<>();

        //Validate quizID - is valid input provided?
        String quizIDString = req.queryParams("quizID");
        if (quizIDString == (null) || quizIDString.equals(""))
        {
            //display error page
            throw halt(400, eng.render(eng.modelAndView(map, "student/invalidQuiz.mustache")));
        }
        int quizID = Integer.parseInt(quizIDString);

        //Validate quizID - does it exist?
        StudentQuizModel quizModel = new StudentQuizModel();
        Quiz quiz = quizModel.getCompleteQuiz(quizID);
        if (quiz == null)
        {
            throw halt(400, eng.render(eng.modelAndView(map, "student/invalidQuiz.mustache")));
        }

        //Don't show quiz if unpublished
        if (!quiz.isPublish_status())
        {
            return eng.render(eng.modelAndView(map, "student/unpublishedQuiz.mustache"));
        }

      
        List<Question> allQuestions = quiz.getQuestions();
        for (Question q:allQuestions){
            q.setQuestion(Marked.marked(q.getQuestion()));   
        }
       
        map.put("quizID", quizID);
        map.put("allQuestions", allQuestions);

        return eng.render(eng.modelAndView(map, "student/takeQuiz.mustache"));

    }

    
    
    
    
    public static Object receiveTakeQuiz(Request req, Response res){

        //receive params: quizID, answerIDs, questionIDs
        int quizID = Integer.parseInt(req.queryParams("quizID"));
        int duration = Integer.parseInt(req.queryParams("duration"));

        //retrieve questionIDs in the quiz.
        StudentQuizModel quizModel = new StudentQuizModel();
        List<Question> questions = quizModel.getQuestions(quizID);

        //get ticked answers' answerIDs:
        List<Integer> answerIDs = new ArrayList<>();

        for (Question question: questions) {
            int questionID = question.getQuestionID();
            String[] values = req.queryParamsValues(("question" + Integer.toString(questionID)));

            if (values == null) Logger.getGlobal().info("No answers submitted for question with ID " + questionID);

            for (String value:values) {
                answerIDs.add(Integer.parseInt(value));
            }
        }


        Quiz quiz = quizModel.getCompleteQuiz(quizID);
        int score = calculateScore(answerIDs, quiz);
        int studentID = 1;
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
        quizModel.writeResult(score, quizID, studentID, sqlDate, duration, answerIDs);

        
        //SEND EMAIL
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
        String pageHtml = createHtml(questions, answerIDs, quiz, score, df.format(sqlDate), duration);
        
       
        
        try{
            String message = "Quiz results for quiz: "+quiz.getTitle();
            sendEmail(pageHtml, message);
        }
        catch(FileNotFoundException fnfe){
            LOGGER.error("File not found", fnfe);
           
        }
        catch(AddressException ae){
            LOGGER.error("Wrong format of email address", ae);
           //add could not send email text to endQuiz
        }
        catch(MessagingException me){
            LOGGER.error("MessagingException", me);
           
        }
         
        
        
        HashMap<String, Object> map = new HashMap<>();

        TemplateEngine eng = new MustacheTemplateEngine();
        return eng.render(eng.modelAndView(map, "student/endQuiz.mustache"));

    }
    
    
    private static void sendEmail(String pageHtml, String message) throws FileNotFoundException, AddressException, MessagingException{
       
        String fileName = UUID.randomUUID().toString()+".html";
        String path = StudentQuizManager.class.getResource("/temp").getPath();
        
        File targetFile = new File(path, fileName);
        targetFile.deleteOnExit();//if for some reason is not deleted when has to
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            writer.println(pageHtml);
            writer.flush();
        }
       

        GoogleMail.Send("QuizResultSender1@gmail.com", "spamAllanToDeath", "vladislav.voicehovich@gmail.com", "Quiz Results", message, targetFile);
         
         
        targetFile.delete();
       
        
        
        
    }
    
    
    private static String createHtml(List<Question> allQuestions, List<Integer> answersByStudent, Quiz quiz, int score,String whenTaken, int duration) {

      
            Options options = new  Options();
            options.setXhtml(true);//without this does not close img tag and cannot create pdf
            
            //I have tried using html generation libraries (rendersnake and ecs), but it looks even messier due to iteration
            //And gmail attachment viewer does not support <style> tag -- only inline
            
            //SUMMARY
            String quizModuleCode = "<li class='summaryItem' style='font-weight:bolder;'>Module code: <span style='font-weight:normal; padding-left:10px; color:yellowgreen;'>"+quiz.getModule_id()+"</span></li>";
            String quizTitle = "<li class='summaryItem' style='font-weight:bolder;'>Title: <span style='font-weight:normal; padding-left:10px; color:yellowgreen;'>"+quiz.getTitle()+"</span></li>";
            String quizScore = "<li class='summaryItem' style='font-weight:bolder;'>Score: <span style='font-weight:normal; padding-left:10px; color:yellowgreen;'>"+score+"</span></li>";
            String quizTimeTaken = "<li class='summaryItem' style='font-weight:bolder;'>Time taken: <span style='font-weight:normal; padding-left:10px; color:yellowgreen;'>"+duration+"</span></li>";
            String quizWhenTaken = "<li class='summaryItem' style='font-weight:bolder;'>Date: <span style='font-weight:normal; padding-left:10px; color:yellowgreen;'>"+whenTaken+"</span></li>";
            String summary = "<div style='padding:25px; background-color:grey;color:white;'><h1>Summary</h1><ul style='font-size:larger;'>"+quizModuleCode+quizTitle+quizScore+quizTimeTaken+quizWhenTaken+"</ul></div>";
            
            
            //QUESTIONS
            String allQuestionHtml = "";
            int counter =0;
            for (Question q:allQuestions){
                counter++;
                String qTextDiv = "<div style='padding-top:20px;'><label style='font-weight:bolder; font-size:large;'>Text:</label>"+Marked.marked( q.getQuestion(), options)+"</div>";                
                String explanationDiv = "<div style='padding-top:20px;'><label style='font-weight:bolder; font-size:large;'>Explanation:</label><div>"+q.getExplanation()+"</div></div>";
                ArrayList<Answer> qAnswers = q.getAnswers();
                String answerList ="";
                for (Answer a:qAnswers){
                    
                    String correctnessSpan = a.isCorrect()?"<span style='padding-left:10px; color:#3dbc3d' title='correct answer'>&euro;</span>":"<span style='padding-left:10px; color:red' title='incorrect answer'>&#x2717;</span>";
                    
                    if (answersByStudent.contains(a.getAnswer_id())){
                        String answeredCorrectlySpan = a.isCorrect()?"<span style='padding-left:10px;' title='your answer'>&#9899;</span>":"<span style='padding-left:10px;' title='your answer'>&#9898;</span>" ; 
                        answerList+= "<li>"+a.getAnswer()+correctnessSpan+answeredCorrectlySpan+"</li>";
                    }
                    else{
                        answerList+= "<li>"+a.getAnswer()+correctnessSpan+"</li>";
                    }
                    
                    
                    
                }
                answerList = "<div style='padding-top:20px;'><label style='font-weight:bolder; font-size:large;'>Options:</label><ol>"+answerList+"</ol></div>";
                
               
                String bgColor = "background-color: "+ ((counter%2==1)? "papayawhip":"none");
                allQuestionHtml+=  "<div style='padding:25px; "+bgColor+";' ><h1>Question "+counter+"</h1>"+qTextDiv+answerList+explanationDiv+"</div>";
                
            }
            
            //PUT TOGETHER
           
            String contentDiv = "<div style='margin:25px'>"+summary+allQuestionHtml+"</div>";
            String allHtml = "<html><head></head><body>"+contentDiv+"</body></html>";
            
            return allHtml;
            
            
      

    }
    
    
    
    
    
    public static int calculateScore(List<Integer> answerIDs, Quiz quiz){

        //calculate Score (type: INT) as: questions answered correctly out of totalquestions, as a percentage
        //count total number of questions in quiz
        //count number of questions answered correctly
        //calculate score

        int numberOfQuestions = 0;
        int numberOfCorrectlyAnsweredQuestions = 0;

        if (quiz.getQuestions()==null)
        {
            Logger.getGlobal().info("Error: cannot mark quiz - Questions are null.");
            return 0;
        }

        for (Question question : quiz.getQuestions()) {
            numberOfQuestions++;
            boolean isCorrect = true;
            for (Answer answer : question.getAnswers()) {
                if (answer.isCorrect()) //we'd like to find it in user's list of answers
                {
                    if (!isIDInList(answerIDs, answer.getAnswer_id())) isCorrect = false;
                }
                else { //we'd like NOT to find it in the user's list of answers
                    if (isIDInList(answerIDs, answer.getAnswer_id())) isCorrect = false;
                }
            }
            if (isCorrect) numberOfCorrectlyAnsweredQuestions++;
        }

        Logger.getGlobal().info("Number of Questions: " + numberOfQuestions + " Number Correctly Answered: " + numberOfCorrectlyAnsweredQuestions);

        return getPercentage(numberOfCorrectlyAnsweredQuestions, numberOfQuestions);

    }

    private static int getPercentage(int small, int large)
    {
        if (large == 0){
            Logger.getGlobal().info("Error: tried to divide by zero while calculating percentage score");
            return 0;
        }

        return Math.round((float)small/(float)large * 100f);
    }

    private static boolean isIDInList(List<Integer> ids, int idToFind){

        for (Integer number: ids) {
            if (number == idToFind) {
                return true;
            }
        }

        return false;

    }
    
    
    


}
