package com.movieticket.util;

import com.movieticket.entity.*;
import com.movieticket.repository.*;
import com.movieticket.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatService seatService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").description("Administrator role").build());
        }
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(Role.builder().name("USER").description("Regular user role").build());
        }

        // Create sample admin user
        if (userRepository.findByEmail("admin@movieticket.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").get();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User adminUser = User.builder()
                    .email("admin@movieticket.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .status("ACTIVE")
                    .roles(roles)
                    .build();
            userRepository.save(adminUser);
        }

        // Create sample user
        if (userRepository.findByEmail("user@movieticket.com").isEmpty()) {
            Role userRole = roleRepository.findByName("USER").get();
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User user = User.builder()
                    .email("user@movieticket.com")
                    .password(passwordEncoder.encode("user123"))
                    .firstName("John")
                    .lastName("Doe")
                    .phoneNumber("1234567890")
                    .status("ACTIVE")
                    .roles(roles)
                    .build();
            userRepository.save(user);
        }

        // Create sample movies
        if (movieRepository.count() == 0) {
            List<Movie> sampleMovies = List.of(
                    Movie.builder()
                            .title("The Shawshank Redemption")
                            .description("Two imprisoned men bond over years, finding solace and eventual redemption through acts of common decency.")
                            .genre("Drama")
                            .language("English")
                            .releaseDate(LocalDate.of(1994, 10, 14))
                            .duration(142)
                            .status("ACTIVE")
                            .rating(9.3)
                            .posterUrl("https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg")
                            .build(),
                    Movie.builder()
                            .title("The Godfather")
                            .description("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.")
                            .genre("Crime")
                            .language("English")
                            .releaseDate(LocalDate.of(1972, 3, 24))
                            .duration(175)
                            .status("ACTIVE")
                            .rating(9.2)
                            .posterUrl("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg")
                            .build(),
                    Movie.builder()
                            .title("The Dark Knight")
                            .description("Batman faces the Joker, a criminal mastermind who thrusts Gotham into anarchy and pushes him to new limits.")
                            .genre("Action")
                            .language("English")
                            .releaseDate(LocalDate.of(2008, 7, 18))
                            .duration(152)
                            .status("ACTIVE")
                            .rating(9.0)
                            .posterUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg")
                            .build(),
                    Movie.builder()
                            .title("Inception")
                            .description("A skilled thief who steals secrets through dream-sharing technology is given a chance at redemption.")
                            .genre("Sci-Fi")
                            .language("English")
                            .releaseDate(LocalDate.of(2010, 7, 16))
                            .duration(148)
                            .status("ACTIVE")
                            .rating(8.8)
                            .posterUrl("https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg")
                            .build(),
                    Movie.builder()
                            .title("Interstellar")
                            .description("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.")
                            .genre("Sci-Fi")
                            .language("English")
                            .releaseDate(LocalDate.of(2014, 11, 7))
                            .duration(169)
                            .status("ACTIVE")
                            .rating(8.6)
                            .posterUrl("https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg")
                            .build(),
                    Movie.builder()
                            .title("Parasite")
                            .description("Greed and class discrimination threaten the symbiotic relationship formed between two families.")
                            .genre("Thriller")
                            .language("Korean")
                            .releaseDate(LocalDate.of(2019, 5, 30))
                            .duration(132)
                            .status("ACTIVE")
                            .rating(8.6)
                            .posterUrl("https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg")
                            .build(),
                    Movie.builder()
                            .title("Avengers: Endgame")
                            .description("The remaining Avengers assemble once more to reverse Thanos' actions and restore balance to the universe.")
                            .genre("Action")
                            .language("English")
                            .releaseDate(LocalDate.of(2019, 4, 26))
                            .duration(181)
                            .status("ACTIVE")
                            .rating(8.4)
                            .posterUrl("https://image.tmdb.org/t/p/w500/ulzhLuWrPK07P1YkdWQLZnQh1JL.jpg")
                            .build(),
                    Movie.builder()
                            .title("La La Land")
                            .description("A jazz pianist and an aspiring actress fall in love while pursuing their dreams in Los Angeles.")
                            .genre("Romance")
                            .language("English")
                            .releaseDate(LocalDate.of(2016, 12, 9))
                            .duration(128)
                            .status("ACTIVE")
                            .rating(8.0)
                            .posterUrl("https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg")
                            .build(),
                    Movie.builder()
                            .title("Joker")
                            .description("Arthur Fleck, a mentally troubled comedian, embarks on a downward spiral that leads to the creation of the Joker.")
                            .genre("Thriller")
                            .language("English")
                            .releaseDate(LocalDate.of(2019, 10, 4))
                            .duration(122)
                            .status("ACTIVE")
                            .rating(8.5)
                            .posterUrl("https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg")
                            .build(),
                    Movie.builder()
                            .title("Spider-Man: Into the Spider-Verse")
                            .description("Teen Miles Morales becomes Spider-Man and joins other Spider-People to save their universes.")
                            .genre("Animation")
                            .language("English")
                            .releaseDate(LocalDate.of(2018, 12, 14))
                            .duration(117)
                            .status("ACTIVE")
                            .rating(8.4)
                            .posterUrl("https://image.tmdb.org/t/p/w500/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg")
                            .build(),
                    Movie.builder()
                            .title("Titanic")
                            .description("A romance blossoms aboard the ill-fated maiden voyage of the RMS Titanic.")
                            .genre("Romance")
                            .language("English")
                            .releaseDate(LocalDate.of(1997, 12, 19))
                            .duration(195)
                            .status("ACTIVE")
                            .rating(7.9)
                            .posterUrl("https://image.tmdb.org/t/p/w500/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg")
                            .build(),
                    Movie.builder()
                            .title("Gladiator")
                            .description("A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family.")
                            .genre("Action")
                            .language("English")
                            .releaseDate(LocalDate.of(2000, 5, 5))
                            .duration(155)
                            .status("ACTIVE")
                            .rating(8.5)
                            .posterUrl("https://image.tmdb.org/t/p/w500/pRn3TJHbAqCAOqgrJqN5jLRGpWX.jpg")
                            .build(),
                    Movie.builder()
                            .title("Whiplash")
                            .description("A promising young drummer enrolls at a cut-throat music conservatory where his dreams are mentored by an instructor who will stop at nothing.")
                            .genre("Drama")
                            .language("English")
                            .releaseDate(LocalDate.of(2014, 10, 10))
                            .duration(106)
                            .status("ACTIVE")
                            .rating(8.5)
                            .posterUrl("https://image.tmdb.org/t/p/w500/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg")
                            .build(),
                    Movie.builder()
                            .title("Mad Max: Fury Road")
                            .description("In a post-apocalyptic wasteland, Max teams up with a mysterious woman to flee from a warlord.")
                            .genre("Action")
                            .language("English")
                            .releaseDate(LocalDate.of(2015, 5, 15))
                            .duration(120)
                            .status("ACTIVE")
                            .rating(8.1)
                            .posterUrl("https://image.tmdb.org/t/p/w500/8tZYtuWezp8JbcsvHYO0O46tFbo.jpg")
                            .build(),
                    Movie.builder()
                            .title("The Lord of the Rings: The Fellowship of the Ring")
                            .description("A meek Hobbit and eight companions set out on a journey to destroy the powerful One Ring.")
                            .genre("Fantasy")
                            .language("English")
                            .releaseDate(LocalDate.of(2001, 12, 19))
                            .duration(178)
                            .status("ACTIVE")
                            .rating(8.8)
                            .posterUrl("https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg")
                            .build(),
                    Movie.builder()
                            .title("The Matrix")
                            .description("A hacker learns the truth about his reality and his role in the war against its controllers.")
                            .genre("Sci-Fi")
                            .language("English")
                            .releaseDate(LocalDate.of(1999, 3, 31))
                            .duration(136)
                            .status("ACTIVE")
                            .rating(8.7)
                            .posterUrl("https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg")
                            .build(),
                    Movie.builder()
                            .title("Fight Club")
                            .description("An insomniac office worker and a soap maker form an underground fight club that evolves into something much, much more.")
                            .genre("Drama")
                            .language("English")
                            .releaseDate(LocalDate.of(1999, 10, 15))
                            .duration(139)
                            .status("ACTIVE")
                            .rating(8.7)
                            .posterUrl("https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg")
                            .build(),
                    Movie.builder()
                            .title("The Social Network")
                            .description("Harvard student Mark Zuckerberg creates the social networking site that would become Facebook, but is sued by the twins who claimed he stole their idea.")
                            .genre("Drama")
                            .language("English")
                            .releaseDate(LocalDate.of(2010, 10, 1))
                            .duration(120)
                            .status("ACTIVE")
                            .rating(7.7)
                            .posterUrl("https://image.tmdb.org/t/p/w500/n0ybibhJtQ5icDqTp8eRytcIHJx.jpg")
                            .build(),
                    Movie.builder()
                            .title("Coco")
                            .description("Aspiring musician Miguel enters the Land of the Dead to find his great-great-grandfather, a legendary singer.")
                            .genre("Animation")
                            .language("English")
                            .releaseDate(LocalDate.of(2017, 11, 22))
                            .duration(105)
                            .status("ACTIVE")
                            .rating(8.4)
                            .posterUrl("https://image.tmdb.org/t/p/w500/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg")
                            .build(),
                    Movie.builder()
                            .title("Dune")
                            .description("Paul Atreides must travel to the most dangerous planet in the universe to ensure the future of his family and his people.")
                            .genre("Sci-Fi")
                            .language("English")
                            .releaseDate(LocalDate.of(2021, 10, 22))
                            .duration(155)
                            .status("ACTIVE")
                            .rating(8.1)
                            .posterUrl("https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg")
                            .build(),
                    Movie.builder()
                            .title("Everything Everywhere All at Once")
                            .description("An aging Chinese immigrant is swept up in an insane adventure across the multiverse.")
                            .genre("Adventure")
                            .language("English")
                            .releaseDate(LocalDate.of(2022, 3, 25))
                            .duration(139)
                            .status("ACTIVE")
                            .rating(8.0)
                            .posterUrl("https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg")
                            .build()
            );

            movieRepository.saveAll(sampleMovies);
        }

        // Create sample theaters and screens
        if (theaterRepository.count() == 0) {
            Theater theater = theaterRepository.save(Theater.builder()
                    .name("PVR Cinema")
                    .city("New York")
                    .address("123 Broadway, New York, NY 10001")
                    .phoneNumber("555-0101")
                    .build());

            screenRepository.save(Screen.builder()
                    .theater(theater)
                    .screenNumber("Screen 1")
                    .totalSeats(100)
                    .build());

            screenRepository.save(Screen.builder()
                    .theater(theater)
                    .screenNumber("Screen 2")
                    .totalSeats(80)
                    .build());

            screenRepository.save(Screen.builder()
                    .theater(theater)
                    .screenNumber("Screen 3")
                    .totalSeats(60)
                    .build());
        }

        // Initialize seats for each screen once
        if (seatRepository.count() == 0) {
            screenRepository.findAll().forEach(screen ->
                    seatService.initializeScreenSeats(screen.getId(), screen.getTotalSeats())
            );
        }

        // Create sample showtimes for all movies
        if (showtimeRepository.count() == 0) {
            Theater theater = theaterRepository.findAll().get(0);
            List<Screen> screens = screenRepository.findAll();
            List<Movie> movies = movieRepository.findAll();
            int dayOffset = 1;

            for (Movie movie : movies) {
                for (int i = 0; i < screens.size(); i++) {
                    Screen screen = screens.get(i);
                    LocalDateTime showTime = LocalDateTime.now()
                            .withHour(14 + (i * 2))
                            .withMinute(0)
                            .plusDays(dayOffset + i);

                    showtimeRepository.save(Showtime.builder()
                            .movie(movie)
                            .theater(theater)
                            .screen(screen)
                            .showDateTime(showTime)
                            .ticketPrice(10.0 + (i * 2))
                            .status("AVAILABLE")
                            .build());
                }
                dayOffset++;
            }
        }
    }
}
