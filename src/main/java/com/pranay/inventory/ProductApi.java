package com.pranay.inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

record ProductInput(@NotBlank String name,@NotBlank String sku,@NotBlank String category,@NotNull @DecimalMin("0.01") BigDecimal price,@Min(0) int quantity,@Min(0) int reorderLevel) {}
record StockInput(@Min(1) int quantity) {}
interface ProductRepo extends JpaRepository<Product,Long> { boolean existsBySku(String sku); List<Product> findByCategoryIgnoreCase(String category); }
@RestController @RequestMapping("/api/products")
class ProductApi {
 private final ProductRepo repo; ProductApi(ProductRepo r){repo=r;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) Product create(@Valid @RequestBody ProductInput r){
  if(repo.existsBySku(r.sku())) throw new ResponseStatusException(HttpStatus.CONFLICT,"SKU already exists");
  return repo.save(new Product(r.name(),r.sku(),r.category(),r.price(),r.quantity(),r.reorderLevel()));
 }
 @GetMapping List<Product> all(){return repo.findAll();}
 @GetMapping("/{id}") Product get(@PathVariable Long id){return find(id);}
 @PutMapping("/{id}") Product update(@PathVariable Long id,@Valid @RequestBody ProductInput r){
  Product p=find(id);
  if(!p.sku.equals(r.sku()) && repo.existsBySku(r.sku())) throw new ResponseStatusException(HttpStatus.CONFLICT,"SKU already exists");
  p.name=r.name();p.sku=r.sku();p.category=r.category();p.price=r.price();p.quantity=r.quantity();p.reorderLevel=r.reorderLevel();return repo.save(p);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable Long id){repo.delete(find(id));}
 @GetMapping("/category/{category}") List<Product> category(@PathVariable String category){return repo.findByCategoryIgnoreCase(category);}
 @GetMapping("/low-stock") List<Product> lowStock(){return repo.findAll().stream().filter(p->p.quantity<=p.reorderLevel).toList();}
 @PostMapping("/{id}/stock-in") Product stockIn(@PathVariable Long id,@Valid @RequestBody StockInput r){Product p=find(id);p.quantity+=r.quantity();return repo.save(p);}
 @PostMapping("/{id}/stock-out") Product stockOut(@PathVariable Long id,@Valid @RequestBody StockInput r){
  Product p=find(id);if(p.quantity<r.quantity())throw new ResponseStatusException(HttpStatus.CONFLICT,"Insufficient stock");
  p.quantity-=r.quantity();return repo.save(p);
 }
 private Product find(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));}
}
