package todd.toddsdetector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vibinvarghese on 08/07/17.
 * <p>
 * This class is to store the data related to questions, options and responses
 */

public class QuestionItem implements Parcelable {

    //String Containing the question
    String question;

    //String containing the button text to be displayed for positive response
    String positiveResponse;

    //String containing the button text to be displayed for positive response
    String negativeResponse;

    //Boolean value whether the response was in the positive.
    boolean affirmitiveResponse;

    public QuestionItem() {
    }

    protected QuestionItem(Parcel in) {
        question = in.readString();
        positiveResponse = in.readString();
        negativeResponse = in.readString();
        affirmitiveResponse = in.readByte() != 0;
    }

    public static final Creator<QuestionItem> CREATOR = new Creator<QuestionItem>() {
        @Override
        public QuestionItem createFromParcel(Parcel in) {
            return new QuestionItem(in);
        }

        @Override
        public QuestionItem[] newArray(int size) {
            return new QuestionItem[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPositiveResponse() {
        return positiveResponse;
    }

    public void setPositiveResponse(String positiveResponse) {
        this.positiveResponse = positiveResponse;
    }

    public String getNegativeResponse() {
        return negativeResponse;
    }

    public void setNegativeResponse(String negativeResponse) {
        this.negativeResponse = negativeResponse;
    }

    public boolean isAffirmitiveResponse() {
        return affirmitiveResponse;
    }

    public void setAffirmitiveResponse(boolean affirmitiveResponse) {
        this.affirmitiveResponse = affirmitiveResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(positiveResponse);
        dest.writeString(negativeResponse);
    }
}
