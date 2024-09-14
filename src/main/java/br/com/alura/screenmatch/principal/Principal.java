package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private Optional<Serie> buscaSerie;

    @Autowired
    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar Série por titulo
                    5 - Buscar Série por ator
                    6 - Top 5 Séries
                    7 - Busce por categoria
                    8 - Buscar por temporada e avaliação
                    9 - Busca episódio por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios por data
                                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    ListarSerires();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarPorAtor();
                    break;
                case 6:
                    top5Series();
                    break;
                case 7:
                    SeriPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarPorEpisodio();
                    break;
                case 10:
                    episodioTopPorSerie();
                    break;
                case 11:
                    buscarEpisodioPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);

        // Busca os episódios e salva junto com a série
        List<Episodio> episodios = buscarEpisodiosParaSerie(serie);
        serie.setEpisodios(episodios);

        repositorio.save(serie);
        System.out.println(dados);
    }

    private List<Episodio> buscarEpisodiosParaSerie(Serie serie) {
        List<Episodio> episodios = new ArrayList<>();
        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + serie.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            List<Episodio> episodiosTemporada = dadosTemporada.episodios().stream()
                    .map(e -> new Episodio(dadosTemporada.numero(), e))
                    .collect(Collectors.toList());
            episodios.addAll(episodiosTemporada);
        }
        return episodios;
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        ListarSerires();
        System.out.println("Digite o nome da Série: ");
        var nomeDaSerie = leitura.nextLine();

        Optional<Serie> serieEncontrada = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeDaSerie.toLowerCase()))
                .findFirst();

        if (serieEncontrada.isPresent()) {
            Serie serie = serieEncontrada.get();

            List<Episodio> episodios = repositorio.obterEpisodiosPorTemporada(serie.getId(), (long) serie.getTotalTemporadas());

            if (episodios.isEmpty()) {
                // Busca os episódios se não houver no banco de dados
                episodios = buscarEpisodiosParaSerie(serie);
                serie.setEpisodios(episodios);
                repositorio.save(serie);
            }

            episodios.forEach(System.out::println);

        } else {
            System.out.println("Série não encontrada");
        }
    }
    private void ListarSerires() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        buscaSerie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (buscaSerie.isPresent()) {
            System.out.println("Dados da Série: " + buscaSerie.get());

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarPorAtor() {
        System.out.println("Digite o nome para realizar a busca");
        var nomeAtor = leitura.nextLine();
        System.out.println("Digite a avaliação: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que o: " + nomeAtor + " trabalhou");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + "avaliação: " + s.getAvaliacao()));
    }

    private void top5Series() {
        List<Serie> top5Series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5Series.forEach(s -> System.out.println(s.getTitulo() + "Avaliacao: " + s.getAvaliacao()));
    }

    private void SeriPorCategoria() {
        System.out.println("buscar séries de qual categoria/gênero?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromString(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Série da categoria: " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar até quantas temporadas ?");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Apartir de qual avaliação ?");
        var avalicao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avalicao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    private void buscarPorEpisodio() {
        System.out.println("Digite o nome do episódio para realizar a busca");
        var trechoEpisodio = leitura.nextLine().trim();
        List<Episodio> episodioEncontrado = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodioEncontrado.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void episodioTopPorSerie() {
        buscarSeriePorTitulo();
        if (buscaSerie.isPresent()) {
            Serie serie = buscaSerie.get();

            List<Episodio> topEpisodios = repositorio.topEpisodioPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (buscaSerie.isPresent()) {
            Serie serie = buscaSerie.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodioAno = repositorio.episodioPorAno(serie ,anoLancamento);
            episodioAno.forEach(System.out::println);
        }
    }
}