package my.com.unifi.myunifi.appstore.view;

import my.com.unifi.myunifi.appstore.model.AppConfiguration;
import my.com.unifi.myunifi.appstore.model.RemoteMavenPackage;

public class AppInfoView extends BaseView {

    protected String appId;
    protected AppConfiguration appConfig;
    protected RemoteMavenPackage maven;

    public AppInfoView(String appId, AppConfiguration appConfig, RemoteMavenPackage maven) {
        super("AppInfoView.ftl");
        this.appId = appId;
        this.appConfig = appConfig;
        this.maven = maven;
    }

    public String getAppId() {
        return appId;
    }

    public AppConfiguration getAppConfig() {
        return appConfig;
    }

    public RemoteMavenPackage getMaven() {
        return maven;
    }

}
