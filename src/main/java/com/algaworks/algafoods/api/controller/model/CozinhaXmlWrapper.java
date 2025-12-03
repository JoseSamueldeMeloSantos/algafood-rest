package com.algaworks.algafoods.api.controller.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.algaworks.algafoods.domain.model.Cozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.NonNull;


@JacksonXmlRootElement(localName = "cozinhas")
@Data
public class CozinhaXmlWrapper {

	@JsonProperty("cozinhas")
	@JacksonXmlElementWrapper(useWrapping = false)//desativa o embrulho da tag xml
	@NonNull
	List<Cozinha> cozinhas;
}
