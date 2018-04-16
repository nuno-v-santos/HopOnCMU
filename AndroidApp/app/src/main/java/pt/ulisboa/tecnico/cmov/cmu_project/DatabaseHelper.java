package pt.ulisboa.tecnico.cmov.cmu_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MOBILE_DATABASE";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper sInstance;


    // Table Names
    private static final String TABLE_MONUMENTS = "monuments";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ANSWERS = "ansers";


    // Monument Table Columns
    private static final String KEY_MON_ID = "monumentId";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IMG_URL = "imgURL";
    private static final String KEY_MON_NAME = "monumentName";
    private static final String KEY_MON_DESC = "monumentDesc";
    private static final String KEY_WIFI_ID = "wifiID";

    // Question table Columns
    private static String KEY_QUESTION_ID = "questionId";
    private static String KEY_MON_ID_FK = "monIdFk";
    private static String KEY_QUESTION = "question";
    private static String KEY_QUESTION_ANSWERED = "questionAnswered";


    // Answer table Columns
    private static String KEY_ANSWER_ID = "answerId";
    private static String KEY_ANSWER = "answer";
    private static String KEY_QUESTION_ID_FK = "questionIdFk";
    private static String KEY_CORRECT = "correct";


    // Constructor should be private to prevent direct instantiation.
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_MONUMENT_TABLE = "CREATE TABLE " + TABLE_MONUMENTS + "(" +
                KEY_MON_ID + " INTEGER PRIMARY KEY," + KEY_STATUS + " TEXT," + KEY_IMG_URL + " TEXT," +
                KEY_MON_NAME + " TEXT," + KEY_MON_DESC + " TEXT," + KEY_WIFI_ID + " TEXT" + ")";

        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" +
                KEY_QUESTION_ID + " INTEGER PRIMARY KEY," +
                KEY_MON_ID_FK + " INTEGER REFERENCES " + TABLE_MONUMENTS + "," +
                KEY_QUESTION + " TEXT," + KEY_QUESTION_ANSWERED + " INTEGER" + ")";

        String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "(" + KEY_ANSWER_ID + " INTEGER PRIMARY KEY," +
                KEY_QUESTION_ID_FK + " INTEGER REFERENCES " + TABLE_QUESTIONS + "," + KEY_ANSWER + " TEXT," + KEY_CORRECT + " INTEGER" + ")";

        sqLiteDatabase.execSQL(CREATE_MONUMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_ANSWER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MONUMENTS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
            onCreate(sqLiteDatabase);
        }
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


    public void insertMonument(int monumentID, String status, String imgURL, String monName,
                               String monDesc, String wifiID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MON_ID, monumentID);
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_IMG_URL, imgURL);
        contentValues.put(KEY_MON_NAME, monName);
        contentValues.put(KEY_MON_DESC, monDesc);
        contentValues.put(KEY_WIFI_ID, wifiID);

        db.insert(TABLE_MONUMENTS, null, contentValues);
    }


    public void insertAnswer(int answerID, int questionID, String answer, int correct) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ANSWER_ID, answerID);
        contentValues.put(KEY_QUESTION_ID_FK, questionID);
        contentValues.put(KEY_ANSWER, answer);
        contentValues.put(KEY_CORRECT, correct);

        db.insert(TABLE_ANSWERS, null, contentValues);
    }

    public void updateMonumentStatus(int monumentID, String status) {

        SQLiteDatabase db = this.getWritableDatabase();

        String strFilter = KEY_MON_ID + "=" + monumentID;
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, status);
        db.update(TABLE_MONUMENTS, args, strFilter, null);
    }

    public void updateQuestion(int questionID, int qAnswered) {

        SQLiteDatabase db = this.getWritableDatabase();
        String strFilter = KEY_QUESTION_ID + "=" + questionID;
        ContentValues args = new ContentValues();
        args.put(KEY_QUESTION_ANSWERED, qAnswered);

        db.update(TABLE_QUESTIONS, args, strFilter, null);
    }


}
