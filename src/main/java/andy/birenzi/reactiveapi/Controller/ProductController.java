package andy.birenzi.reactiveapi.Controller;

import andy.birenzi.reactiveapi.model.Product;
import andy.birenzi.reactiveapi.repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProductRepo repo;
    @GetMapping
    public Flux<Product> findAll(){
        return repo.findAll();
    }
    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getOneProduct(@PathVariable String id){
        logger.info("find one method");
        return repo.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product){
        return repo.save(product);
    }
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Product>> saveProduct(@PathVariable String id, @RequestBody Product product){
        return repo.findById(id)
                .flatMap(existingP-> {
                    existingP.setName(product.getName());
                    existingP.setPrice(product.getPrice());
                            return repo.save(existingP);
                } )
                .map(updateP->ResponseEntity.ok(updateP))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id, @RequestBody Product product){
        return repo.findById(id)
                .flatMap(existingP->
                        repo.delete(existingP).then(Mono.just(ResponseEntity.ok().<Void>build()))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping
    public Mono<Void> deleteAllProducts(){
       return repo.deleteAll();
    }

}
