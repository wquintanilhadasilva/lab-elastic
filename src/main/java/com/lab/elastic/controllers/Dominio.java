package com.lab.elastic.controllers;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Dominio {
	private String descricao;
	private Integer ordem;
	private String payload;
}
