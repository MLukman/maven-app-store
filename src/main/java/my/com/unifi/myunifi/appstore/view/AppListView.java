package my.com.unifi.myunifi.appstore.view;

import java.util.Map;
import javax.inject.Inject;
import my.com.unifi.myunifi.appstore.Configuration;
import my.com.unifi.myunifi.appstore.model.AppConfiguration;

public class AppListView extends BaseView {
    
    @Inject
    protected Configuration configuration;
    
    public AppListView() {
        super("AppListView.ftl");
    }

    public Map<String, AppConfiguration> getApps() {
        return configuration.apps;
    }
    
    
    
}
