package mk.finki.ukim.mk.lab2.repository.inMemoryRepo;

import mk.finki.ukim.mk.lab2.model.Location;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryLocationRepository {
    public List<Location> findAll(){
        return null;
    }
    public Optional<Location> findById(Long id){
        return Optional.empty();
    }
}
