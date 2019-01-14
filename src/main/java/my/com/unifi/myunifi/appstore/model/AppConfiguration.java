package my.com.unifi.myunifi.appstore.model;

import javax.validation.constraints.NotNull;

public class AppConfiguration {

    @NotNull
    public String name;

    static public enum Type {
        ANDROID("ANDROID"),
        IOS("IOS");

        private final String text;

        Type(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public Type type = Type.ANDROID;

    @NotNull
    public String repository;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getRepository() {
        return repository;
    }

}
