package andy.birenzi.reactiveapi;

import andy.birenzi.reactiveapi.model.Product;
import andy.birenzi.reactiveapi.repository.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveapiApplication.class, args);

	}
	@Bean
	CommandLineRunner init(ProductRepo repo){
	return args -> {
				Flux<Product> productFlux = Flux.just(
						new Product(null, "Test1",2.66),
						new Product(null, "Test1",2.66),
						new Product(null, "Test1",2.66)

				).flatMap(p->repo.save(p));
				productFlux.thenMany(repo.findAll())
						.subscribe(System.out::println);
			};
	}
}
