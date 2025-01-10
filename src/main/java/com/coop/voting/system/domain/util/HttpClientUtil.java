package com.coop.voting.system.domain.util;

import com.coop.voting.system.domain.model.exception.CpfInvalidException;
import com.coop.voting.system.domain.model.exception.ExternalApiException;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClientUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public static String checkCpfEligibility (String url, String cpf) throws Exception {
        OkHttpClient client = HttpClientUtil.client;
        Request request = new Request.Builder()
                .url(url + cpf)
                .get()
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response.code() == 404) {
                throw new CpfInvalidException("The provided CPF is invalid.");
            }
            return response.body().string();
        } catch (CpfInvalidException e) {
            throw new CpfInvalidException(e.getMessage());
        } catch (ExternalApiException e) {
            throw new ExternalApiException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}