package in.aryaura.chess.engine.server.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "git")
public class GitDetails {

    private String branch;

    private Commit commit;

    public static String BRANCH;

    public static String COMMIT_ID;

    public static String COMMIT_TIME;

    @PostConstruct
    public void init() {

        BRANCH = branch;

        if (commit != null && commit.id != null) {
            COMMIT_ID = commit.id.abbrev;
        }

        if (commit != null) {
            COMMIT_TIME = commit.time;
        }
    }

    @Getter
    @Setter
    public static class Commit {

        private Id id;

        private String time;

        @Getter
        @Setter
        public static class Id {

            private String abbrev;
        }
    }
}
