package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizAnswer;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizEvent;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizQuestion;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MOBILE_DATABASE";
    private static final int DATABASE_VERSION = 2;
    private static DatabaseHelper sInstance;


    // Table Names
    public static final String TABLE_MONUMENTS = "monuments";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_ANSWERS = "ansers";
    public static final String TABLE_ANSWERS_POOL = "answersPool";
    public static final String TABLE_EVENTS_POOL = "eventsPool";


    // Monument Table Columns
    private static final String KEY_MON_ID = "monumentId";
    private static final String KEY_STATUS = "status";
    private static final String KEY_QUIZ_STATUS = "quizStatus";
    private static final String KEY_IMG_URL = "imgURL";
    private static final String KEY_MON_NAME = "monumentName";
    private static final String KEY_MON_DESC = "monumentDesc";
    private static final String KEY_WIFI_ID = "wifiID";

    // Question table Columns
    private static final String KEY_QUESTION_ID = "questionId";
    private static final String KEY_MON_ID_FK = "monIdFk";
    private static String KEY_QUESTION = "question";
    private static String KEY_QUESTION_ANSWERED = "questionAnswered";


    // Answer table Columns
    private static final String KEY_ANSWER_ID = "answerId";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUESTION_ID_FK = "questionIdFk";
    private static final String KEY_CORRECT = "correct";

    //answer pool table
    private static final String ANSWER_POOL_ID = "answerId";
    private static final String ANSWER_POOL_QUESTION_ID = "questionId";
    private static final String ANSWER_POOL_TIME = "time";
    private static final String ANSWER_POOL_CORRECT = "correct";
    private static final String ANSWER_POOL_ACK = "ack";


    //answer pool table
    private static final String EVENT_POOL_ID = "answerId";
    private static final String EVENT_POOL_TYPE = "type";
    private static final String EVENT_POOL_VALUE = "value";
    private static final String EVENT_POOL_MON_ID = "monId";
    private static final String EVENT_POOL_ACK = "ack";
    private SenderService.ServiceHandler currentPool;


    /**
     * Class constructor is private to prevent direct instantiation.
     *
     * @param context
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * GetInstace method
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_MONUMENT_TABLE = "CREATE TABLE " + TABLE_MONUMENTS + "(" +
                KEY_MON_ID + " INTEGER PRIMARY KEY,"
                + KEY_STATUS + " TEXT,"
                + KEY_IMG_URL + " TEXT,"
                + KEY_MON_NAME + " TEXT,"
                + KEY_QUIZ_STATUS + " TEXT,"
                + KEY_MON_DESC + " TEXT,"
                + KEY_WIFI_ID + " TEXT"
                + ")";

        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" +
                KEY_QUESTION_ID + " INTEGER PRIMARY KEY," +
                KEY_MON_ID_FK + " INTEGER REFERENCES " + TABLE_MONUMENTS + "," +
                KEY_QUESTION + " TEXT," +
                KEY_QUESTION_ANSWERED + " INTEGER" + ")";

        String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "(" +
                KEY_ANSWER_ID + " INTEGER PRIMARY KEY," +
                KEY_QUESTION_ID_FK + " INTEGER REFERENCES " + TABLE_QUESTIONS + "," +
                KEY_ANSWER + " TEXT," +
                KEY_CORRECT + " INTEGER" +
                ")";

        String CREATE_ANSWER_POOL_TABLE = "CREATE TABLE " + TABLE_ANSWERS_POOL + "(" +
                ANSWER_POOL_ID + " INTEGER PRIMARY KEY," +
                ANSWER_POOL_QUESTION_ID + " INTEGER," +
                ANSWER_POOL_TIME + " INTEGER," +
                ANSWER_POOL_CORRECT + " INTEGER," +
                ANSWER_POOL_ACK + " INTEGER" +
                ")";

        String CREATE_EVENT_POOL_TABLE = "CREATE TABLE " + TABLE_EVENTS_POOL + "(" +
                EVENT_POOL_ID + " INTEGER PRIMARY KEY," +
                EVENT_POOL_TYPE + " TEXT," +
                EVENT_POOL_VALUE + " TEXT," +
                EVENT_POOL_ACK + " INTEGER," +
                EVENT_POOL_MON_ID + " INTEGER" +
                ")";

        sqLiteDatabase.execSQL(CREATE_MONUMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_ANSWER_TABLE);
        sqLiteDatabase.execSQL(CREATE_ANSWER_POOL_TABLE);
        sqLiteDatabase.execSQL(CREATE_EVENT_POOL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS_POOL);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_POOL);
            onCreate(sqLiteDatabase);
        }
    }

    /**
     * Sqlite disable foreign key constrain by default, so is necessary to enable it by simply override onOpen method
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }


    public void insertQuestion(int questionID, int monumentID, String question, int qAnswered) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_QUESTION_ID, questionID);
        contentValues.put(KEY_MON_ID_FK, monumentID);
        contentValues.put(KEY_QUESTION, question);
        contentValues.put(KEY_QUESTION_ANSWERED, qAnswered);

        db.insert(TABLE_QUESTIONS, null, contentValues);
    }

    /**
     * Function that allows a monument to be inserted in the Database
     *
     * @param monumentID
     * @param status
     * @param imgURL
     * @param monName
     * @param monDesc
     * @param wifiID
     */
    public void insertMonument(int monumentID, String status, String quizStatus, String imgURL, String monName,
                               String monDesc, String wifiID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MON_ID, monumentID);
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_QUIZ_STATUS, quizStatus);
        contentValues.put(KEY_IMG_URL, imgURL);
        contentValues.put(KEY_MON_NAME, monName);
        contentValues.put(KEY_MON_DESC, monDesc);
        contentValues.put(KEY_WIFI_ID, wifiID);

        db.insert(TABLE_MONUMENTS, null, contentValues);
    }

    /**
     * Function that allows a new answer to be inserted
     *
     * @param answerID
     * @param questionID
     * @param answer
     * @param correct
     */
    public void insertAnswer(int answerID, int questionID, String answer, int correct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ANSWER_ID, answerID);
        contentValues.put(KEY_QUESTION_ID_FK, questionID);
        contentValues.put(KEY_ANSWER, answer);
        contentValues.put(KEY_CORRECT, correct);
        db.insert(TABLE_ANSWERS, null, contentValues);
    }

    /**
     * Function that updates the monument status
     *
     * @param monumentID
     * @param status
     */
    public void updateMonumentStatus(int monumentID, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strFilter = KEY_MON_ID + "=" + monumentID;
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, status);
        db.update(TABLE_MONUMENTS, args, strFilter, null);
        addEventPool(monumentID, "status", status);
    }

    /**
     * Function that updates the monument status
     *
     * @param monumentID
     * @param status
     */
    public void updateMonumentQuizStatus(int monumentID, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strFilter = KEY_MON_ID + "=" + monumentID;
        ContentValues args = new ContentValues();
        args.put(KEY_QUIZ_STATUS, status);
        db.update(TABLE_MONUMENTS, args, strFilter, null);
        addEventPool(monumentID, "quizStatus", status);
    }

    /**
     * Function that updates a entry in table Question
     *
     * @param questionID
     * @param qAnsweredID
     */
    public void updateQuestion(int questionID, int qAnsweredID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String strFilter = KEY_QUESTION_ID + "=" + questionID;
        ContentValues args = new ContentValues();
        args.put(KEY_QUESTION_ANSWERED, qAnsweredID);

        db.update(TABLE_QUESTIONS, args, strFilter, null);
    }

    /**
     * Function that verifies if a given table has data or not
     *
     * @param tableName Name of the table
     * @return true if empty, false otherwise
     */
    public boolean tableIsEmpty(String tableName) {
        Cursor mCursor = getReadableDatabase().rawQuery("SELECT * FROM " + tableName, null);

        if (mCursor.getCount() == 0) {
            mCursor.close();
            return true;
        } else {
            mCursor.close();
            return false;
        }

    }


    public int[] getTotalQuestionAndAnswertedQuestions() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS, null);
        int totalNumber = mCursor.getCount();
        mCursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + KEY_QUESTION_ANSWERED + " = 1", null);
        int answered = mCursor.getCount();
        mCursor.close();
        return new int[]{totalNumber, answered};
    }


    /**
     * Simple function that deletes the database
     *
     * @param context
     */
    public void deleteDataBase(Context context) {

        SQLiteDatabase db = this.getWritableDatabase();
        boolean b = db.isReadOnly();
        db.execSQL("delete from " + TABLE_ANSWERS);
        db.execSQL("delete from " + TABLE_QUESTIONS);
        db.execSQL("delete from " + TABLE_MONUMENTS);
        db.execSQL("delete from " + TABLE_ANSWERS_POOL);
        db.execSQL("delete from " + TABLE_EVENTS_POOL);

        // context.getApplicationContext().deleteDatabase(DATABASE_NAME);
        System.out.println("Database has been deleted");
    }

    /**
     * Function that builds an ArrayList of Monument Data Objects from data base
     *
     * @return ArrayList<MonumentData> e
     */
    public ArrayList<MonumentData> buildMonumentsFromDB() {
        ArrayList<MonumentData> monumentDataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_MONUMENTS, null);

        while (mCursor.moveToNext()) {
            int monID = mCursor.getInt(0);
            String status = mCursor.getString(mCursor.getColumnIndex(KEY_STATUS));
            String quizStatus = mCursor.getString(mCursor.getColumnIndex(KEY_QUIZ_STATUS));
            String imURL = mCursor.getString(mCursor.getColumnIndex(KEY_IMG_URL));
            String monName = mCursor.getString(mCursor.getColumnIndex(KEY_MON_NAME));
            String monDesc = mCursor.getString(mCursor.getColumnIndex(KEY_MON_DESC));
            String wifiID = mCursor.getString(mCursor.getColumnIndex(KEY_WIFI_ID));
            MonumentData monumentData = new MonumentData(imURL, monName, monDesc, wifiID, monID);
            monumentData.setStatus(status);
            monumentData.setQuizStatus(status);

            monumentDataList.add(monumentData);
        }

        mCursor.close();
        return monumentDataList;
    }

    /**
     * Function that builds all QuizQuestions related with a monument
     *
     * @param monumentID
     * @return ArrayList<QuizQuestion> q
     */
    public ArrayList<QuizQuestion> buildQuizQuestionFromDB(int monumentID) {

        ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT " + KEY_QUESTION_ID + "," + KEY_QUESTION + " FROM " + TABLE_QUESTIONS +
                " WHERE " + KEY_MON_ID_FK + " = " + Integer.toString(monumentID), null);

        while (mCursor.moveToNext()) {
            int questionID = mCursor.getInt(0);
            String question = mCursor.getString(1);
            Object[] res = this.getAnswer(questionID, db);
            ArrayList<String> answers = (ArrayList<String>) res[0];
            String correctAnswer = (String) res[1];
            HashMap<String, Integer> map = (HashMap<String, Integer>) res[2];
            quizQuestions.add(new QuizQuestion(questionID, question, correctAnswer, answers, map));
        }

        mCursor.close();
        return quizQuestions;
    }

    /**
     * Function that builds possible answers for a question
     * Auxiliary function of buildQuizQuestionFromDB
     *
     * @param questionID
     * @param db
     * @return Object[] o
     */
    private Object[] getAnswer(int questionID, SQLiteDatabase db) {

        ArrayList<String> answersList = new ArrayList<>();
        HashMap<String, Integer> answersID = new HashMap<>();
        String correctAnswer = null;

        Cursor cursor = db.rawQuery("SELECT " + KEY_ANSWER + "," + KEY_CORRECT + "," + KEY_ANSWER_ID + " FROM "
                + TABLE_ANSWERS + " WHERE " + KEY_QUESTION_ID_FK + " = "
                + Integer.toString(questionID), null);

        while (cursor.moveToNext()) {

            String answer = cursor.getString(0);
            answersList.add(answer);
            int correct = cursor.getInt(1);
            int id = cursor.getInt(2);
            answersID.put(answer, id);

            if (correct == 1) {
                correctAnswer = answer;

            }
        }

        cursor.close();

        return new Object[]{answersList, correctAnswer, answersID};
    }

    /**
     * Function that checks if the quiz was downloaded for a monument
     *
     * @param monumentID
     * @return true if question were downloaded , false otherwise
     */
    public boolean questionForMonumentDownload(int monumentID) {

        boolean res = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " + " FROM " +
                TABLE_QUESTIONS + " WHERE " + KEY_MON_ID_FK + " = "
                + Integer.toString(monumentID), null);

        if (cursor.getCount() > 0)
            res = true;

        return res;
    }

    /**
     * Function that updates if a question was answered
     *
     * @param question
     */
    public void updateQuestionAnswered(int question) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION_ANSWERED, 1);
        db.update(TABLE_QUESTIONS, cv, KEY_QUESTION_ID + "= ?", new String[]{"" + question});
    }

    /**
     * Function that verifies if a given quiz was already answered
     *
     * @param monID
     * @return
     */
    public boolean monQuestionAnswered(int monID) {
        boolean res = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_QUIZ_STATUS + " FROM " +
                TABLE_MONUMENTS + " WHERE " + KEY_MON_ID + " = "
                + Integer.toString(monID), null);

        if (cursor.moveToFirst()) {
            String a = cursor.getString(cursor.getColumnIndex(KEY_QUIZ_STATUS));
            if (a.equals(MonumentData.ANSWERED) || a.equals(MonumentData.INTERRUPTED))
                res = true;
        }

        return res;
    }


    public void insertAnswersPool(List<QuizAnswer> answers) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (QuizAnswer answer : answers) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ANSWER_POOL_QUESTION_ID, answer.getQuestionId());
            contentValues.put(ANSWER_POOL_CORRECT, answer.isCorrect());
            contentValues.put(ANSWER_POOL_TIME, answer.getTime());
            contentValues.put(ANSWER_POOL_ACK, false);
            db.insert(TABLE_ANSWERS_POOL, null, contentValues);
        }

        synchronized (currentPool) {
            currentPool.notify();
        }

    }

    public void addEventPool(int idMun, String type, String value) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_POOL_MON_ID, idMun);
        contentValues.put(EVENT_POOL_TYPE, type);
        contentValues.put(EVENT_POOL_VALUE, value);
        contentValues.put(EVENT_POOL_ACK, 0);

        db.insert(TABLE_EVENTS_POOL, null, contentValues);

        synchronized (currentPool) {
            currentPool.notify();
        }

    }

    public List<QuizAnswer> getPoolQuizAnswers() {

        ArrayList<QuizAnswer> quizAnswers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_ANSWERS_POOL +
                " WHERE " + ANSWER_POOL_ACK + " = 0", null);

        while (mCursor.moveToNext()) {

            int id = mCursor.getInt(mCursor.getColumnIndex(ANSWER_POOL_ID));
            int questionID = mCursor.getInt(mCursor.getColumnIndex(ANSWER_POOL_QUESTION_ID));
            boolean correctAnswer = mCursor.getInt(mCursor.getColumnIndex(ANSWER_POOL_CORRECT)) == 1;
            long time = mCursor.getLong(mCursor.getColumnIndex(ANSWER_POOL_TIME));
            QuizAnswer answer = new QuizAnswer(id, questionID, 0, correctAnswer, time);

            quizAnswers.add(answer);
        }

        mCursor.close();
        return quizAnswers;

    }

    public List<QuizEvent> getEventPool() {

        ArrayList<QuizEvent> eventPool = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS_POOL +
                " WHERE " + ANSWER_POOL_ACK + " = 0", null);

        while (mCursor.moveToNext()) {

            int id = mCursor.getInt(mCursor.getColumnIndex(EVENT_POOL_ID));
            int munId = mCursor.getInt(mCursor.getColumnIndex(EVENT_POOL_MON_ID));
            String type = mCursor.getString(mCursor.getColumnIndex(EVENT_POOL_TYPE));
            String value = mCursor.getString(mCursor.getColumnIndex(EVENT_POOL_VALUE));

            QuizEvent answer = new QuizEvent(id, munId, type, value);

            eventPool.add(answer);
        }

        mCursor.close();
        return eventPool;

    }

    public void setCurrentPool(SenderService.ServiceHandler currentPool) {
        this.currentPool = currentPool;
    }


    public void updateAnswerPoolAck(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ANSWER_POOL_ACK, 1);
        db.update(TABLE_ANSWERS_POOL, cv, ANSWER_POOL_ID + "= ?", new String[]{"" + id});
    }
}
