package com.projext.matchMove.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="address_id")
	private UUID id;
	
	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;

}
