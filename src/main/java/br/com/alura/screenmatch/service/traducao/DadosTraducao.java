package br.com.alura.screenmatch.service.traducao;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTraducao(@JsonAlias(value = "responseData") DadosResposta dadosResposta) {
    private static final String responseData = "{\n" +
            "    \"translatedText\": \"Olá mundo!\",\n" +
            "    \"match\": 1,\n" +
            "    \"quotaFinished\": false,\n" +
            "    \"mtLangSupported\": null,\n" +
            "    \"responseDetails\": \"\",\n" +
            "    \"responseStatus\": 200,\n" +
            "    \"responderId\": null,\n" +
            "    \"exception_code\": null,\n" +
            "    \"matches\": []\n" +
            "}";
}