package com.projext.matchMove.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContactDetails {

	private String emailId;
	
	private String homePhone;
	
	private String workPhone;
}
