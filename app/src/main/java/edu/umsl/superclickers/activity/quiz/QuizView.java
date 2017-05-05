package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.umsl.superclickers.activity.helper.HorDottedProgress;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 5/4/2017.
 *
 */

public abstract class QuizView extends Fragment implements
        AnswerView.AnswerListener,
        QuizGET.QuizGETController {

    private final String TAG = QuizView.class.getSimpleName();

    String userID;
    String quizID;
    String courseID;


    Quiz curQuiz;
    Question curQuestion;
    int minutesLeft;
    int secondsLeft;

    TextView progressView;
    TextView numQuestionsView;
    TextView questionView;
    TextView quizTimeView;
    QuizGET quizGET;
    HorDottedProgress horDottedProgress;

    boolean resume = false;

    QuizController qController;

    interface QuizController {
        QuizGET getQuizGET();
        void startQuizTimer();
        void setQuizIndex(int qNum);
        int getQuizIndex();
        void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers);

    }

    public void setQuizInfo(String quizID, String userID, String courseID) {
        this.quizID = quizID;
        this.userID = userID;
        this.courseID = courseID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        qController = (QuizView.QuizController) getActivity();
        quizGET = qController.getQuizGET();
        quizGET.setController(this);
        if (!resume || curQuiz == null) {
            downloadQuiz();
        }

    }

    @Override
    public void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers) {
        qController.setSelectedAnswers(selectedAnswers);
    }

    public void setResume(boolean flag) {
        resume = flag;
    }

    public void downloadQuiz() {
        quizGET.getToken(quizID);
    }

    @Override
    public void setToken(String token) {
        // Get the quiz after got token
        quizGET.getQuiz(userID, courseID, quizID, token);
    }

    public void attachQuiz(Quiz quiz, int qNum) {
        if (quiz != null) {
            curQuiz = quiz;
            // set quiz index
            curQuiz.setqNum(qNum);
        } else {
            curQuiz = null;
        }
    }

    @Override
    public void setQuiz(Quiz quiz) {
        curQuiz = quiz;
        if (questionView != null) {
            currQuestion();
        }
        numQuestionsView.setText("/" + curQuiz.getQuestions().size());
        if (horDottedProgress != null) {
            horDottedProgress.setDotAmount(curQuiz.getQuestions().size());
        }

        Log.d(TAG, "Set the group quiz ");

    }

    public void currQuestion() {
        if(curQuiz == null) {
            setBSQuiz();
        }
        numQuestionsView.setText("/" + curQuiz.getQuestions().size());
        if (horDottedProgress != null) {
            horDottedProgress.setDotAmount(curQuiz.getQuestions().size());
            horDottedProgress.setProgress(curQuiz.getqNum());
        }
        curQuestion = curQuiz.getQuestion();
        startTimer();
        loadAnswerFragment();
    }

    public void nextQuestion() {
        curQuestion = curQuiz.getNextQuestion();
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        if (horDottedProgress != null) {
            horDottedProgress.nextDot();
        }
        loadAnswerFragment();
    }

    public void prevQuestion() {
        curQuestion = curQuiz.getPrevQuestion();
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        if (horDottedProgress != null) {
            horDottedProgress.previousDot();
        }
        loadAnswerFragment();
    }

    abstract void loadAnswerFragment();

    public int getQuizTime() {
        return curQuiz.getTimedLength();
    }

    public String getQuizID() { return curQuiz.get_id(); }

    public void updateGUITimer(int minutes, int seconds) {
        minutesLeft = minutes;
        secondsLeft = seconds;

        String time = String.format(Locale.getDefault(), "%02d", minutesLeft) +
                String.format(Locale.getDefault(), ":%02d", secondsLeft);
        if (minutesLeft > curQuiz.getTimedLength() / 2) {
            quizTimeView.setTextColor(Color.GREEN);
        } else if (minutesLeft > curQuiz.getTimedLength() / 2 / 2) {
            quizTimeView.setTextColor(Color.YELLOW);
        } else if (minutesLeft == 0 && secondsLeft == 1) {
            quizTimeView.setTextColor(Color.CYAN);
        } else {
            quizTimeView.setTextColor(Color.RED);
        }

        quizTimeView.setText(time);
    }

    void startTimer() {
        qController.startQuizTimer();
    }

    @Override
    public Question getQuestion() {
        return curQuestion;
    }


    public void setBSQuiz() {
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("A", "1", 0, "id"));
        answers.add(new Answer("B", "2", 1, "id"));
        answers.add(new Answer("C", "3", 2, "id"));
        answers.add(new Answer("D", "4", 3, "id"));
        Question question = new Question("id", "title", "What is log_10 1000", 22, answers);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);
        setQuiz(new Quiz("ddd", "description", "what is log_10 1000?", "now", "never",
                questions, "sessionId", false, 0));
    }

}
