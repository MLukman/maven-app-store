package my.com.unifi.myunifi.appstore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import my.com.unifi.myunifi.appstore.model.AppConfiguration;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration extends io.dropwizard.Configuration {

    public Map<String, AppConfiguration> apps;

}
