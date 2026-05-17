package in.aryaura.chess.engine.server.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GitDetails {

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;

    public static String BRANCH;

    public static String COMMIT_ID;

    @PostConstruct
    public void init() {

        BRANCH = branch;
        COMMIT_ID = commitId;
    }
}
