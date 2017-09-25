
package com.team_vii.smsread;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Querystring {

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Number")
    @Expose
    private String number;

    String getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    String getNumber() {
        return number;
    }
}
