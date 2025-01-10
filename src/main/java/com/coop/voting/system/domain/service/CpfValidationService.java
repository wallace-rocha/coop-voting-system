package com.coop.voting.system.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CpfValidationService {

    @Value("${api.user-info.url}")
    private String url;

    public void checkCpfEligibility(String cpf) throws Exception {
        /*String responseEligibility = HttpClientUtil.checkCpfEligibility(url, cpf);
        if ("UNABLE_TO_VOTE".equalsIgnoreCase(responseEligibility))
            throw new BusinessException("Member unable to vote.");*/
    }
}