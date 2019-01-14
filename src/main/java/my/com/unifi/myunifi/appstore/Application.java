package my.com.unifi.myunifi.appstore;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class Application extends io.dropwizard.Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(configuration).to(Configuration.class);
            }
        });
        environment.jersey().packages("my.com.unifi.myunifi.appstore.resource");
        //String url = "https://clr.tm.com.my/repo/repository/maven-releases/my/com/unifi/AuthToken/";
        //String url = "https://clr.tm.com.my/repo/repository/maven-snapshots/my/com/unifi/mobile/myUnifi-android/";
    }

}
