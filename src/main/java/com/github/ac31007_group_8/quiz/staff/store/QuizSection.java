package com.github.ac31007_group_8.quiz.staff.store;

import java.util.List;

/**
 * This class represents a QuizSection to be displayed, which is a pair with two elements, the first being a question and the second being a list of answers for that question.
 * (Refactored form of what was previously a Pair<Question, List<Answer>>)
 *
 * Created by Can on 23/02/2017.
 */
public class QuizSection {

    public QuizSection()
    {

    }

    private Question question;
    private List<Answer> answers;

    public Question getQuestion()
    {
        return question;
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public void setQuestion(Question question){
        this.question = question;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }


}
