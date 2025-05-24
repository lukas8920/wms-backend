package org.kehrbusch.security.vault.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class KeyResponse {
    @SerializedName("request_id")
    private String requestId;
    @SerializedName("data")
    private Map<String, String> data;
}