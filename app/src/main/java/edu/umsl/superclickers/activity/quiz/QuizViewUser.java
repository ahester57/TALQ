package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.HorDottedProgress;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/19/2017.
 */

public class QuizViewUser extends Fragment implements
        AnswerViewUser.AnswerListener,
        QuizGET.QuizGETController {

    private final String TAG = QuizViewUser.class.getSimpleName();

    private String userID;
    private String quizID;
    private String courseID;
    private String token;

    private Quiz curQuiz;
    private Question curQuestion;
    private int minutesLeft;
    private int secondsLeft;

    private TextView progressView;
    private TextView numQuestionsView;
    private TextView questionView;
    private TextView quizTimeView;
    private QuizGET quizGET;
    private HorDottedProgress horDottedProgress;

    private boolean resume = false;

    private QuizController qController;

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
        qController = (QuizController) getActivity();
        quizGET = qController.getQuizGET();
        quizGET.setController(this);
        if (!resume || curQuiz == null) {
            downloadQuiz();
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        numQuestionsView = (TextView) view.findViewById(R.id.num_questions);
        progressView = (TextView) view.findViewById(R.id.quiz_progress);
        quizTimeView = (TextView) view.findViewById(R.id.quiz_timer_text);
        questionView = (TextView) view.findViewById(R.id.question_text_view);
        //horDottedProgress = (HorDottedProgress) view.findViewById(R.id.progress_quiz_dots);

        currQuestion();
        return view;
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
        this.token = token;
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

        Log.d(TAG, "Set the quiz ");

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
        //horDottedProgress.nextDot();
        loadAnswerFragment();
    }

    public void prevQuestion() {
        curQuestion = curQuiz.getPrevQuestion();
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        //horDottedProgress.previousDot();
        loadAnswerFragment();
    }

    private void loadAnswerFragment() {
        questionView.setText(curQuestion.getQuestion());
        qController.setQuizIndex(curQuiz.getqNum());
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        // create instance of the answer fragment
        updateGUITimer(minutesLeft, secondsLeft);
        AnswerViewUser answerFrag = new AnswerViewUser();
        // load answer fragment into answerSection of QuizActivityUser
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment, answerFrag, FragmentConfig.KEY_ANSWER_VIEW_USER);
        ft.commit();
    }

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
