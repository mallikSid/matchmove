package com.projext.matchMove.domain;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionData {

    private Date startDate;

    private Date endDate;

    private String txType;

    private Double txAmount;
}
