package br.com.alura.screenmatch.Dto;

public record SerieDto( Long id,
                        String titulo,
                        Integer totalTemporadas,
                        Double avaliacao,
                        String atores,
                        String poster,
                        String sinopse) {

}
