package com.projext.matchMove.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressDetails {

	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;
	
}
