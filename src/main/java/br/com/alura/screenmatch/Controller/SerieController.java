package br.com.alura.screenmatch.Controller;

import br.com.alura.screenmatch.Dto.EpisodioDto;
import br.com.alura.screenmatch.Dto.SerieDto;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servico;


    @GetMapping
    public List<SerieDto> obterSeries() {
        return servico.obetendoSerie();
    }

    @GetMapping("/top5")
    public List<SerieDto> Obtertop5Series() {
        return servico.Obtertop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterlancamentos() {
        return servico.obterlancamentos();
    }

    @GetMapping("/{id}")
    public SerieDto obterPorId(@PathVariable Long id){
        return servico.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> obterTodasTemporadas(@PathVariable Long id) {
        return servico.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDto> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero) {
        return servico.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDto> obterGenero(@PathVariable String nomeGenero) {
        return servico.obterGenero(nomeGenero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDto> obterTopEps(@PathVariable Long id) {
        return servico.obterTopEps(id);
    }
}
