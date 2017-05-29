package com.nonvoid.barcrawler.datalayer.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matt on 5/29/2017.
 */

public class BaseResponse {
    @SerializedName("status")
    String status;

    public boolean isSuccess(){
        return "success".equalsIgnoreCase(status);
    }
}
