/** Clasa pentru popularea initiala a bazei de date cu flori
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.config;

import com.florarie.florarie.model.Flower;
import com.florarie.florarie.repository.FlowerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowerSeeder {

    @Bean
    CommandLineRunner seedFlowers(FlowerRepository flowerRepository) {
        return args -> {
            if (flowerRepository.count() == 0) {

                Flower f1 = new Flower();
                f1.setName("Trandafir");
                f1.setColor("Roșu");
                f1.setPrice(12.5);
                f1.setStock(50);
                f1.setAvailable(true);

                Flower f2 = new Flower();
                f2.setName("Lalea");
                f2.setColor("Galben");
                f2.setPrice(8.0);
                f2.setStock(60);
                f2.setAvailable(true);

                Flower f3 = new Flower();
                f3.setName("Crin");
                f3.setColor("Alb");
                f3.setPrice(15.0);
                f3.setStock(25);
                f3.setAvailable(true);

                flowerRepository.save(f1);
                flowerRepository.save(f2);
                flowerRepository.save(f3);
            }
        };
    }
}
