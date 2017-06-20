package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Slf4j
@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final int NUMBER_OF_NEWS_TO_GENERATE = 25;

    @Autowired
    private NewsRepository newsRepository;
    private final Faker faker = new Faker();

    @PostConstruct
    private void generateNews() throws IOException {
        if (newsRepository.count() > 0) {
            log.info("news already generated");
        } else {
            log.info("generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);
            News news = News.builder()
                .summary( "For those who didnt have enough from the long weekendnd this Tuesday again - just for local techno.")
                .title("Local Techno is back")
                .text("Wir nicht! Kein Schnick Schnack... Bei uns steht die Musik im Vordergrund! \n" +
                    "Worum es geht? Einfach nur Techno!\n" +
                    "\n" +
                    "Wir wollen zurück zu den Anfängen, als es nur um die Musik ging. \"Love, Peace and Harmony\" waren einst die Grundwerte, heute passt das Motto immer seltener. Deswegen “back to the roots!”\n" +
                    "Alles, das von den treibenden Bässen und dem speziellen Feeling ablenkt wird nicht wirklich gebraucht. Wir lassen es einfach weg.\n" +
                    "Keine übertriebene Werbung, keine übertriebene Lichtshow, kein Timetable, etc. \n" +
                    "EINFACH NUR TECHNO!\n" +
                    "Techno und gute Stimmung, mehr braucht es meistens nicht. Lasst euch einfach mal wieder treiben!\n")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/techno.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            log.debug("saving news {}", news);
            newsRepository.save(news);
            news = News.builder()
                .summary("Censored Tickets müssen heute verkauft werden")
                .title("Censored Tickets")
                .text("Alle VerkäuferInnen sollen heute bitte besonderen Wert darauf legen Censored Tickets zu verkaufen. \n Die Geschäftsleitung")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/censored.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            log.debug("saving news {}", news);
            newsRepository.save(news);
            news = News.builder()
                .summary("Alle Maturanten haben die Zentralmatura mehr oder weniger gut überstanden und jetzt ist mal so richtig Feiern angesagt")
                .title("Tuesday 4 Club - Reifeprüfung")
                .text("\n" +
                    "Endlich ist es soweit. Alle Maturanten haben die Zentralmatura mehr oder weniger gut überstanden und jetzt ist mal so richtig Feiern angesagt. Und das gleich den ganzen Sommer lang. Wir geben den Auftakt mit einer Reifeprüfung der ganz besonderen Art, die euch in das Studentenleben einführt.\n" +
                    "\n" +
                    "Gratiseintritt* für alle Maturanten (Kopie des Maturazeugnisses nicht vergessen!)!\n" +
                    "\n" +
                    "(*Es sind lediglich 2€ an Vergnügungssteuerabgaben zu entrichten.)\n" +
                    "\n" +
                    "Wir verschenken wieder ein Flaschen Set\n" +
                    "Vodka/Orange und 5 Gästelistenplätze.\n" +
                    "Postet in das Event:\n" +
                    "\"Reif für den TuesdayClub\"\n" +
                    "und makiert 4 eurer Freunde.\n" +
                    "\n" +
                    "EMIL´s SPECIALS :\n" +
                    "-) 5€ Cocktail - HappyHour 22 - 24Uhr\n" +
                    "-) Bis 24 Uhr Free Welcome Shots\n" +
                    "-) Gratis Eintritt mit Maturazeugnis\n" +
                    "\n" +
                    "Regulärer Eintritt: 10€\n" +
                    "\n" +
                    "DJ LINE-UP:\n" +
                    "Mainfloor: DJ Berlü\n" +
                    "Clubfloor: DJ One\n" +
                    "\n" +
                    "Was spielts da für Musik?\n" +
                    "Mainfloor - Happy Sound, Charts, Classic & alles was deppat ist!\n" +
                    "Clubfloor - HipHop/RnB/Trap & immer etwas geiles!\n" +
                    "\n" +
                    "TU ES ab 22 Uhr im U4\n" +
                    "(12. Bez., Schönbrunnerstr. 222)\n" +
                    "\n" +
                    "> > > Ermäßigter Eintritt für alle X-Jamer mit X-Card\n" +
                    "\n" +
                    "Ihr wollt einen Tisch bei uns reservieren?\n" +
                    "Dann meldet Euch beim tableservice@u-4.at\n" +
                    "\n" +
                    "Bussi\n" +
                    "Eure TuesdayClub Bagage")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/albertina.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            newsRepository.save(news);

            news = News.builder()
                .summary("Lake Neon laden zum Tanzen und Träumen ein.")
                .title("LAKE NEON")
                .text("Diesmal live: Lake Neon && Eugene Delta\n" +
                    "\n" +
                    "DOORS OPEN: 20h\n" +
                    "ENTRY FREE - DONATIONS ARE NICE :)\n" +
                    "\n" +
                    "Im Anschluss wie immer: \n" +
                    "WANNAPLAY-INDIE-AWESOMENESS-CLUB\n" +
                    "\n" +
                    "++++++++++++++++++++++\n" +
                    "\n" +
                    "LAKE NEON\n" +
                    "https://www.facebook.com/LakeNeonMusic/\n" +
                    "https://soundcloud.com/lake_neon\n" +
                    "\n" +
                    "Lake Neon laden zum Tanzen und Träumen ein. Zwei wunderschöne Stimmen treffen auf Melodien, die einfach glücklich machen. Lake Neon gehen einem sofort nahe: Sie berühren Ohr und Herz, ohne kitschig oder nach klassischem Indie-Pop zu klingen. Der eigene, verträumte Sound der vier zeigt, dass sich Wien nicht hinter Musikmetropolen wie Berlin oder Hamburg verstecken muss. Lake Neon bringen mit, was man für die Bühne braucht: Liebe und Hingabe zur Musik.\n" +
                    "\n" +
                    "++++++++++++++++++++++\n" +
                    "\n" +
                    "EUGENE DELTA\n" +
                    "https://www.facebook.com/eugenedelta/\n" +
                    "http://eugenedelta.com/")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/oper.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            newsRepository.save(news);

            news = News.builder()
                .summary("Wir starten heuer erstmalig mit 'Ritmo de la noche' – und das mit zwei Dance Floors!")
                .title("RITMO DE LA NOCHE ")
                .text("Wir starten heuer erstmalig mit \"Ritmo de la noche\" – und das mit zwei Dance Floors! DJ Gringo wird Salsa, Bachata, Cha Cha Cha und Son präsentieren. Am 2. Dancefloor werden sich verschiedene DJs aus dem In- und Ausland mit Reggaeton, Latin Hits, Salsa, Merengue, Bachata und Kizomba abwechseln. Hier ist für jeden Geschmack etwas dabei - Vamos a bailar!\n" +
                    "\n" +
                    "Die Veranstaltung findet nur bei Schönwetter statt! Info's gegebenenfalls am Veranstaltungstag ab 13 Uhr auf Fb und www.vcbc.at\n" +
                    "\n" +
                    "X Line Up am 20.6.2017 X\n" +
                    "Floor 1: DJ Gringo ab 20 Uhr\n" +
                    "Floor 2: DJ Hernan ab 19 Uhr\n" +
                    "\n" +
                    "X Specials X\n" +
                    "Rueda de Casino um 20:45 Uhr mit Marco Barona-Sanchez und um 22:00 Uhr mit Norbert Gerstl\n" +
                    "\n" +
                    "Tanzkurse mit Javier Marin-Colorado Salsa-Workshop von 19:00-20:00 Uhr\n" +
                    "\n" +
                    "Javier stammt aus Kolumbien und ist seit vielen Jahren Tanz Instruktor für Salsa in Wien. In diesem Sommer wird er bei uns in Form von Workshops und Schnupperstunden unterrichten. Mehr Infos auf www.salsamania.at und www.salsavienna.at\n" +
                    "\n" +
                    "Anmeldung für den Bachata-Kurs bitte schriftlich unter: javier.marin@aon.at\n" +
                    "\n" +
                    "X Aktion X\n" +
                    "Happy Hour 18:00 - 20:00 Uhr\n" +
                    "Mojito -50% powered by Pampero\n" +
                    "\n" +
                    "X Mehr Infos X\n" +
                    "www.vcbc.at\n" +
                    "www.facebook.com/vcbc.at\n" +
                    "www.instagram.com/vcbc.at")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/esp.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            newsRepository.save(news);

            news = News.builder()
                .summary("Good Vibes - zu viele Tickets übrig")
                .title("Good Vibes")
                .text("Good Vibes. Bitte vermehrt empfehlen " +
                    "\n" +
                    "X Line Up X\n" +
                    "Mr. Gee all night long\n" +
                    "\n" +
                    "X Aktion X\n" +
                    "#Happy #Hour 18:00 - 20:00 Uhr Desperados um nur € 3,20")
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/eugene.jpg")))
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .build();
            newsRepository.save(news);

            news = News.builder()
                .title("RED FANG")
                .summary("Das neueste Werk der Stoner-Rock-Band aus Oregon erschien im Herbst 2016.")
                .text("Das neueste Werk der Stoner-Rock-Band aus Oregon erschien im Herbst 2016. \"Only Ghost\" besteht aus zehn ausdrucksstarken Tracks, von denen jeder einzelne den Charakter von Red Fang unterstreicht. Produziert wurde es von Ross Robin (At The Drive In, The Cure, Slipknot , and many more) und den Mix übernahm Joe Baressi (Queens of the Stone Age, Kyuss, Melvins). Red Fang beweisen damit wieder, dass sie Meister der härteren Klänge sind ohne sich selbst allzu ernst zu nehmen.\n" +
                    "\n" +
                    "Tickets gibt's ab Mi, 12.04. / 10:00 Uhr bei:\n" +
                    "www.musicticket.at\n" +
                    "www.oeticket.com\n" +
                    "sowie in allen oeticket.com- Vorverkaufsstellen und europaweit über das Eventim-Vertriebsnetz.\n" +
                    "Ermäßigte Tickets für 14-19 Jährige sind bei JugendTicket im oeticket Center Halle E + G im MuseumsQuartier gegen Vorlage eines Ausweises erhältlich.")
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/red.jpg")))
                .build();
            newsRepository.save(news);

            news = News.builder()
                .title("LIFE BALL")
                .summary("Ganz schön bunt")
                .text("Der Life Ball (englisch für Lebensball) in Wien ist die größte Benefiz-Veranstaltung in Europa zu Gunsten HIV-infizierter und AIDS-erkrankter Menschen.[1] Hinter dem Life Ball steht der 1992 von den selbst Betroffenen Gery Keszler[2] und dem Arzt Torgom Petrosian († 1993)[3] gegründete karitative Verein Aids Life.[4]\n" +
                    "Aids Life unterstützt Non-Profit-Organisationen, die sich für HIV-positive und an AIDS erkrankte Menschen einsetzen. Zudem ist es ein erklärtes Ziel von Aids Life, die Öffentlichkeit über die Gefahren von HIV/AIDS aufzuklären und ihr Bewusstsein für die Krankheit zu schärfen.\n" +
                    "Der Ball findet seit 1993 alljährlich statt, 2016 wurde pausiert.")
                .publishedAt(
                    LocalDateTime.ofInstant(
                        faker.date()
                            .past(365 * 3, TimeUnit.DAYS).
                            toInstant(),
                        ZoneId.systemDefault()
                    ))
                .image(Files.readAllBytes(Paths.get("server/src/main/resources/dataGeneratorImages/lifeball.jpg")))
                .build();
            newsRepository.save(news);
            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                news = News.builder()
                    .summary(faker.lorem().characters(25, 100))
                    .title(faker.lorem().characters(10, 30))
                    .text(faker.lorem().paragraph(faker.number().numberBetween(8, 16)))
                    .publishedAt(
                        LocalDateTime.of(1900, 1, 1, 1, i)
                    )
                    .build();
                log.debug("saving news {}", news);
                newsRepository.save(news);
            }
        }
    }
}
