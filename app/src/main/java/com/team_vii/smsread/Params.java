
package com.team_vii.smsread;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Params {


    @SerializedName("querystring")
    @Expose
    private Querystring querystring;

    Querystring getQuerystring() {
        return querystring;
    }
}
