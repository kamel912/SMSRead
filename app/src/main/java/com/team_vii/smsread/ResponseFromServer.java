
package com.team_vii.smsread;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ResponseFromServer {


    @SerializedName("params")
    @Expose
    private Params params;



    Params getParams() {
        return params;
    }
}
