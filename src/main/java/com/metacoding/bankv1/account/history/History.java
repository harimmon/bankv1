package com.metacoding.bankv1.account.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "history_tb")
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer withdrawNumber; // 1111 (FK)
    private Integer depositNumber; // 2222 (FK)
    private Integer amount; // 100원
    private Integer withdrawBalance; // 900원 그 시점에 잔액
    private Timestamp createdAt; // 생성날짜 (insert 된 시간)
}
