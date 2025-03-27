package com.metacoding.bankv1.account;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountResponse {

    @AllArgsConstructor
    @Data
    public static class DetailDTO {
        private int accountNumber;
        private int accountBalance;
        private String accountOwner;
        private String createdAt; //timestamp타입이 아님 substr로 잘라냈으니까
        private int wNumber;
        private int dNumber;
        private int amount;
        private int balance;
        private String type;
    }
}