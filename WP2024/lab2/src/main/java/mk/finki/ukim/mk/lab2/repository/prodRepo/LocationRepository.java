package mk.finki.ukim.mk.lab2.repository.prodRepo;

import mk.finki.ukim.mk.lab2.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
