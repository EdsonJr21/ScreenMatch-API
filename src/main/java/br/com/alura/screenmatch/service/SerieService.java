package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.Dto.EpisodioDto;
import br.com.alura.screenmatch.Dto.SerieDto;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDto> obetendoSerie() {
        return converteDados(repositorio.findAll());
    }

    public List<SerieDto> Obtertop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());

    }

    private List<SerieDto> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDto(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                        s.getAtores(),s.getPoster(),s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterlancamentos() {
        return converteDados(repositorio.lancamentosMaisRecentes());
    }

    public SerieDto obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                    s.getAtores(),s.getPoster(),s.getSinopse());
        }

        return null;
    }

    public List<EpisodioDto> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public List<EpisodioDto> obterTemporadasPorNumero(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterGenero(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return converteDados(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDto> obterTopEps(Long id) {
        Optional<Serie> serieOptional = repositorio.findById(id);
        if (serieOptional.isPresent()) {
            Serie serie = serieOptional.get();
            return repositorio.topEpisodioPorSerie(serie)
                    .stream()
                    .map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
