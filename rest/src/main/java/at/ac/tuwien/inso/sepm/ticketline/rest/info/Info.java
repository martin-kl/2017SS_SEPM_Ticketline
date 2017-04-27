package at.ac.tuwien.inso.sepm.ticketline.rest.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;

@Getter
@Setter
public class Info {

    private Git git;
    private Duration uptime;

    @Getter
    @Setter
    public static class Git {

        private String branch;
        private String tags;
        private Build build;
        private Commit commit;

        @Getter
        @Setter
        public static class Build {

            private String version;

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private ZonedDateTime time;

        }

        @Getter
        @Setter
        public static class Commit {

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private ZonedDateTime time;

            private Id id;

            @Getter
            @Setter
            public static class Id {

                private String abbrev;

            }
        }
    }
}