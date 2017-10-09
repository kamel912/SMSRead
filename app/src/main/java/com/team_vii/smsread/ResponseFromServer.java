
package com.team_vii.smsread;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ResponseFromServer {


    @SerializedName("ok")
    @Expose
    private boolean ok;

    @SerializedName("message")
    @Expose
    private String respons;

    String getRespons() {
        return respons;
    }
}
